package qrsoft.information.ops.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qrsoft.common.entity.*;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.input.InspectionPlanInput;
import qrsoft.information.dto.output.InspectionPlanDetailOutput;
import qrsoft.information.dto.output.InspectionPlanOutput;
import qrsoft.information.dto.page.InspectionPlanPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.mapper.*;
import qrsoft.information.ops.service.InspectionPlanService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class InspectionPlanServiceImpl implements InspectionPlanService {

	@Autowired private InspectionPlanMapper planMapper;
	@Autowired private InspectionPlanPointMapper planPointMapper;
	@Autowired private InspectionPlanUserMapper planUserMapper;
	@Autowired private InspectionPointMapper pointMapper;
	@Autowired private InspectionManageMapper manageMapper;
	@Autowired private SysUserMapper userMapper;

	@Override
	public ResultPage<InspectionPlanOutput> pagePlanByParam(InspectionPlanPage input) {
		if (input == null) input = new InspectionPlanPage();
		QueryWrapper<InspectionPlan> q = new QueryWrapper<>();
		q.eq("del_flag", 0);
		if (StringUtils.isNotBlank(input.getPlanName())) {
			q.like("name", input.getPlanName());
		}
		try {
			if (StringUtils.isNotBlank(input.getBeginDate())) {
				q.ge("begin_date", input.getBeginDate().substring(0, 10));
			}
			if (StringUtils.isNotBlank(input.getEndDate())) {
				q.le("end_date", input.getEndDate().substring(0, 10));
			}
		} catch (Exception e) {
			throw new RuntimeException("日期格式错误");
		}
		q.orderByDesc("id");
		int pageNo = input.getPage() == null || input.getPage() < 1 ? 1 : input.getPage();
		int limit = input.getLimit() == null || input.getLimit() < 1 ? 10 : input.getLimit();
		Page<InspectionPlan> page = planMapper.selectPage(new Page<>(pageNo, limit), q);
		ResultPage<InspectionPlanOutput> result = new ResultPage<>(page);
		List<InspectionPlanOutput> list = new ArrayList<>();
		for (InspectionPlan p : page.getRecords()) {
			list.add(toOutput(p));
		}
		result.setList(list);
		return result;
	}

	@Override
	public InspectionPlanInput detailPlan(Integer id) {
		InspectionPlan p = require(id);
		InspectionPlanInput in = new InspectionPlanInput();
		in.setId(p.getId());
		in.setName(p.getName());
		in.setMemo(p.getMemo());
		if (p.getBeginDate() != null) in.setBeginDate(DateUtil.dateToString(p.getBeginDate(), DateUtil.YYMMDD));
		if (p.getEndDate() != null) in.setEndDate(DateUtil.dateToString(p.getEndDate(), DateUtil.YYMMDD));
		List<InspectionPlanPoint> pps = planPointMapper.selectList(new QueryWrapper<InspectionPlanPoint>().eq("plan_id", id));
		List<Integer> pointIds = new ArrayList<>();
		for (InspectionPlanPoint pp : pps) pointIds.add(pp.getPointId());
		in.setPointIds(pointIds);
		List<InspectionPlanUser> pus = planUserMapper.selectList(new QueryWrapper<InspectionPlanUser>().eq("plan_id", id));
		List<Integer> userIds = new ArrayList<>();
		for (InspectionPlanUser pu : pus) userIds.add(pu.getUserId());
		in.setUserIds(userIds);
		return in;
	}

	@Override
	public InspectionPlanDetailOutput viewPlan(Integer id) {
		InspectionPlan p = require(id);
		InspectionPlanDetailOutput o = new InspectionPlanDetailOutput();
		o.setId(p.getId());
		o.setName(p.getName());
		o.setMemo(p.getMemo());
		if (p.getBeginDate() != null) o.setBeginDate(DateUtil.dateToString(p.getBeginDate(), DateUtil.YYMMDD));
		if (p.getEndDate() != null) o.setEndDate(DateUtil.dateToString(p.getEndDate(), DateUtil.YYMMDD));
		List<InspectionPlanPoint> pps = planPointMapper.selectList(new QueryWrapper<InspectionPlanPoint>().eq("plan_id", id));
		List<String> pointNames = new ArrayList<>();
		for (InspectionPlanPoint pp : pps) {
			InspectionPoint pt = pointMapper.selectById(pp.getPointId());
			if (pt != null) pointNames.add(pt.getName());
		}
		o.setPointNames(pointNames);
		List<InspectionPlanUser> pus = planUserMapper.selectList(new QueryWrapper<InspectionPlanUser>().eq("plan_id", id));
		List<String> userNames = new ArrayList<>();
		for (InspectionPlanUser pu : pus) {
			SysUser u = userMapper.selectById(pu.getUserId());
			if (u != null) userNames.add(u.getTrueName() != null ? u.getTrueName() : u.getName());
			else userNames.add("用户" + pu.getUserId());
		}
		o.setUserNames(userNames);
		return o;
	}

	@Override
	@Transactional
	public void saveOrUpdatePlan(InspectionPlanInput input) {
		if (input == null || StringUtils.isBlank(input.getName())) {
			throw new RuntimeException("计划名称不能为空");
		}
		InspectionPlan p = input.getId() == null ? new InspectionPlan() : planMapper.selectById(input.getId());
		if (input.getId() != null && p == null) throw new RuntimeException("巡检计划不存在");
		p.setName(input.getName());
		p.setMemo(input.getMemo());
		p.setStation(1);
		try {
			if (StringUtils.isNotBlank(input.getBeginDate())) {
				p.setBeginDate(DateUtil.stringToDate(input.getBeginDate().substring(0, 10), DateUtil.YYMMDD));
			}
			if (StringUtils.isNotBlank(input.getEndDate())) {
				p.setEndDate(DateUtil.stringToDate(input.getEndDate().substring(0, 10), DateUtil.YYMMDD));
			}
		} catch (Exception e) {
			throw new RuntimeException("日期格式错误");
		}
		if (p.getDelFlag() == null) p.setDelFlag(0);
		if (input.getId() == null) {
			planMapper.insert(p);
		} else {
			planMapper.updateById(p);
			planPointMapper.delete(new QueryWrapper<InspectionPlanPoint>().eq("plan_id", p.getId()));
			planUserMapper.delete(new QueryWrapper<InspectionPlanUser>().eq("plan_id", p.getId()));
			manageMapper.delete(new QueryWrapper<InspectionManage>().eq("plan_id", p.getId()));
		}
		if (input.getPointIds() != null) {
			for (Integer pointId : input.getPointIds()) {
				if (pointId == null) continue;
				InspectionPlanPoint pp = new InspectionPlanPoint();
				pp.setPlanId(p.getId());
				pp.setPointId(pointId);
				planPointMapper.insert(pp);
			}
		}
		if (input.getUserIds() != null) {
			for (Integer userId : input.getUserIds()) {
				if (userId == null) continue;
				InspectionPlanUser pu = new InspectionPlanUser();
				pu.setPlanId(p.getId());
				pu.setUserId(userId);
				planUserMapper.insert(pu);
			}
		}
		// 生成计划期内每日巡检进度（简化：每天 x 每个点）
		generateManage(p, input.getPointIds());
	}

	private void generateManage(InspectionPlan p, List<Integer> pointIds) {
		if (p.getBeginDate() == null || p.getEndDate() == null || pointIds == null || pointIds.isEmpty()) {
			return;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(p.getBeginDate());
		Date end = p.getEndDate();
		// 最多生成 60 天避免膨胀
		int guard = 0;
		while (!cal.getTime().after(end) && guard < 60) {
			Date day = cal.getTime();
			for (Integer pointId : pointIds) {
				InspectionPoint pt = pointMapper.selectById(pointId);
				InspectionManage m = new InspectionManage();
				m.setPlanId(p.getId());
				m.setPointId(pointId);
				m.setPlanDate(day);
				m.setStatus(0);
				m.setName(pt == null ? ("点" + pointId) : pt.getName());
				manageMapper.insert(m);
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
			guard++;
		}
	}

	@Override
	@Transactional
	public void deletePlan(Integer id) {
		InspectionPlan p = require(id);
		p.setDelFlag(1);
		planMapper.updateById(p);
	}

	private InspectionPlan require(Integer id) {
		InspectionPlan p = planMapper.selectById(id);
		if (p == null || (p.getDelFlag() != null && p.getDelFlag() == 1)) {
			throw new RuntimeException("巡检计划不存在");
		}
		return p;
	}

	private InspectionPlanOutput toOutput(InspectionPlan p) {
		InspectionPlanOutput o = new InspectionPlanOutput();
		o.setId(p.getId());
		o.setName(p.getName());
		o.setMemo(p.getMemo());
		if (p.getBeginDate() != null) o.setBeginDate(DateUtil.dateToString(p.getBeginDate(), DateUtil.YYMMDD));
		if (p.getEndDate() != null) o.setEndDate(DateUtil.dateToString(p.getEndDate(), DateUtil.YYMMDD));
		return o;
	}
}
