package qrsoft.information.ops.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.shared.aspect.SysLog;
import qrsoft.information.dto.input.InspectionPointInput;
import qrsoft.information.dto.output.InspectionPointDetailOutput;
import qrsoft.information.dto.output.InspectionPointOutput;
import qrsoft.information.dto.page.InspectionPointPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.ops.service.InspectionPointService;

import java.util.List;

@RestController
@RequestMapping("/inspectionPoint")
@Api(tags = "巡检点管理")
public class InspectionPointController {

	@Autowired
	private InspectionPointService pointService;

	@PostMapping("/point/pageByParam")
	@SysLog(action = "巡检点分页查询")
	@ApiOperation(value = "巡检点分页查询")
	public WrappedResult<ResultPage<InspectionPointOutput>> pagePointByParam(@RequestBody InspectionPointPage input) {
		return WrappedResult.successWrappedResult(pointService.pagePointByParam(input));
	}

	@GetMapping("/pointList")
	@SysLog(action = "获取巡检点列表")
	@ApiOperation(value = "获取巡检点列表")
	public WrappedResult<List<InspectionPointOutput>> getPointList() {
		return WrappedResult.successWrappedResult(pointService.getPointList());
	}

	@GetMapping("/point/detail/{id}")
	@SysLog(action = "获取巡检点详情")
	@ApiOperation(value = "获取巡检点详情")
	public WrappedResult<InspectionPointInput> detailPoint(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(pointService.detailPoint(id));
	}

	@GetMapping("/point/view/{id}")
	@SysLog(action = "巡检点查看")
	@ApiOperation(value = "巡检点查看")
	public WrappedResult<InspectionPointDetailOutput> viewPoint(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(pointService.viewPoint(id));
	}

	@PostMapping("/point/save")
	@SysLog(action = "巡检点保存")
	@ApiOperation(value = "巡检点保存")
	public WrappedResult<Boolean> saveOrUpdatePoint(@RequestBody InspectionPointInput input) {
		pointService.saveOrUpdatePoint(input);
		return WrappedResult.successWrappedResult(true);
	}

	@GetMapping("/point/delete/{id}")
	@SysLog(action = "删除巡检点")
	@ApiOperation(value = "删除巡检点")
	public WrappedResult<Boolean> deletePoint(@ApiParam(value = "巡检点id") @PathVariable Integer id) {
		pointService.deletePoint(id);
		return WrappedResult.successWrappedResult(true);
	}
}
