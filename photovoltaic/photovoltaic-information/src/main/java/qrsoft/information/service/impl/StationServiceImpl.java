package qrsoft.information.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qrsoft.common.entity.*;
import qrsoft.common.util.SQLUtil;
import qrsoft.information.dto.input.StationInput;
import qrsoft.information.dto.input.StationPhotoInput;
import qrsoft.information.dto.output.*;
import qrsoft.information.dto.page.StationPage;
import qrsoft.information.dto.vo.ResultPage;
import qrsoft.information.mapper.*;
import qrsoft.information.service.IStationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StationServiceImpl implements IStationService {

	@Autowired
	private StationMapper stationMapper;
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private ProvinceMapper provinceMapper;
	@Autowired
	private CityMapper cityMapper;
	@Autowired
	private AreaMapper areaMapper;

	@Override
	@Transactional
	public void saveOrUpdate(StationInput input) {
		Station station = input.getId() == null ? new Station() : stationMapper.selectById(input.getId());
		if (input.getId() != null && station == null) {
			throw new RuntimeException("电站信息不存在或已删除");
		}
		Integer count = stationMapper.countByName(input.getName());
		if (input.getId() == null && count != null && count != 0) {
			throw new RuntimeException("电站名称已使用");
		}
		if (input.getId() != null && !station.getName().equals(input.getName()) && count != null && count != 0) {
			throw new RuntimeException("电站名称已使用");
		}
		StationInput.inputToEntity(station, input);
		if (station.getDelFlag() == null) {
			station.setDelFlag(0);
		}
		int i = input.getId() == null ? stationMapper.insert(station) : stationMapper.updateById(station);
		if (i != 1) {
			throw new RuntimeException("数据保存失败");
		}
	}

	@Override
	@Transactional
	public void savePhoto(StationPhotoInput input) {
		Station station = stationMapper.selectById(input.getStation());
		if (station == null) {
			throw new RuntimeException("电站信息不存在或已删除");
		}
		station.setPhotoPath(input.getPhoto());
		int i = stationMapper.updateById(station);
		if (i != 1) {
			throw new RuntimeException("图片信息保存失败");
		}
	}

	@Override
	public ResultPage<StationSimpleOutput> pageByParam(StationPage input) {
		QueryWrapper<Station> query = new QueryWrapper<>();
		query.select("id", "name", "short_name", "type", "status", "install_capacity", "own", "maintain_person", "lon", "lat");
		if (StringUtils.isNotBlank(input.getName())) {
			query.like("name", SQLUtil.filter(input.getName()));
		}
		if (input.getType() != null) {
			query.eq("type", input.getType());
		}
		if (input.getStatus() != null) {
			query.eq("status", input.getStatus());
		}
		if (StringUtils.isNotBlank(input.getOwn())) {
			query.like("own", SQLUtil.filter(input.getOwn()));
		}
		if (StringUtils.isNotBlank(input.getMaintainPerson())) {
			query.exists("select id from sys_user where id = station.maintain_person and del_flag = 0 and name like '%"
					+ SQLUtil.filter(input.getMaintainPerson()) + "%'");
		}
		query.eq("del_flag", 0);
		query.orderByDesc("id");
		Page<Station> page = new Page<>(input.getPage() == null ? 1 : input.getPage(),
				input.getLimit() == null ? 10 : input.getLimit());
		Page<Station> stationPage = stationMapper.selectPage(page, query);
		ResultPage<StationSimpleOutput> outputs = new ResultPage<>(stationPage);
		List<StationSimpleOutput> list = stationPage.getRecords().stream()
				.map(StationSimpleOutput::entityToOutput)
				.collect(Collectors.toList());
		Map<Integer, SysUserSimpleOutput> maintainMap = new HashMap<>();
		for (StationSimpleOutput output : list) {
			if (output.getMaintainPerson() != null) {
				if (!maintainMap.containsKey(output.getMaintainPerson())) {
					SysUser user = userMapper.getSimpleById(output.getMaintainPerson());
					maintainMap.put(output.getMaintainPerson(),
							user == null ? null : SysUserSimpleOutput.entityToOutput(user));
				}
				output.setMaintainPersonObj(maintainMap.get(output.getMaintainPerson()));
			}
		}
		outputs.setList(list);
		return outputs;
	}

	/**
	 * 电站信息详情（含经纬度，供地图展示）
	 */
	@Override
	public StationOutput detail(Integer id) {
		Station station = stationMapper.selectById(id);
		if (station == null) {
			throw new RuntimeException("电站信息不存在或已删除");
		}
		StationOutput output = StationOutput.entityToOutput(station);
		if (output.getMaintainPerson() != null) {
			SysUser user = userMapper.getSimpleById(output.getMaintainPerson());
			output.setMaintainPersonObj(user == null ? null : SysUserSimpleOutput.entityToOutput(user));
		}
		if (output.getStationPerson() != null) {
			SysUser user = userMapper.getSimpleById(output.getStationPerson());
			output.setStationPersonObj(user == null ? null : SysUserSimpleOutput.entityToOutput(user));
		}
		if (output.getProvinceId() != null) {
			Province province = provinceMapper.selectById(output.getProvinceId());
			output.setProvince(province == null ? null : ProvinceOutput.entityToOutput(province));
		}
		if (output.getCityId() != null) {
			City city = cityMapper.selectById(output.getCityId());
			output.setCity(city == null ? null : CityOutput.entityToOutput(city));
		}
		if (output.getAreaId() != null) {
			Area area = areaMapper.selectById(output.getAreaId());
			output.setArea(area == null ? null : AreaOutput.entityToOutput(area));
		}
		return output;
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		int i = stationMapper.deleteById(id);
		if (i != 1) {
			throw new RuntimeException("电站信息删除失败");
		}
	}
}
