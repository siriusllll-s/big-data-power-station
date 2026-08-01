package qrsoft.information.ops.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qrsoft.common.entity.*;
import qrsoft.information.dto.input.InspectionPointInput;
import qrsoft.information.dto.output.InspectionPointDetailOutput;
import qrsoft.information.dto.output.InspectionPointOutput;
import qrsoft.information.dto.page.InspectionPointPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.mapper.*;
import qrsoft.information.ops.service.InspectionPointService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InspectionPointServiceImpl implements InspectionPointService {

	@Autowired private InspectionPointMapper pointMapper;
	@Autowired private InspectionProjectMapper projectMapper;
	@Autowired private InspectionPointItemMapper pointItemMapper;
	@Autowired private InspectionPointDeviceMapper pointDeviceMapper;
	@Autowired private InspectionItemMapper itemMapper;

	@Override
	public ResultPage<InspectionPointOutput> pagePointByParam(InspectionPointPage input) {
		if (input == null) input = new InspectionPointPage();
		QueryWrapper<InspectionPoint> q = new QueryWrapper<>();
		q.eq("del_flag", 0);
		if (input.projectId() != null) {
			q.eq("project_id", input.projectId());
		}
		if (StringUtils.isNotBlank(input.getPointName())) {
			q.like("name", input.getPointName());
		}
		q.orderByDesc("id");
		int pageNo = input.getPage() == null || input.getPage() < 1 ? 1 : input.getPage();
		int limit = input.getLimit() == null || input.getLimit() < 1 ? 10 : input.getLimit();
		Page<InspectionPoint> page = pointMapper.selectPage(new Page<>(pageNo, limit), q);
		ResultPage<InspectionPointOutput> result = new ResultPage<>(page);
		List<InspectionPointOutput> list = new ArrayList<>();
		for (InspectionPoint p : page.getRecords()) {
			list.add(toOutput(p));
		}
		result.setList(list);
		return result;
	}

	@Override
	public List<InspectionPointOutput> getPointList() {
		List<InspectionPoint> list = pointMapper.selectList(new QueryWrapper<InspectionPoint>().eq("del_flag", 0));
		return list.stream().map(this::toOutput).collect(Collectors.toList());
	}

	@Override
	public InspectionPointInput detailPoint(Integer id) {
		InspectionPoint p = pointMapper.selectById(id);
		if (p == null || (p.getDelFlag() != null && p.getDelFlag() == 1)) {
			throw new RuntimeException("巡检点不存在");
		}
		InspectionPointInput in = new InspectionPointInput();
		in.setId(p.getId());
		in.setName(p.getName());
		in.setProjectId(p.getProjectId());
		in.setProject(p.getProjectId());
		in.setMemo(p.getMemo());
		List<InspectionPointItem> items = pointItemMapper.selectList(new QueryWrapper<InspectionPointItem>().eq("point_id", id));
		in.setItemIds(items.stream().map(InspectionPointItem::getItemId).collect(Collectors.toList()));
		List<InspectionPointDevice> devices = pointDeviceMapper.selectList(new QueryWrapper<InspectionPointDevice>().eq("point_id", id));
		in.setDeviceNames(devices.stream().map(InspectionPointDevice::getDeviceName).collect(Collectors.toList()));
		if (!devices.isEmpty()) {
			in.setDeviceType(devices.get(0).getDeviceType());
		}
		return in;
	}

	@Override
	public InspectionPointDetailOutput viewPoint(Integer id) {
		InspectionPoint p = pointMapper.selectById(id);
		if (p == null || (p.getDelFlag() != null && p.getDelFlag() == 1)) {
			throw new RuntimeException("巡检点不存在");
		}
		InspectionPointDetailOutput o = new InspectionPointDetailOutput();
		o.setId(p.getId());
		o.setName(p.getName());
		o.setProjectId(p.getProjectId());
		o.setMemo(p.getMemo());
		if (p.getProjectId() != null) {
			InspectionProject proj = projectMapper.selectById(p.getProjectId());
			if (proj != null) o.setProjectName(proj.getName());
		}
		List<InspectionPointItem> items = pointItemMapper.selectList(new QueryWrapper<InspectionPointItem>().eq("point_id", id));
		List<String> itemNames = new ArrayList<>();
		for (InspectionPointItem pi : items) {
			InspectionItem it = itemMapper.selectById(pi.getItemId());
			if (it != null) itemNames.add(it.getName());
		}
		o.setItemNames(itemNames);
		List<InspectionPointDevice> devices = pointDeviceMapper.selectList(new QueryWrapper<InspectionPointDevice>().eq("point_id", id));
		o.setDeviceNames(devices.stream().map(InspectionPointDevice::getDeviceName).collect(Collectors.toList()));
		return o;
	}

	@Override
	@Transactional
	public void saveOrUpdatePoint(InspectionPointInput input) {
		if (input == null || StringUtils.isBlank(input.getName())) {
			throw new RuntimeException("巡检点名称不能为空");
		}
		InspectionPoint p = input.getId() == null ? new InspectionPoint() : pointMapper.selectById(input.getId());
		if (input.getId() != null && p == null) {
			throw new RuntimeException("巡检点不存在");
		}
		p.setName(input.getName());
		p.setProjectId(input.resolveProjectId());
		p.setMemo(input.getMemo());
		p.setStation(1);
		if (p.getDelFlag() == null) p.setDelFlag(0);
		if (input.getId() == null) {
			pointMapper.insert(p);
		} else {
			pointMapper.updateById(p);
			pointItemMapper.delete(new QueryWrapper<InspectionPointItem>().eq("point_id", p.getId()));
			pointDeviceMapper.delete(new QueryWrapper<InspectionPointDevice>().eq("point_id", p.getId()));
		}
		if (input.getItemIds() != null) {
			for (Integer itemId : input.getItemIds()) {
				if (itemId == null) continue;
				InspectionPointItem pi = new InspectionPointItem();
				pi.setPointId(p.getId());
				pi.setItemId(itemId);
				pointItemMapper.insert(pi);
			}
		}
		if (input.getDeviceNames() != null) {
			for (String dn : input.getDeviceNames()) {
				if (StringUtils.isBlank(dn)) continue;
				InspectionPointDevice d = new InspectionPointDevice();
				d.setPointId(p.getId());
				d.setDeviceType(input.getDeviceType());
				d.setDeviceName(dn);
				pointDeviceMapper.insert(d);
			}
		}
	}

	@Override
	@Transactional
	public void deletePoint(Integer id) {
		InspectionPoint p = pointMapper.selectById(id);
		if (p == null) throw new RuntimeException("巡检点不存在");
		p.setDelFlag(1);
		pointMapper.updateById(p);
	}

	private InspectionPointOutput toOutput(InspectionPoint p) {
		InspectionPointOutput o = new InspectionPointOutput();
		o.setId(p.getId());
		o.setName(p.getName());
		o.setProjectId(p.getProjectId());
		o.setMemo(p.getMemo());
		if (p.getProjectId() != null) {
			InspectionProject proj = projectMapper.selectById(p.getProjectId());
			if (proj != null) o.setProjectName(proj.getName());
		}
		return o;
	}
}
