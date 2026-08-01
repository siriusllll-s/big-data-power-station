package qrsoft.information.station.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qrsoft.common.entity.Station;
import qrsoft.common.entity.StationSolarPrice;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.input.StationSolarPriceInput;
import qrsoft.information.dto.output.StationSimpleOutput;
import qrsoft.information.dto.output.StationSolarPriceOutput;
import qrsoft.information.dto.page.StationSolarPricePage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.mapper.StationMapper;
import qrsoft.information.mapper.StationSolarPriceMapper;
import qrsoft.information.station.service.IStationSolarPriceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StationSolarPriceServiceImpl implements IStationSolarPriceService {

	@Autowired
	private StationSolarPriceMapper priceMapper;
	@Autowired
	private StationMapper stationMapper;

	@Override
	@Transactional
	public void saveOrUpdate(StationSolarPriceInput input) {
		if (input == null) {
			throw new RuntimeException("参数不能为空");
		}
		if (input.getPrice() == null) {
			throw new RuntimeException("电价不能为空");
		}
		StationSolarPrice entity = input.getId() == null ? new StationSolarPrice() : priceMapper.selectById(input.getId());
		if (input.getId() != null && entity == null) {
			throw new RuntimeException("电价记录不存在或已删除");
		}
		if (entity.getDelFlag() != null && entity.getDelFlag() == 1) {
			throw new RuntimeException("电价记录不存在或已删除");
		}
		Integer stationId = input.getStation() != null ? input.getStation() : entity.getStation();
		if (stationId == null) {
			stationId = 1;
		}
		entity.setStation(stationId);
		entity.setPrice(input.getPrice());
		entity.setMemo(input.getMemo());
		if (StringUtils.isNotBlank(input.getBeginDate())) {
			try {
				String d = input.getBeginDate().length() >= 10 ? input.getBeginDate().substring(0, 10) : input.getBeginDate();
				entity.setBeginDate(DateUtil.stringToDate(d, DateUtil.YYMMDD));
			} catch (Exception e) {
				throw new RuntimeException("实施日期格式错误，需 yyyy-MM-dd");
			}
		} else if (input.getId() == null) {
			throw new RuntimeException("实施日期不能为空");
		}
		if (entity.getDelFlag() == null) {
			entity.setDelFlag(0);
		}
		int i = input.getId() == null ? priceMapper.insert(entity) : priceMapper.updateById(entity);
		if (i != 1) {
			throw new RuntimeException("电价保存失败");
		}
	}

	@Override
	public ResultPage<StationSolarPriceOutput> pageByParam(StationSolarPricePage input) {
		if (input == null) {
			input = new StationSolarPricePage();
		}
		QueryWrapper<StationSolarPrice> query = new QueryWrapper<>();
		query.eq("del_flag", 0);
		if (input.getStation() != null) {
			query.eq("station", input.getStation());
		}
		if (StringUtils.isNotBlank(input.getStart())) {
			query.ge("begin_date", input.getStart());
		}
		if (StringUtils.isNotBlank(input.getEnd())) {
			query.le("begin_date", input.getEnd());
		}
		query.orderByDesc("begin_date").orderByDesc("id");
		int pageNo = input.getPage() == null || input.getPage() < 1 ? 1 : input.getPage();
		int pageSize = input.getLimit() == null || input.getLimit() < 1 ? 10 : input.getLimit();
		Page<StationSolarPrice> page = priceMapper.selectPage(new Page<>(pageNo, pageSize), query);
		ResultPage<StationSolarPriceOutput> result = new ResultPage<>(page);
		Map<Integer, StationSimpleOutput> stationCache = new HashMap<>();
		List<StationSolarPriceOutput> list = page.getRecords().stream().map(e -> {
			StationSolarPriceOutput o = StationSolarPriceOutput.entityToOutput(e);
			o.setStationObj(loadStation(e.getStation(), stationCache));
			return o;
		}).collect(Collectors.toList());
		result.setList(list);
		return result;
	}

	@Override
	public StationSolarPriceOutput detail(Integer id) {
		StationSolarPrice entity = priceMapper.selectById(id);
		if (entity == null || (entity.getDelFlag() != null && entity.getDelFlag() == 1)) {
			throw new RuntimeException("电价记录不存在或已删除");
		}
		StationSolarPriceOutput o = StationSolarPriceOutput.entityToOutput(entity);
		o.setStationObj(loadStation(entity.getStation(), new HashMap<>()));
		return o;
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		StationSolarPrice entity = priceMapper.selectById(id);
		if (entity == null) {
			throw new RuntimeException("电价记录不存在");
		}
		entity.setDelFlag(1);
		int i = priceMapper.updateById(entity);
		if (i != 1) {
			throw new RuntimeException("电价删除失败");
		}
	}

	private StationSimpleOutput loadStation(Integer stationId, Map<Integer, StationSimpleOutput> cache) {
		if (stationId == null) {
			return StationSimpleOutput.entityToOutput(null);
		}
		if (cache.containsKey(stationId)) {
			return cache.get(stationId);
		}
		Station station = stationMapper.selectById(stationId);
		StationSimpleOutput simple = StationSimpleOutput.entityToOutput(station);
		cache.put(stationId, simple);
		return simple;
	}
}
