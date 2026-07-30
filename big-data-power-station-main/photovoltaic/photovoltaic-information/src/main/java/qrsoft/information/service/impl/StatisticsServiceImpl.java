package qrsoft.information.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qrsoft.common.entity.KWhStation;
import qrsoft.information.dto.input.StatisticsStationInput;
import qrsoft.information.dto.output.StatisticsStationOutput;
import qrsoft.information.mapper.KWhStationMapper;
import qrsoft.information.service.IStatisticsService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements IStatisticsService {

	@Autowired
	private KWhStationMapper kWhStationMapper;

	private static final Integer DAY = 0;
	private static final Integer MONTH = 1;

	/**
	 * 电站发电量统计
	 */
	@Override
	public List<StatisticsStationOutput> stationStatistics(StatisticsStationInput input) {
		if (input == null || input.getStart() == null || input.getEnd() == null || input.getType() == null) {
			return new ArrayList<>();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(input.getEnd());
		if (MONTH.equals(input.getType())) {
			// 月统计：结束月取当月最后一天
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			input.setEnd(calendar.getTime());
		}

		QueryWrapper<KWhStation> query = new QueryWrapper<>();
		query.ge("power_date", input.getStart()).le("power_date", input.getEnd());
		query.orderByAsc("power_date");
		List<KWhStation> list = kWhStationMapper.selectList(query);
		if (list == null) {
			list = new ArrayList<>();
		}

		if (DAY.equals(input.getType())) {
			return list.stream().map(StatisticsStationOutput::entityToOutputDay).collect(Collectors.toList());
		} else if (MONTH.equals(input.getType())) {
			Map<String, List<KWhStation>> map = new HashMap<>();
			for (KWhStation kWhStation : list) {
				if (kWhStation.getPowerDate() == null) {
					continue;
				}
				calendar.setTime(kWhStation.getPowerDate());
				String key = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
				List<KWhStation> kWhStations = map.containsKey(key) ? map.get(key) : new ArrayList<>();
				kWhStations.add(kWhStation);
				map.put(key, kWhStations);
			}
			return StatisticsStationOutput.entityToOutputMonth(map);
		}
		return new ArrayList<>();
	}
}
