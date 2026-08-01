package qrsoft.information.station.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.shared.aspect.SysLog;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.dto.input.StationContractInput;
import qrsoft.information.dto.output.StationContractOutput;
import qrsoft.information.dto.page.StationContractPage;
import qrsoft.information.station.service.IStationContractService;

@RestController
@RequestMapping("/stationContract")
@Api(tags = "电站合同相关操作")
public class StationContractController {

	@Autowired
	private IStationContractService contractService;

	@PostMapping("/save")
	@SysLog(action = "电站合同信息保存/修改")
	@ApiOperation(value = "电站合同信息保存/修改")
	public WrappedResult<Boolean> saveOrUpdate(@Validated @RequestBody StationContractInput input) {
		contractService.saveOrUpdate(input);
		return WrappedResult.successWrappedResult(true);
	}

	@PostMapping("/pageByParam")
	@SysLog(action = "电站合同信息分页查询")
	@ApiOperation(value = "电站合同信息分页查询")
	public WrappedResult<ResultPage<StationContractOutput>> pageByParam(@RequestBody StationContractPage input) {
		ResultPage<StationContractOutput> outputs = contractService.pageByParam(input);
		return WrappedResult.successWrappedResult(outputs);
	}

	@GetMapping("/detail/{id}")
	@SysLog(action = "电站合同信息详情")
	@ApiOperation(value = "电站合同信息详情")
	public WrappedResult<StationContractOutput> detail(@ApiParam(value = "电站合同id", required = true, example = "0") @PathVariable Integer id) {
		StationContractOutput output = contractService.detail(id);
		return WrappedResult.successWrappedResult(output);
	}

	@GetMapping("/delete/{id}")
	@SysLog(action = "电站合同信息删除")
	@ApiOperation(value = "电站合同信息删除")
	public WrappedResult<Boolean> delete(@ApiParam(value = "电站合同id", required = true, example = "0") @PathVariable Integer id) {
		contractService.delete(id);
		return WrappedResult.successWrappedResult(true);
	}
}