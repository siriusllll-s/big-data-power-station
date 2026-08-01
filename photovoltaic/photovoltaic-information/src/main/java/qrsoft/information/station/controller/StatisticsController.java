package qrsoft.information.station.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qrsoft.information.shared.aspect.SysLog;
import qrsoft.information.dto.input.StatisticsStationInput;
import qrsoft.information.dto.output.StatisticsStationOutput;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.station.service.IStatisticsService;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@Api(tags = "发电量统计")
public class StatisticsController {

	@Autowired
	private IStatisticsService statisticsService;

	@PostMapping("/station")
	@ApiOperation(value = "电站发电量统计")
	@SysLog(action = "电站发电量统计")
	public WrappedResult stationStatistics(@RequestBody StatisticsStationInput input) {
		// 前端传 type/start/end 为字符串时兼容：type 可能是 "0"/"1"
		if (input != null && input.getType() == null) {
			// leave empty
		}
		List<StatisticsStationOutput> list = statisticsService.stationStatistics(input);
		return WrappedResult.successWrappedResult(list);
	}
}
