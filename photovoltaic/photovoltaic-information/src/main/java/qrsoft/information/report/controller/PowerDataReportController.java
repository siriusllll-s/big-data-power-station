package qrsoft.information.report.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.shared.aspect.SysLog;
import qrsoft.information.dto.input.PowerDataReportInput;
import qrsoft.information.dto.output.PowerDataReportOutput;
import qrsoft.information.dto.page.PowerDataReportPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.report.service.IPowerDataReportService;

@RestController
@RequestMapping("/powerDataReport")
@Api(tags = "电站运行日报相关操作")
public class PowerDataReportController {

	@Autowired
	private IPowerDataReportService powerDataReportService;

	@PostMapping("/update")
	@SysLog(action = "电站运行日报修改")
	@ApiOperation(value = "电站运行日报修改")
	public WrappedResult<Boolean> update(@RequestBody PowerDataReportInput input) {
		powerDataReportService.update(input);
		return WrappedResult.successWrappedResult(true);
	}

	@PostMapping("/pageByParam")
	@SysLog(action = "电站运行日报分页查询")
	@ApiOperation(value = "电站运行日报分页查询")
	public WrappedResult<ResultPage<PowerDataReportOutput>> pageByParam(@RequestBody PowerDataReportPage input) {
		ResultPage<PowerDataReportOutput> output = powerDataReportService.pageByParam(input);
		return WrappedResult.successWrappedResult(output);
	}

	@GetMapping("/detail/{id}")
	@SysLog(action = "电站运行日报详情")
	@ApiOperation(value = "电站运行日报详情")
	public WrappedResult<PowerDataReportOutput> detail(
			@ApiParam(value = "电站运行日报id", required = true, example = "1") @PathVariable Integer id) {
		PowerDataReportOutput output = powerDataReportService.detail(id);
		return WrappedResult.successWrappedResult(output);
	}

	/**
	 * 手动触发生成日报（实验用）
	 */
	@PostMapping("/generate")
	@SysLog(action = "手动生成日报")
	@ApiOperation(value = "手动生成指定日期日报")
	public WrappedResult<Boolean> generate(@RequestBody PowerDataReportPage input) {
		// 复用 page 的 start 作为日期；station 可选
		try {
			Integer station = input.getStation() == null ? 1 : input.getStation();
			String day = input.getStart();
			if (day == null || day.isEmpty()) {
				day = qrsoft.common.util.DateUtil.currentDateStr(qrsoft.common.util.DateUtil.YYMMDD);
			}
			java.util.Date d = qrsoft.common.util.DateUtil.stringToDate(day.substring(0, 10), qrsoft.common.util.DateUtil.YYMMDD);
			boolean ok = powerDataReportService.generateForDay(station, d);
			return WrappedResult.successWrappedResult(ok);
		} catch (Exception e) {
			return WrappedResult.failedWrappedResult("生成失败: " + e.getMessage());
		}
	}
}
