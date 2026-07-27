package qrsoft.information.service;

import qrsoft.information.dto.input.StatisticsStationInput;
import qrsoft.information.dto.output.StatisticsStationOutput;

import java.util.List;

public interface IStatisticsService {

	/**
	 * 电站发电量统计
	 */
	List<StatisticsStationOutput> stationStatistics(StatisticsStationInput input);
}
