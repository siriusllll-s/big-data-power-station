package qrsoft.information.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qrsoft.common.entity.KWhStation;
import qrsoft.common.entity.PowerDataReport;
import qrsoft.common.entity.Weather;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.input.PowerDataReportInput;
import qrsoft.information.dto.output.PowerDataReportOutput;
import qrsoft.information.dto.page.PowerDataReportPage;
import qrsoft.information.dto.vo.ResultPage;
import qrsoft.information.mapper.KWhStationMapper;
import qrsoft.information.mapper.PowerDataReportMapper;
import qrsoft.information.mapper.WeatherMapper;
import qrsoft.information.service.IPowerDataReportService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PowerDataReportServiceImpl implements IPowerDataReportService {

	@Autowired
	private PowerDataReportMapper dataReportMapper;
	@Autowired
	private KWhStationMapper kWhStationMapper;
	@Autowired
	private WeatherMapper weatherMapper;

	@Override
	@Transactional
	public void update(PowerDataReportInput input) {
		if (input == null || input.getId() == null) {
			throw new RuntimeException("参数不能为空");
		}
		PowerDataReport dataReport = dataReportMapper.selectById(input.getId());
		if (dataReport == null) {
			throw new RuntimeException("电站运行日报不存在");
		}
		PowerDataReportInput.inputToEntity(input, dataReport);
		dataReport.setUpdateTime(new Date());
		int i = dataReportMapper.updateById(dataReport);
		if (i != 1) {
			throw new RuntimeException("电站运行日报更新失败");
		}
	}

	@Override
	public ResultPage<PowerDataReportOutput> pageByParam(PowerDataReportPage input) {
		if (input == null) {
			input = new PowerDataReportPage();
		}
		QueryWrapper<PowerDataReport> query = new QueryWrapper<>();
		if (input.getStation() != null) {
			query.eq("station", input.getStation());
		}
		try {
			if (StringUtils.isNotBlank(input.getStart())) {
				Date start = DateUtil.stringToDate(input.getStart().substring(0, 10), DateUtil.YYMMDD);
				query.ge("report_date", start);
			}
			if (StringUtils.isNotBlank(input.getEnd())) {
				// 结束日含当天：end + 1 天 lt
				Date end = DateUtil.stringToDate(input.getEnd().substring(0, 10), DateUtil.YYMMDD);
				query.lt("report_date", DateUtil.addDay(end, 1));
			}
		} catch (Exception e) {
			throw new RuntimeException("日期格式错误，需 yyyy-MM-dd");
		}
		query.orderByDesc("report_date").orderByDesc("id");
		int pageNo = input.getPage() == null || input.getPage() < 1 ? 1 : input.getPage();
		int pageSize = input.getLimit() == null || input.getLimit() < 1 ? 10 : input.getLimit();
		Page<PowerDataReport> page = dataReportMapper.selectPage(new Page<>(pageNo, pageSize), query);
		ResultPage<PowerDataReportOutput> outputs = new ResultPage<>(page);
		outputs.setList(page.getRecords().stream()
				.map(PowerDataReportOutput::entityToOutput)
				.collect(Collectors.toList()));
		return outputs;
	}

	@Override
	public PowerDataReportOutput detail(Integer id) {
		PowerDataReport dataReport = dataReportMapper.selectById(id);
		if (dataReport == null) {
			throw new RuntimeException("电站运行日报不存在");
		}
		return PowerDataReportOutput.entityToOutput(dataReport);
	}

	/**
	 * 从 k_wh_station + weather 汇总生成日报
	 */
	@Override
	@Transactional
	public boolean generateForDay(Integer station, Date day) {
		if (station == null || day == null) {
			return false;
		}
		Date dayStart = truncateDay(day);
		String dayStr = DateUtil.dateToString(dayStart, DateUtil.YYMMDD);

		// 发电量 / 辐照 / 效率：当日 k_wh_station
		List<KWhStation> kwhList = kWhStationMapper.selectList(new QueryWrapper<KWhStation>()
				.eq("station", station)
				.eq("power_date", dayStart));
		double kwh = 0D;
		double radiation = 0D;
		double powerRatio = 0D;
		if (kwhList != null && !kwhList.isEmpty()) {
			kwh = kwhList.stream().mapToDouble(e -> e.getKwh() == null ? 0D : e.getKwh()).sum();
			radiation = kwhList.stream().mapToDouble(e -> e.getRadiation() == null ? 0D : e.getRadiation()).average().orElse(0D);
			powerRatio = kwhList.stream().mapToDouble(e -> e.getPowerRatio() == null ? 0D : e.getPowerRatio()).average().orElse(0D);
		}

		// 天气：取当日最近一条 weather，否则按辐照推断
		String weatherText = resolveWeather(station, dayStart, radiation);

		PowerDataReport existing = dataReportMapper.selectOne(new QueryWrapper<PowerDataReport>()
				.eq("station", station)
				.eq("report_date", dayStart)
				.last("limit 1"));
		Date now = new Date();
		if (existing == null) {
			PowerDataReport report = new PowerDataReport();
			report.setStation(station);
			report.setReportDate(dayStart);
			report.setWeather(weatherText);
			report.setKwh(scale(kwh));
			report.setRadiation(scale(radiation));
			report.setPowerRatio(scale(powerRatio));
			report.setSummary(buildDefaultSummary(dayStr, kwh, powerRatio, weatherText));
			report.setCreateTime(now);
			report.setUpdateTime(now);
			return dataReportMapper.insert(report) == 1;
		}
		// 已有记录：刷新计算字段，保留人工 summary/weather 若已编辑过也可覆盖计算字段
		existing.setKwh(scale(kwh));
		existing.setRadiation(scale(radiation));
		existing.setPowerRatio(scale(powerRatio));
		if (StringUtils.isBlank(existing.getWeather())) {
			existing.setWeather(weatherText);
		}
		existing.setUpdateTime(now);
		return dataReportMapper.updateById(existing) == 1;
	}

	private String resolveWeather(Integer station, Date dayStart, double radiation) {
		try {
			Date next = DateUtil.addDay(dayStart, 1);
			List<Weather> list = weatherMapper.selectList(new QueryWrapper<Weather>()
					.eq("station", station)
					.ge("weather_time", dayStart)
					.lt("weather_time", next)
					.orderByDesc("weather_time")
					.last("limit 1"));
			if (list != null && !list.isEmpty()) {
				Weather w = list.get(0);
				if (StringUtils.isNotBlank(w.getWeatherName())) {
					return w.getWeatherName();
				}
				if (w.getIrradiance() != null) {
					return weatherByIrradiance(w.getIrradiance());
				}
			}
		} catch (Exception ignored) {
			// fall through
		}
		return weatherByIrradiance(radiation * 100); // radiation 可能是日累计，兜底
	}

	private static String weatherByIrradiance(double irradiance) {
		if (irradiance >= 600) {
			return "晴";
		}
		if (irradiance >= 300) {
			return "多云";
		}
		if (irradiance >= 100) {
			return "阴";
		}
		return "雨";
	}

	private static String buildDefaultSummary(String day, double kwh, double ratio, String weather) {
		return String.format("%s 天气%s，日发电量 %.2f kWh，发电效率 %.2f%%。", day, weather, kwh, ratio);
	}

	private static Date truncateDay(Date date) {
		try {
			return DateUtil.stringToDate(DateUtil.dateToString(date, DateUtil.YYMMDD), DateUtil.YYMMDD);
		} catch (Exception e) {
			return date;
		}
	}

	private static Double scale(double v) {
		return BigDecimal.valueOf(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
