package qrsoft.information.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.common.constant.BaseConstant;
import qrsoft.information.aspect.SysLog;
import qrsoft.information.dto.input.DeviceInput;
import qrsoft.information.dto.output.DeviceOutPut;
import qrsoft.information.dto.page.DevicePage;
import qrsoft.information.dto.vo.ResultPage;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.service.IDeviceService;

import java.util.*;

@RestController
@RequestMapping("/device")
@Api(tags = "设备相关操作")
public class DeviceController {

	@Autowired
	private IDeviceService deviceService;

	@PostMapping("/pageByParam")
	@SysLog(action = "设备分页查询")
	@ApiOperation("设备分页查询")
	public WrappedResult<ResultPage<DeviceOutPut>> pageByParam(@RequestBody DevicePage input) {
		return WrappedResult.successWrappedResult(deviceService.pageByParam(input));
	}

	@PostMapping("/save")
	@SysLog(action = "设备保存")
	@ApiOperation("设备保存")
	public WrappedResult<Boolean> save(@RequestBody DeviceInput input) {
		deviceService.saveOrUpdate(input);
		return WrappedResult.successWrappedResult(true);
	}

	@GetMapping("/detail/{id}")
	@SysLog(action = "设备详情")
	@ApiOperation("设备详情")
	public WrappedResult<DeviceOutPut> detail(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(deviceService.detail(id));
	}

	@GetMapping("/delete/{id}")
	@SysLog(action = "设备删除")
	@ApiOperation("设备删除")
	public WrappedResult<Boolean> delete(@PathVariable Integer id) {
		deviceService.delete(id);
		return WrappedResult.successWrappedResult(true);
	}

	@GetMapping("/deviceByType/{type}")
	@SysLog(action = "按类型获取设备")
	@ApiOperation("按类型获取设备")
	public WrappedResult<List<DeviceOutPut>> getDeviceByType(@PathVariable Integer type) {
		return WrappedResult.successWrappedResult(deviceService.getDeviceByType(type));
	}

	@GetMapping("/deviceList")
	@SysLog(action = "设备列表")
	@ApiOperation("设备列表")
	public WrappedResult<List<DeviceOutPut>> deviceList() {
		return WrappedResult.successWrappedResult(deviceService.deviceList());
	}

	/**
	 * 兼容工单页 getDeviceListByType：优先查库，无数据则回退常量设备名
	 */
	@GetMapping("/listByType/{type}")
	@ApiOperation("按类型设备列表(兼容)")
	public WrappedResult<List<Map<String, Object>>> listByType(@PathVariable Integer type) {
		List<DeviceOutPut> fromDb = deviceService.getDeviceByType(type);
		List<Map<String, Object>> list = new ArrayList<>();
		if (fromDb != null && !fromDb.isEmpty()) {
			for (DeviceOutPut d : fromDb) {
				Map<String, Object> m = new HashMap<>();
				m.put("id", d.getName());
				m.put("name", d.getName());
				list.add(m);
			}
			return WrappedResult.successWrappedResult(list);
		}
		String[] names;
		if (type == null) names = new String[0];
		else if (type == BaseConstant.DEVICE_INVERTER) names = BaseConstant.inverterList;
		else if (type == BaseConstant.DEVICE_COMBINER_BOX) names = new String[]{"01号汇流箱", "02号汇流箱", "03号汇流箱", "04号汇流箱", "05号汇流箱"};
		else if (type == BaseConstant.DEVICE_DC_CABINET) names = BaseConstant.dcCabinetList;
		else if (type == BaseConstant.DEVICE_WEATHER) names = new String[]{"气象仪"};
		else if (type == BaseConstant.DEVICE_AMMETER) names = BaseConstant.ammeterList;
		else names = new String[0];
		for (String n : names) {
			Map<String, Object> m = new HashMap<>();
			m.put("id", n);
			m.put("name", n);
			list.add(m);
		}
		return WrappedResult.successWrappedResult(list);
	}
}
