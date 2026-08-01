package qrsoft.information.device.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.shared.aspect.SysLog;
import qrsoft.information.dto.input.DeviceFactoryInput;
import qrsoft.information.dto.output.DeviceFactoryOutput;
import qrsoft.information.dto.page.DeviceFactoryPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.device.service.IDeviceFactoryService;

import java.util.List;

@RestController
@RequestMapping("/factory")
@Api(tags = "设备厂商相关操作")
public class DeviceFactoryController {

	@Autowired
	private IDeviceFactoryService deviceFactoryService;

	@PostMapping("/save")
	@SysLog(action = "设备厂商保存")
	@ApiOperation("设备厂商保存")
	public WrappedResult<Boolean> saveOrUpdate(@RequestBody DeviceFactoryInput input) {
		deviceFactoryService.saveOrUpdate(input);
		return WrappedResult.successWrappedResult(true);
	}

	@PostMapping("/pageByParam")
	@SysLog(action = "设备厂商分页")
	@ApiOperation("设备厂商分页")
	public WrappedResult<ResultPage<DeviceFactoryOutput>> pageByParam(@RequestBody DeviceFactoryPage input) {
		return WrappedResult.successWrappedResult(deviceFactoryService.pageByParam(input));
	}

	@GetMapping("/delete/{id}")
	@SysLog(action = "删除厂商")
	@ApiOperation("删除厂商")
	public WrappedResult<Boolean> delete(@PathVariable Integer id) {
		deviceFactoryService.delete(id);
		return WrappedResult.successWrappedResult(true);
	}

	@GetMapping("/detail/{id}")
	@SysLog(action = "厂商详情")
	@ApiOperation("厂商详情")
	public WrappedResult<DeviceFactoryOutput> detail(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(deviceFactoryService.detail(id));
	}

	@GetMapping("/factoryList")
	@SysLog(action = "厂商列表")
	@ApiOperation("厂商列表")
	public WrappedResult<List<DeviceFactoryOutput>> factoryList() {
		return WrappedResult.successWrappedResult(deviceFactoryService.factoryList());
	}
}
