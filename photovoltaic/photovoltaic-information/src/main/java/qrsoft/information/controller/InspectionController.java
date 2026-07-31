package qrsoft.information.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qrsoft.information.aspect.SysLog;
import qrsoft.information.dto.output.InspectionItemDetailOutput;
import qrsoft.information.dto.output.InspectionManageOutput;
import qrsoft.information.dto.output.InspectionProjectOutput;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.service.InspectionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inspection")
@Api(tags = "巡检管理")
public class InspectionController {

	@Autowired
	private InspectionService inspectionService;

	@GetMapping("/projectList")
	@SysLog(action = "获取巡检项目列表")
	@ApiOperation(value = "获取巡检项目列表")
	public WrappedResult<List<InspectionProjectOutput>> projectList() {
		return WrappedResult.successWrappedResult(inspectionService.getProjectList());
	}

	@GetMapping("/itemList/{projectId}")
	@SysLog(action = "根据巡检项目获取巡检内容及事项")
	@ApiOperation(value = "根据巡检项目获取巡检内容及事项")
	public WrappedResult<List<InspectionItemDetailOutput>> getItemListByProject(
			@ApiParam(value = "巡检项目id", required = true) @PathVariable Integer projectId) {
		return WrappedResult.successWrappedResult(inspectionService.getItemListByProject(projectId));
	}

	@GetMapping("/manageList")
	@SysLog(action = "获取巡检进度列表")
	@ApiOperation(value = "获取巡检进度列表")
	public WrappedResult<Map<String, List<InspectionManageOutput>>> getManageList() {
		return WrappedResult.successWrappedResult(inspectionService.getManageList());
	}
}
