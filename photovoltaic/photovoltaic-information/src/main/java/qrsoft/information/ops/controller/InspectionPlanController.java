package qrsoft.information.ops.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.shared.aspect.SysLog;
import qrsoft.information.dto.input.InspectionPlanInput;
import qrsoft.information.dto.output.InspectionPlanDetailOutput;
import qrsoft.information.dto.output.InspectionPlanOutput;
import qrsoft.information.dto.page.InspectionPlanPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.ops.service.InspectionPlanService;

@RestController
@RequestMapping("/inspectionPlan")
@Api(tags = "巡检计划管理")
public class InspectionPlanController {

	@Autowired
	private InspectionPlanService planService;

	@PostMapping("/pageByParam")
	@SysLog(action = "巡检计划分页查询")
	@ApiOperation(value = "巡检计划分页查询")
	public WrappedResult<ResultPage<InspectionPlanOutput>> pagePlanByParam(@RequestBody InspectionPlanPage input) {
		return WrappedResult.successWrappedResult(planService.pagePlanByParam(input));
	}

	@GetMapping("/detail/{id}")
	@SysLog(action = "获取巡检计划详情")
	@ApiOperation(value = "获取巡检计划详情")
	public WrappedResult<InspectionPlanInput> detailPlan(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(planService.detailPlan(id));
	}

	@GetMapping("/view/{id}")
	@SysLog(action = "查看巡检计划")
	@ApiOperation(value = "查看巡检计划")
	public WrappedResult<InspectionPlanDetailOutput> viewPlan(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(planService.viewPlan(id));
	}

	@PostMapping("/save")
	@SysLog(action = "巡检计划保存")
	@ApiOperation(value = "巡检计划保存")
	public WrappedResult<Boolean> saveOrUpdatePlan(@RequestBody InspectionPlanInput input) {
		planService.saveOrUpdatePlan(input);
		return WrappedResult.successWrappedResult(true);
	}

	@GetMapping("/delete/{id}")
	@SysLog(action = "删除巡检计划")
	@ApiOperation(value = "删除巡检计划")
	public WrappedResult<Boolean> deletePlan(@ApiParam(value = "巡检计划id") @PathVariable Integer id) {
		planService.deletePlan(id);
		return WrappedResult.successWrappedResult(true);
	}
}
