package qrsoft.information.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qrsoft.common.constant.BaseConstant;
import qrsoft.information.dto.vo.WrappedResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备列表（实验：按类型返回固定设备名，供工单筛选）
 */
@RestController
@RequestMapping("/device")
@Api(tags = "设备")
public class DeviceController {

	@GetMapping("/listByType/{type}")
	@ApiOperation("按设备类型获取设备列表")
	public WrappedResult<List<Map<String, Object>>> listByType(@PathVariable Integer type) {
		String[] names;
		if (type == null) {
			names = new String[0];
		} else if (type == BaseConstant.DEVICE_INVERTER) {
			names = BaseConstant.inverterList;
		} else if (type == BaseConstant.DEVICE_COMBINER_BOX) {
			names = new String[]{"01号汇流箱", "02号汇流箱", "03号汇流箱", "04号汇流箱", "05号汇流箱"};
		} else if (type == BaseConstant.DEVICE_DC_CABINET) {
			names = BaseConstant.dcCabinetList;
		} else if (type == BaseConstant.DEVICE_WEATHER) {
			names = new String[]{"气象仪"};
		} else if (type == BaseConstant.DEVICE_AMMETER) {
			names = BaseConstant.ammeterList;
		} else {
			names = new String[0];
		}
		List<Map<String, Object>> list = new ArrayList<>();
		int i = 1;
		for (String n : names) {
			Map<String, Object> m = new HashMap<>();
			m.put("id", n);
			m.put("name", n);
			list.add(m);
			i++;
		}
		return WrappedResult.successWrappedResult(list);
	}
}
