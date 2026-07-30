package qrsoft.information.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.aspect.SysLog;
import qrsoft.information.dto.input.DataAmmeterInput;
import qrsoft.information.dto.output.*;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.service.IStationScreenService;

import java.util.List;

/**
 * @author Created by Young on 2021/4/25
 */
@RestController
@RequestMapping("/screen")
@Api(tags = "电站大屏相关操作")
public class StationScreenController {

	@Autowired
	private IStationScreenService stationScreenService;

	@GetMapping("/weather")
	@SysLog(action = "天气信息")
	@ApiOperation(value = "天气信息")
	public WrappedResult<WeatherOutput> weather() {
		WeatherOutput output = stationScreenService.latestWeather();
		return WrappedResult.successWrappedResult(output);
	}

	@GetMapping("/stationLastThirtyDayPower/{id}")
	@SysLog(action = "近30天发电效率")
	@ApiOperation(value = "近30天发电效率")
	public WrappedResult<List<StationPowerAndRadioOutput>> stationLastThirtyDayPower(
			@ApiParam(value = "电站id", required = true, example = "0") @PathVariable Integer id) {
		List<StationPowerAndRadioOutput> list = stationScreenService.stationLastThirtyDayPower(id);
		return WrappedResult.successWrappedResult(list);

	}

	@GetMapping("/stationDayAndYearPower/{id}")
	@SysLog(action = "今日发电量和今年发电量，节能指标")
	@ApiOperation(value = "今日发电量和今年发电量，节能指标")
	public WrappedResult<StationDayAndYearPowerOutput> stationDayAndYearPower(
			@ApiParam(value = "电站id", required = true, example = "0") @PathVariable Integer id) {
		StationDayAndYearPowerOutput stationDayAndYearPowerOutput = stationScreenService.stationDayAndYearPower(id);
		return WrappedResult.successWrappedResult(stationDayAndYearPowerOutput);
	}

	@GetMapping("/stationMonthKWhStatistic/{id}")
	@SysLog(action = "过去12个月发电量")
	@ApiOperation(value = "过去12个月发电量")
	public WrappedResult<List<StationPowerMonthOutput>> stationMonthKWhStatistic(
			@ApiParam(value = "电站id", required = true, example = "0") @PathVariable Integer id) {
		List<StationPowerMonthOutput> stationPowerAndRadioOutput = stationScreenService.kWhStatisticByMonth(id);
		return WrappedResult.successWrappedResult(stationPowerAndRadioOutput);
	}

	@GetMapping("/stationAllAndAverage")
	@SysLog(action = "电站综合及平均发电相关统计")
	@ApiOperation(value = "电站综合及平均发电相关统计")
	public WrappedResult<StationAllAndAverOutput> stationAllAndAverage() {
		StationAllAndAverOutput output = stationScreenService.stationAllAndAverage();
		return WrappedResult.successWrappedResult(output);
	}

	@GetMapping("/stationMonthPower")
	@SysLog(action = "电站本月发电量和本月发电效率")
	@ApiOperation(value = "电站本月发电量和本月发电效率")
	public WrappedResult<StationMonthPowerOutput> stationMonthPower() {
		StationMonthPowerOutput output = stationScreenService.stationMonthPower();
		return WrappedResult.successWrappedResult(output);
	}

	@GetMapping("/stationTypePower")
	@SysLog(action = "各类电站分布数和日发电效率")
	@ApiOperation(value = "各类电站分布数和日发电效率")
	public WrappedResult<StationTypePowerOutput> stationTypePower() {
		StationTypePowerOutput output = stationScreenService.stationTypePower();
		return WrappedResult.successWrappedResult(output);
	}

	@GetMapping("/stationNextThirtyDayPower/{id}")
	@SysLog(action = "预测未来30天发电量")
	@ApiOperation(value = "预测未来30天发电量")
	public WrappedResult<List<StationPowerAndRadioOutput>> stationNextThirtyDayPower(
			@ApiParam(value = "电站id", required = true, example = "0") @PathVariable Integer id) {
		List<StationPowerAndRadioOutput> outputs = stationScreenService.stationNextThirtyDayPower(id);
		return WrappedResult.successWrappedResult(outputs);
	}

	@GetMapping("/stationFaultCount/{id}")
	@SysLog(action = "电站设备故障统计")
	@ApiOperation(value = "电站设备故障统计")
	public WrappedResult<List<FaultCountOutput>> stationFaultCount(
			@ApiParam(value = "电站id", required = true, example = "0") @PathVariable Integer id) {
		List<FaultCountOutput> outputs = stationScreenService.stationFaultCount(id);
		return WrappedResult.successWrappedResult(outputs);
	}

	@PostMapping("/ammeter")
	@SysLog(action = "电表读数")
	@ApiOperation(value = "电表读数")
	public WrappedResult<List<DataAmmeterOutput>> ammeter(@RequestBody DataAmmeterInput input) {
		List<DataAmmeterOutput> outputs = stationScreenService.ammeter(input);
		return WrappedResult.successWrappedResult(outputs);
	}

}
