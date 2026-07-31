package qrsoft.information.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qrsoft.common.constant.BaseConstant;
import qrsoft.common.entity.WorkOrder;
import qrsoft.common.entity.WorkOrderDevice;
import qrsoft.common.entity.WorkOrderHistory;
import qrsoft.common.entity.WorkOrderUser;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.input.HandleOrderInput;
import qrsoft.information.dto.input.WorkOrderInput;
import qrsoft.information.dto.output.WorkOrderHistoryOutput;
import qrsoft.information.dto.output.WorkOrderOutput;
import qrsoft.information.dto.page.WorkOrderPage;
import qrsoft.information.dto.vo.ResultPage;
import qrsoft.information.mapper.WorkOrderDeviceMapper;
import qrsoft.information.mapper.WorkOrderHistoryMapper;
import qrsoft.information.mapper.WorkOrderMapper;
import qrsoft.information.mapper.WorkOrderUserMapper;
import qrsoft.information.service.IWorkOrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkOrderServiceImpl implements IWorkOrderService {

	@Autowired private WorkOrderMapper workOrderMapper;
	@Autowired private WorkOrderDeviceMapper deviceMapper;
	@Autowired private WorkOrderUserMapper userMapper;
	@Autowired private WorkOrderHistoryMapper historyMapper;

	@Override
	public ResultPage<WorkOrderOutput> pageByParam(WorkOrderPage input) {
		if (input == null) input = new WorkOrderPage();
		QueryWrapper<WorkOrder> q = new QueryWrapper<>();
		q.eq("del_flag", 0);
		if (input.statusInt() != null) q.eq("status", input.statusInt());
		if (input.typeInt() != null) q.eq("type", input.typeInt());
		if (input.deviceTypeInt() != null) q.eq("device_type", input.deviceTypeInt());
		try {
			if (StringUtils.isNotBlank(input.getBeginDate())) {
				q.ge("exception_time", input.getBeginDate().substring(0, 10) + " 00:00:00");
			}
			if (StringUtils.isNotBlank(input.getEndDate())) {
				q.le("exception_time", input.getEndDate().substring(0, 10) + " 23:59:59");
			}
		} catch (Exception ignored) {}
		if (StringUtils.isNotBlank(input.getUserName())) {
			q.exists("select 1 from work_order_user u where u.order_id = work_order.id and u.user_name like '%"
					+ input.getUserName().replace("'", "") + "%'");
		}
		if (StringUtils.isNotBlank(input.getDeviceName())) {
			q.exists("select 1 from work_order_device d where d.order_id = work_order.id and d.device_name like '%"
					+ input.getDeviceName().replace("'", "") + "%'");
		}
		q.orderByDesc("id");
		int pageNo = input.getPage() == null || input.getPage() < 1 ? 1 : input.getPage();
		int limit = input.getLimit() == null || input.getLimit() < 1 ? 10 : input.getLimit();
		Page<WorkOrder> page = workOrderMapper.selectPage(new Page<>(pageNo, limit), q);
		ResultPage<WorkOrderOutput> result = new ResultPage<>(page);
		List<WorkOrderOutput> list = new ArrayList<>();
		for (WorkOrder w : page.getRecords()) {
			list.add(toOutput(w, false));
		}
		result.setList(list);
		return result;
	}

	@Override
	public WorkOrderOutput detail(Integer id) {
		return toOutput(require(id), true);
	}

	@Override
	@Transactional
	public void saveOrUpdate(WorkOrderInput input) {
		if (input == null || StringUtils.isBlank(input.getTitle())) {
			throw new RuntimeException("工单标题不能为空");
		}
		WorkOrder w = input.getId() == null ? new WorkOrder() : workOrderMapper.selectById(input.getId());
		if (input.getId() != null && w == null) throw new RuntimeException("工单不存在");
		w.setTitle(input.getTitle());
		w.setType(input.getType() == null ? 1 : input.getType());
		w.setDeviceType(input.getDeviceType());
		w.setDescription(input.getDescription());
		w.setStation(1);
		if (w.getStatus() == null) w.setStatus(BaseConstant.ORDER_STATUS_NEW);
		if (input.getStatus() != null) w.setStatus(input.getStatus());
		try {
			if (StringUtils.isNotBlank(input.getExceptionTime())) {
				String t = input.getExceptionTime();
				if (t.length() <= 10) {
					w.setExceptionTime(DateUtil.stringToDate(t.substring(0, 10), DateUtil.YYMMDD));
				} else {
					String norm = t.length() >= 19 ? t.substring(0, 19) : t;
					w.setExceptionTime(DateUtil.stringToDate(norm, DateUtil.YYMMDD_HHMMSS));
				}
			}
			if (StringUtils.isNotBlank(input.getForecastTime())) {
				String t = input.getForecastTime();
				if (t.length() <= 10) {
					w.setForecastTime(DateUtil.stringToDate(t.substring(0, 10), DateUtil.YYMMDD));
				} else {
					String norm = t.length() >= 19 ? t.substring(0, 19) : t;
					w.setForecastTime(DateUtil.stringToDate(norm, DateUtil.YYMMDD_HHMMSS));
				}
			}
		} catch (Exception e) {
			// keep null
		}
		Date now = new Date();
		w.setUpdateTime(now);
		if (w.getDelFlag() == null) w.setDelFlag(0);
		if (input.getId() == null) {
			w.setCreateTime(now);
			workOrderMapper.insert(w);
		} else {
			workOrderMapper.updateById(w);
			deviceMapper.delete(new QueryWrapper<WorkOrderDevice>().eq("order_id", w.getId()));
			userMapper.delete(new QueryWrapper<WorkOrderUser>().eq("order_id", w.getId()));
		}
		if (input.getDeviceNames() != null) {
			for (String dn : input.getDeviceNames()) {
				if (StringUtils.isBlank(dn)) continue;
				WorkOrderDevice d = new WorkOrderDevice();
				d.setOrderId(w.getId());
				d.setDeviceType(input.getDeviceType());
				d.setDeviceName(dn);
				deviceMapper.insert(d);
			}
		}
		List<String> users = input.getUserNames();
		if ((users == null || users.isEmpty()) && StringUtils.isNotBlank(input.getUserName())) {
			users = new ArrayList<>();
			users.add(input.getUserName());
		}
		if (users != null) {
			for (String un : users) {
				if (StringUtils.isBlank(un)) continue;
				WorkOrderUser u = new WorkOrderUser();
				u.setOrderId(w.getId());
				u.setUserName(un);
				userMapper.insert(u);
			}
		}
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		WorkOrder w = require(id);
		w.setDelFlag(1);
		w.setUpdateTime(new Date());
		workOrderMapper.updateById(w);
	}

	@Override
	@Transactional
	public void handleOrder(HandleOrderInput input) {
		if (input == null || input.getId() == null) throw new RuntimeException("参数错误");
		WorkOrder w = require(input.getId());
		if (input.getStatus() != null) {
			w.setStatus(input.getStatus());
		} else if (w.getStatus() != null && w.getStatus().equals(BaseConstant.ORDER_STATUS_NEW)) {
			w.setStatus(BaseConstant.ORDER_STATUS_PROCESSING);
		}
		w.setUpdateTime(new Date());
		workOrderMapper.updateById(w);
		WorkOrderHistory h = new WorkOrderHistory();
		h.setOrderId(w.getId());
		h.setStatus(w.getStatus());
		h.setHandleDesc(input.getHandleDesc());
		h.setHandleUser(StringUtils.isNotBlank(input.getHandleUser()) ? input.getHandleUser() : "admin");
		h.setHandleTime(new Date());
		historyMapper.insert(h);
	}

	private WorkOrder require(Integer id) {
		WorkOrder w = workOrderMapper.selectById(id);
		if (w == null || (w.getDelFlag() != null && w.getDelFlag() == 1)) {
			throw new RuntimeException("工单不存在");
		}
		return w;
	}

	private WorkOrderOutput toOutput(WorkOrder w, boolean withHistory) {
		WorkOrderOutput o = new WorkOrderOutput();
		o.setId(w.getId());
		o.setTitle(w.getTitle());
		o.setStatus(w.getStatus());
		o.setType(w.getType());
		o.setDeviceType(w.getDeviceType());
		o.setDescription(w.getDescription());
		if (w.getExceptionTime() != null) {
			o.setExceptionTime(DateUtil.dateToString(w.getExceptionTime(), DateUtil.YYMMDD_HHMMSS));
		}
		if (w.getForecastTime() != null) {
			o.setForecastTime(DateUtil.dateToString(w.getForecastTime(), DateUtil.YYMMDD_HHMMSS));
		}
		List<WorkOrderDevice> devices = deviceMapper.selectList(new QueryWrapper<WorkOrderDevice>().eq("order_id", w.getId()));
		o.setDeviceNames(devices.stream().map(WorkOrderDevice::getDeviceName).collect(Collectors.toList()));
		List<WorkOrderUser> users = userMapper.selectList(new QueryWrapper<WorkOrderUser>().eq("order_id", w.getId()));
		o.setUserNames(users.stream().map(WorkOrderUser::getUserName).collect(Collectors.toList()));
		if (withHistory) {
			List<WorkOrderHistory> hs = historyMapper.selectList(new QueryWrapper<WorkOrderHistory>()
					.eq("order_id", w.getId()).orderByDesc("id"));
			List<WorkOrderHistoryOutput> list = new ArrayList<>();
			for (WorkOrderHistory h : hs) {
				WorkOrderHistoryOutput ho = new WorkOrderHistoryOutput();
				ho.setStatus(h.getStatus());
				ho.setHandleDesc(h.getHandleDesc());
				ho.setHandleUser(h.getHandleUser());
				if (h.getHandleTime() != null) {
					ho.setHandleTime(DateUtil.dateToString(h.getHandleTime(), DateUtil.YYMMDD_HHMMSS));
				}
				list.add(ho);
			}
			o.setHistory(list);
		}
		return o;
	}
}
