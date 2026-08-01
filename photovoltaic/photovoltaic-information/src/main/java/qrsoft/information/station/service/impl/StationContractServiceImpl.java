package qrsoft.information.station.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qrsoft.common.entity.Station;
import qrsoft.common.entity.StationContract;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.input.StationContractInput;
import qrsoft.information.dto.output.StationContractOutput;
import qrsoft.information.dto.output.StationSimpleOutput;
import qrsoft.information.dto.page.StationContractPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.mapper.StationContractMapper;
import qrsoft.information.mapper.StationMapper;
import qrsoft.information.station.service.IStationContractService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StationContractServiceImpl implements IStationContractService {

	@Autowired
	private StationContractMapper contractMapper;
	@Autowired
	private StationMapper stationMapper;

	@Override
	@Transactional
	public void saveOrUpdate(StationContractInput input) {
		if (input == null) {
			throw new RuntimeException("参数不能为空");
		}
		StationContract entity = input.getId() == null ? new StationContract() : contractMapper.selectById(input.getId());
		if (input.getId() != null && entity == null) {
			throw new RuntimeException("合同记录不存在或已删除");
		}
		if (entity.getDelFlag() != null && entity.getDelFlag() == 1) {
			throw new RuntimeException("合同记录不存在或已删除");
		}
		Integer stationId = input.getStation() != null ? input.getStation() : entity.getStation();
		if (stationId == null) {
			stationId = 1;
		}
		entity.setStation(stationId);
		if (input.getNo() != null) {
			entity.setNo(input.getNo());
		}
		if (input.getContractPower() != null) {
			entity.setContractPower(input.getContractPower());
		}
		if (input.getProtocolPr() != null) {
			entity.setProtocolPr(input.getProtocolPr());
		}
		if (input.getEfficiency() != null) {
			entity.setEfficiency(input.getEfficiency());
		}
		if (input.getAvgRadio() != null) {
			entity.setAvgRadio(input.getAvgRadio());
		}
		if (input.getMemo() != null) {
			entity.setMemo(input.getMemo());
		}
		try {
			if (StringUtils.isNotBlank(input.getBeginDate())) {
				String d = input.getBeginDate().length() >= 10 ? input.getBeginDate().substring(0, 10) : input.getBeginDate();
				entity.setBeginDate(DateUtil.stringToDate(d, DateUtil.YYMMDD));
			}
			if (StringUtils.isNotBlank(input.getEndDate())) {
				String d = input.getEndDate().length() >= 10 ? input.getEndDate().substring(0, 10) : input.getEndDate();
				entity.setEndDate(DateUtil.stringToDate(d, DateUtil.YYMMDD));
			}
		} catch (Exception e) {
			throw new RuntimeException("日期格式错误，需 yyyy-MM-dd");
		}
		if (input.getId() == null) {
			if (entity.getBeginDate() == null || entity.getEndDate() == null) {
				throw new RuntimeException("合同起止时间不能为空");
			}
			if (entity.getContractPower() == null) {
				throw new RuntimeException("合同发电量不能为空");
			}
		}
		if (entity.getDelFlag() == null) {
			entity.setDelFlag(0);
		}
		int i = input.getId() == null ? contractMapper.insert(entity) : contractMapper.updateById(entity);
		if (i != 1) {
			throw new RuntimeException("合同保存失败");
		}
	}

	@Override
	public ResultPage<StationContractOutput> pageByParam(StationContractPage input) {
		if (input == null) {
			input = new StationContractPage();
		}
		QueryWrapper<StationContract> query = new QueryWrapper<>();
		query.eq("del_flag", 0);
		if (input.getStation() != null) {
			query.eq("station", input.getStation());
		}
		if (StringUtils.isNotBlank(input.getNo())) {
			query.like("no", input.getNo());
		}
		// 查询区：合同时间与记录区间有交集
		if (StringUtils.isNotBlank(input.getBeginDate())) {
			query.ge("end_date", input.getBeginDate());
		}
		if (StringUtils.isNotBlank(input.getEndDate())) {
			query.le("begin_date", input.getEndDate());
		}
		query.orderByDesc("begin_date").orderByDesc("id");
		int pageNo = input.getPage() == null || input.getPage() < 1 ? 1 : input.getPage();
		int pageSize = input.getLimit() == null || input.getLimit() < 1 ? 10 : input.getLimit();
		Page<StationContract> page = contractMapper.selectPage(new Page<>(pageNo, pageSize), query);
		ResultPage<StationContractOutput> result = new ResultPage<>(page);
		Map<Integer, StationSimpleOutput> cache = new HashMap<>();
		List<StationContractOutput> list = page.getRecords().stream().map(e -> {
			StationContractOutput o = StationContractOutput.entityToOutput(e);
			o.setStationObj(loadStation(e.getStation(), cache));
			return o;
		}).collect(Collectors.toList());
		result.setList(list);
		return result;
	}

	@Override
	public StationContractOutput detail(Integer id) {
		StationContract entity = contractMapper.selectById(id);
		if (entity == null || (entity.getDelFlag() != null && entity.getDelFlag() == 1)) {
			throw new RuntimeException("合同记录不存在或已删除");
		}
		StationContractOutput o = StationContractOutput.entityToOutput(entity);
		o.setStationObj(loadStation(entity.getStation(), new HashMap<>()));
		return o;
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		StationContract entity = contractMapper.selectById(id);
		if (entity == null) {
			throw new RuntimeException("合同记录不存在");
		}
		entity.setDelFlag(1);
		int i = contractMapper.updateById(entity);
		if (i != 1) {
			throw new RuntimeException("合同删除失败");
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
