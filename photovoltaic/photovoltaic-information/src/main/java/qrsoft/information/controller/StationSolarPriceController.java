package qrsoft.information.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.aspect.SysLog;
import qrsoft.information.dto.vo.ResultPage;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.dto.input.StationSolarPriceInput;
import qrsoft.information.dto.output.StationSolarPriceOutput;
import qrsoft.information.dto.page.StationSolarPricePage;
import qrsoft.information.service.IStationSolarPriceService;

@RestController
@RequestMapping("/stationSolarPrice")
@Api(tags = "电站电价相关操作")
public class StationSolarPriceController {

	@Autowired
	private IStationSolarPriceService priceService;

	@PostMapping("/save")
	@SysLog(action = "电站电价保存/修改")
	@ApiOperation(value = "电站电价保存/修改")
	public WrappedResult<Boolean> saveOrUpdate(@RequestBody StationSolarPriceInput input) {
		priceService.saveOrUpdate(input);
		return WrappedResult.successWrappedResult(true);
	}

	@PostMapping("/pageByParam")
	@SysLog(action = "电站电价分页查询")
	@ApiOperation(value = "电站电价分页查询")
	public WrappedResult<ResultPage<StationSolarPriceOutput>> pageByParam(@RequestBody StationSolarPricePage input) {
		ResultPage<StationSolarPriceOutput> outputs = priceService.pageByParam(input);
		return WrappedResult.successWrappedResult(outputs);
	}

	@GetMapping("/detail/{id}")
	@SysLog(action = "电站电价详情")
	@ApiOperation(value = "电站电价详情")
	public WrappedResult<StationSolarPriceOutput> detail(@ApiParam(value = "电站电价id", required = true, example = "0") @PathVariable Integer id) {
		StationSolarPriceOutput output = priceService.detail(id);
		return WrappedResult.successWrappedResult(output);
	}

	@GetMapping("/delete/{id}")
	@SysLog(action = "电站电价删除")
	@ApiOperation(value = "电站电价删除")
	public WrappedResult<Boolean> delete(@ApiParam(value = "电站电价id", required = true, example = "0") @PathVariable Integer id) {
		priceService.delete(id);
		return WrappedResult.successWrappedResult(true);
	}
}
