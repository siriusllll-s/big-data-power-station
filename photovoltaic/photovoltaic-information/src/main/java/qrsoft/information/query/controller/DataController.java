package qrsoft.information.query.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.common.entity.Fault;
import qrsoft.common.entity.Station;
import qrsoft.common.util.DateUtil;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.mapper.FaultMapper;
import qrsoft.information.mapper.StationMapper;

import java.util.*;

/**
 * 数据检索：异常/故障分页
 */
@RestController
@RequestMapping("/data")
public class DataController {

	@Autowired private FaultMapper faultMapper;
	@Autowired private StationMapper stationMapper;

	@PostMapping("/exceptionPage")
	public WrappedResult<ResultPage<Map<String, Object>>> exceptionPage(@RequestBody Map<String, Object> body) {
		int pageNo = body.get("page") == null ? 1 : Integer.parseInt(String.valueOf(body.get("page")));
		int limit = body.get("limit") == null ? 10 : Integer.parseInt(String.valueOf(body.get("limit")));
		String start = body.get("start") == null ? null : String.valueOf(body.get("start"));
		String end = body.get("end") == null ? null : String.valueOf(body.get("end"));
		String deviceName = body.get("deviceName") == null ? null : String.valueOf(body.get("deviceName"));

		QueryWrapper<Fault> q = new QueryWrapper<>();
		if (StringUtils.isNotBlank(deviceName)) {
			q.like("device_name", deviceName);
		}
		try {
			if (StringUtils.isNotBlank(start)) {
				q.ge("fault_time", start.length() > 10 ? start : start + " 00:00:00");
			}
			if (StringUtils.isNotBlank(end)) {
				q.le("fault_time", end.length() > 10 ? end : end + " 23:59:59");
			}
		} catch (Exception ignored) {}
		q.orderByDesc("fault_time").orderByDesc("id");
		Page<Fault> page = faultMapper.selectPage(new Page<>(pageNo, limit), q);
		ResultPage<Map<String, Object>> result = new ResultPage<>(page);
		Map<Integer, String> stationNames = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		for (Fault f : page.getRecords()) {
			Map<String, Object> m = new HashMap<>();
			m.put("id", f.getId());
			m.put("deviceName", f.getDeviceName());
			m.put("deviceType", f.getDeviceType());
			m.put("faultDesc", f.getFaultDesc());
			m.put("faultLevel", f.getFaultLevel());
			m.put("faultTime", f.getFaultTime() == null ? null : DateUtil.dateToString(f.getFaultTime(), DateUtil.YYMMDD_HHMMSS));
			String sn = "电站";
			if (f.getStation() != null) {
				if (!stationNames.containsKey(f.getStation())) {
					Station s = stationMapper.selectById(f.getStation());
					stationNames.put(f.getStation(), s == null ? ("电站" + f.getStation()) : s.getName());
				}
				sn = stationNames.get(f.getStation());
			}
			m.put("name", sn);
			list.add(m);
		}
		result.setList(list);
		return WrappedResult.successWrappedResult(result);
	}
}
