package qrsoft.information.ops.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qrsoft.common.entity.InspectionContent;
import qrsoft.common.entity.InspectionItem;
import qrsoft.common.entity.InspectionManage;
import qrsoft.common.entity.InspectionProject;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.output.InspectionItemDetailOutput;
import qrsoft.information.dto.output.InspectionItemOutput;
import qrsoft.information.dto.output.InspectionManageOutput;
import qrsoft.information.dto.output.InspectionProjectOutput;
import qrsoft.information.mapper.InspectionContentMapper;
import qrsoft.information.mapper.InspectionItemMapper;
import qrsoft.information.mapper.InspectionManageMapper;
import qrsoft.information.mapper.InspectionProjectMapper;
import qrsoft.information.ops.service.InspectionService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InspectionServiceImpl implements InspectionService {

	@Autowired private InspectionProjectMapper projectMapper;
	@Autowired private InspectionContentMapper contentMapper;
	@Autowired private InspectionItemMapper itemMapper;
	@Autowired private InspectionManageMapper manageMapper;

	@Override
	public List<InspectionProjectOutput> getProjectList() {
		List<InspectionProject> list = projectMapper.selectList(new QueryWrapper<InspectionProject>().eq("del_flag", 0));
		return list.stream().map(InspectionProjectOutput::of).collect(Collectors.toList());
	}

	@Override
	public List<InspectionItemDetailOutput> getItemListByProject(Integer projectId) {
		List<InspectionContent> contents = contentMapper.selectList(new QueryWrapper<InspectionContent>()
				.eq("project_id", projectId).eq("del_flag", 0));
		List<InspectionItemDetailOutput> result = new ArrayList<>();
		for (InspectionContent c : contents) {
			InspectionItemDetailOutput d = new InspectionItemDetailOutput();
			d.setContentId(c.getId());
			d.setContentName(c.getName());
			List<InspectionItem> items = itemMapper.selectList(new QueryWrapper<InspectionItem>()
					.eq("content_id", c.getId()).eq("del_flag", 0));
			List<InspectionItemOutput> outs = new ArrayList<>();
			for (InspectionItem it : items) {
				InspectionItemOutput o = new InspectionItemOutput();
				o.setId(it.getId());
				o.setName(it.getName());
				o.setContentId(it.getContentId());
				outs.add(o);
			}
			d.setItems(outs);
			result.add(d);
		}
		return result;
	}

	@Override
	public Map<String, List<InspectionManageOutput>> getManageList() {
		List<InspectionManage> list = manageMapper.selectList(new QueryWrapper<InspectionManage>().orderByAsc("plan_date"));
		Map<String, List<InspectionManageOutput>> map = new LinkedHashMap<>();
		for (InspectionManage m : list) {
			String day = m.getPlanDate() == null ? "" : DateUtil.dateToString(m.getPlanDate(), DateUtil.YYMMDD);
			InspectionManageOutput o = new InspectionManageOutput();
			o.setId(m.getId());
			o.setName(m.getName());
			o.setStatus(m.getStatus());
			o.setPlanId(m.getPlanId());
			o.setPointId(m.getPointId());
			map.computeIfAbsent(day, k -> new ArrayList<>()).add(o);
		}
		return map;
	}
}
