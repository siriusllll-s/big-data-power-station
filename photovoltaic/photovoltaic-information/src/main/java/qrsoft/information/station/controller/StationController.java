package qrsoft.information.station.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.shared.aspect.SysLog;
import qrsoft.information.dto.input.StationInput;
import qrsoft.information.dto.input.StationPhotoInput;
import qrsoft.information.dto.output.StationOutput;
import qrsoft.information.dto.output.StationSimpleOutput;
import qrsoft.information.dto.page.StationPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.station.service.IStationService;

@RestController
@RequestMapping("/station")
@Api(tags = "电站相关操作")
public class StationController {

	@Autowired
	private IStationService stationService;

	@PostMapping("/save")
	@SysLog(action = "电站数据修改")
	@ApiOperation(value = "电站数据修改")
	public WrappedResult saveOrUpdate(@RequestBody StationInput input) {
		stationService.saveOrUpdate(input);
		return WrappedResult.successWrappedResult(true);
	}

	@PostMapping("/savePhoto")
	@SysLog(action = "电站图片修改")
	@ApiOperation(value = "电站图片修改")
	public WrappedResult savePhoto(@RequestBody StationPhotoInput input) {
		stationService.savePhoto(input);
		return WrappedResult.successWrappedResult(true);
	}

	@PostMapping("/pageByParam")
	@SysLog(action = "电站数据分页查询")
	@ApiOperation(value = "电站数据分页查询")
	public WrappedResult pageByParam(@RequestBody StationPage input) {
		ResultPage<StationSimpleOutput> outputs = stationService.pageByParam(input);
		return WrappedResult.successWrappedResult(outputs);
	}

	@GetMapping("/detail/{id}")
	@SysLog(action = "电站信息详情")
	@ApiOperation(value = "电站信息详情")
	public WrappedResult detail(
			@ApiParam(value = "电站id", required = true, example = "1") @PathVariable Integer id) {
		StationOutput output = stationService.detail(id);
		return WrappedResult.successWrappedResult(output);
	}

	@GetMapping("/delete/{id}")
	@SysLog(action = "电站信息删除")
	@ApiOperation(value = "电站信息删除")
	public WrappedResult delete(
			@ApiParam(value = "电站id", required = true, example = "1") @PathVariable Integer id) {
		stationService.delete(id);
		return WrappedResult.successWrappedResult(true);
	}
}
