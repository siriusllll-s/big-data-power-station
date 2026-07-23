package qrsoft.information.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.aspect.SysLog;
import qrsoft.information.dto.input.DataAmmeterInput;
import qrsoft.information.dto.output.StationAllAndAverOutput;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.service.IStationScreenService;
@RestController
@RequestMapping("/screen")
@Api(tags = "电站大屏相关操作")
public class StationScreenController {
	@Autowired
	private IStationScreenService stationScreenService;
	@GetMapping("/weather")
	@SysLog(action = "天气信息")
	@ApiOperation(value = "天气信息")
	public WrappedResult weather() {
		return WrappedResult.successWrappedResult(stationScreenService.latestWeather());
	}
	@GetMapping("/stationLastThirtyDayPower/{id}")
	@SysLog(action = "近30天发电效率")
	@ApiOperation(value = "近30天发电效率")
	public WrappedResult stationLastThirtyDayPower(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(stationScreenService.stationLastThirtyDayPower(id));
	}
	@GetMapping("/stationDayAndYearPower/{id}")
	@SysLog(action = "今日发电量和今年发电量，节能指标")
	@ApiOperation(value = "今日发电量和今年发电量，节能指标")
	public WrappedResult stationDayAndYearPower(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(stationScreenService.stationDayAndYearPower(id));
	}
	@GetMapping("/stationMonthKWhStatistic/{id}")
	@SysLog(action = "过去12个月发电量")
	@ApiOperation(value = "过去12个月发电量")
	public WrappedResult stationMonthKWhStatistic(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(stationScreenService.kWhStatisticByMonth(id));
	}
	@GetMapping("/stationAllAndAverage")
	@SysLog(action = "电站综合及平均发电相关统计")
	@ApiOperation(value = "电站综合及平均发电相关统计")
	public WrappedResult stationAllAndAverage() {
		StationAllAndAverOutput output = stationScreenService.stationAllAndAverage();
		return WrappedResult.successWrappedResult(output);
	}
	@GetMapping("/stationMonthPower")
	@SysLog(action = "电站本月发电量和本月发电效率")
	@ApiOperation(value = "电站本月发电量和本月发电效率")
	public WrappedResult stationMonthPower() {
		return WrappedResult.successWrappedResult(stationScreenService.stationMonthPower());
	}
	@GetMapping("/stationTypePower")
	@SysLog(action = "各类电站分布数和日发电效率")
	@ApiOperation(value = "各类电站分布数和日发电效率")
	public WrappedResult stationTypePower() {
		return WrappedResult.successWrappedResult(stationScreenService.stationTypePower());
	}
	@GetMapping("/stationNextThirtyDayPower/{id}")
	@SysLog(action = "预测未来30天发电量")
	@ApiOperation(value = "预测未来30天发电量")
	public WrappedResult stationNextThirtyDayPower(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(stationScreenService.stationNextThirtyDayPower(id));
	}
	@GetMapping("/stationFaultCount/{id}")
	@SysLog(action = "电站设备故障统计")
	@ApiOperation(value = "电站设备故障统计")
	public WrappedResult stationFaultCount(@PathVariable Integer id) {
		return WrappedResult.successWrappedResult(stationScreenService.stationFaultCount(id));
	}
	@PostMapping("/ammeter")
	@SysLog(action = "电表读数")
	@ApiOperation(value = "电表读数")
	public WrappedResult ammeter(@RequestBody DataAmmeterInput input) {
		return WrappedResult.successWrappedResult(stationScreenService.ammeter(input));
	}
}
