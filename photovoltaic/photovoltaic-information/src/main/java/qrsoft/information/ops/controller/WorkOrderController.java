package qrsoft.information.ops.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.shared.aspect.SysLog;
import qrsoft.information.dto.input.HandleOrderInput;
import qrsoft.information.dto.input.WorkOrderInput;
import qrsoft.information.dto.output.WorkOrderOutput;
import qrsoft.information.dto.page.WorkOrderPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.ops.service.IWorkOrderService;

@RestController
@RequestMapping("/workerOrder")
@Api(tags = "故障工单管理")
public class WorkOrderController {

	@Autowired
	private IWorkOrderService workOrderService;

	@PostMapping("/pageByParam")
	@SysLog(action = "故障工单分页查询")
	@ApiOperation(value = "故障工单分页查询")
	public WrappedResult<ResultPage<WorkOrderOutput>> pageByParam(@RequestBody WorkOrderPage input) {
		return WrappedResult.successWrappedResult(workOrderService.pageByParam(input));
	}

	@GetMapping("/detail/{id}")
	@SysLog(action = "获取工单详情")
	@ApiOperation(value = "获取工单详情")
	public WrappedResult<WorkOrderOutput> detail(@ApiParam(value = "工单id", required = true) @PathVariable Integer id) {
		return WrappedResult.successWrappedResult(workOrderService.detail(id));
	}

	@PostMapping("/save")
	@SysLog(action = "故障工单保存")
	@ApiOperation(value = "故障工单保存")
	public WrappedResult<Boolean> saveOrUpdate(@RequestBody WorkOrderInput input) {
		workOrderService.saveOrUpdate(input);
		return WrappedResult.successWrappedResult(true);
	}

	@GetMapping("/delete/{id}")
	@SysLog(action = "删除工单")
	@ApiOperation(value = "删除工单")
	public WrappedResult<Boolean> delete(@PathVariable Integer id) {
		workOrderService.delete(id);
		return WrappedResult.successWrappedResult(true);
	}

	@PostMapping("/handle")
	@SysLog(action = "故障工单处理")
	@ApiOperation(value = "故障工单处理")
	public WrappedResult<Boolean> handleOrder(@RequestBody HandleOrderInput input) {
		workOrderService.handleOrder(input);
		return WrappedResult.successWrappedResult(true);
	}
}
