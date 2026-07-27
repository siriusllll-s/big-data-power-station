package qrsoft.information.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qrsoft.common.constant.BaseConstant;
import qrsoft.common.constant.ScreenConstant;
import qrsoft.common.entity.KWhAmmeter;
import qrsoft.common.entity.KWhInverter;
import qrsoft.common.entity.KWhStation;
import qrsoft.common.entity.Station;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.input.DataAmmeterInput;
import qrsoft.information.dto.output.*;
import qrsoft.information.mapper.KWhAmmeterMapper;
import qrsoft.information.mapper.KWhInverterMapper;
import qrsoft.information.mapper.KWhStationMapper;
import qrsoft.information.mapper.StationMapper;
import qrsoft.information.service.IStationScreenService;
import qrsoft.information.utils.DateUtils;
import qrsoft.information.utils.HBaseUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class StationScreenServiceImpl implements IStationScreenService {

	@Autowired
	private RestHighLevelClient restHighLevelClient;
	@Autowired
	private KWhAmmeterMapper kWhAmmeterMapper;
	@Autowired
	private KWhInverterMapper kWhInverterMapper;
	@Autowired
	private KWhStationMapper kWhStationMapper;
	@Autowired
	private StationMapper stationMapper;

	@Override
	public WeatherOutput latestWeather() {
		return new WeatherOutput();
	}

	/**
	 * 近30天发电量/效率（日发电量统计与近30天效率共用）
	 */
	@Override
	public List<StationPowerAndRadioOutput> stationLastThirtyDayPower(Integer id) {
		Integer stationId = id == null ? BaseConstant.STATION : id;
		String[] between = DateUtils.lastThityDayBetween();
		QueryWrapper<KWhStation> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("station", stationId);
		queryWrapper.between("power_date", between[0], between[1]);
		queryWrapper.orderByAsc("power_date");
		List<KWhStation> list = kWhStationMapper.selectList(queryWrapper);
		if (list == null || list.isEmpty()) {
			// 兼容 station 字段为空的历史数据
			queryWrapper = new QueryWrapper<>();
			queryWrapper.between("power_date", between[0], between[1]);
			queryWrapper.orderByAsc("power_date");
			list = kWhStationMapper.selectList(queryWrapper);
		}
		if (list == null) {
			return new ArrayList<>();
		}
		return list.stream().map(StationPowerAndRadioOutput::entityToOutput).collect(Collectors.toList());
	}

	/**
	 * 今日/今年发电量，并计算节煤量、CO2 减排量
	 */
	@Override
	public StationDayAndYearPowerOutput stationDayAndYearPower(Integer id) {
		StationDayAndYearPowerOutput output = new StationDayAndYearPowerOutput();
		Date today = DateUtil.getToDayDate();
		Date yearStart = DateUtil.getToYearDate();

		// 今日发电量
		List<KWhStation> dayList = kWhStationMapper.selectList(new QueryWrapper<KWhStation>()
				.select("kwh")
				.eq("power_date", today));
		double dayKWh = sumStationKwh(dayList);

		// 今年发电量
		List<KWhStation> yearList = kWhStationMapper.selectList(new QueryWrapper<KWhStation>()
				.select("kwh")
				.ge("power_date", yearStart)
				.le("power_date", today));
		double yearKWh = sumStationKwh(yearList);

		// 节煤/减排：系数按 kg/kWh 用于大屏展示（教学常用）
		double dayCoal = dayKWh * ScreenConstant.COAL_RATIO;
		double yearCoal = yearKWh * ScreenConstant.COAL_RATIO;
		double dayCo2 = dayKWh * ScreenConstant.CO2_RATIO;
		double yearCo2 = yearKWh * ScreenConstant.CO2_RATIO;

		output.setDayKWh(scale2(dayKWh));
		output.setYearKWh(scale2(yearKWh));
		output.setDayReduceCoal(scale2(dayCoal));
		output.setYearReduceCoal(scale2(yearCoal));
		output.setDayReduceCO2(scale2(dayCo2));
		output.setYearReduceCO2(scale2(yearCo2));

		output.setDayPower(output.getDayKWh());
		output.setYearPower(output.getYearKWh());
		output.setCoalSave(output.getYearReduceCoal());
		output.setCo2Reduce(output.getYearReduceCO2());
		return output;
	}

	private double sumStationKwh(List<KWhStation> list) {
		if (list == null || list.isEmpty()) {
			return 0D;
		}
		return list.stream()
				.filter(s -> s != null && s.getKwh() != null)
				.mapToDouble(KWhStation::getKwh)
				.sum();
	}

	private double scale2(double v) {
		return BigDecimal.valueOf(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public StationAllAndAverOutput stationAllAndAverage() {
		return new StationAllAndAverOutput();
	}

	/**
	 * 电站本月发电量和本月发电效率
	 */
	@Override
	public StationMonthPowerOutput stationMonthPower() {
		StationMonthPowerOutput output = new StationMonthPowerOutput();
		Station station = stationMapper.getSimpleById(BaseConstant.STATION);
		output.setStationInfo(StationSimpleOutput.entityToOutput(station));

		Date today = DateUtil.getToDayDate();
		Date theMonth = DateUtil.getToMonthDate();

		List<KWhAmmeter> ammeters = kWhAmmeterMapper.selectList(new QueryWrapper<KWhAmmeter>().select("kwh")
				.ge("power_date", theMonth).le("power_date", today));
		List<KWhInverter> inverters = kWhInverterMapper.selectList(new QueryWrapper<KWhInverter>().select("kwh")
				.ge("power_date", theMonth).le("power_date", today));

		double allKWh = 0D;
		if (ammeters != null) {
			allKWh = ammeters.stream()
					.filter(a -> a != null && a.getKwh() != null)
					.mapToDouble(KWhAmmeter::getKwh)
					.sum();
		}
		output.setAllKWh(BigDecimal.valueOf(allKWh).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		output.setMonthPower(output.getAllKWh());

		if (output.getAllKWh() == null || output.getAllKWh().equals(0D)) {
			output.setPowerRatio(0D);
		} else {
			double allInverter = 0D;
			if (inverters != null) {
				allInverter = inverters.stream()
						.filter(i -> i != null && i.getKwh() != null)
						.mapToDouble(KWhInverter::getKwh)
						.sum();
			}
			if (allInverter <= 0D) {
				output.setPowerRatio(0D);
			} else {
				output.setPowerRatio(BigDecimal.valueOf(output.getAllKWh())
						.divide(BigDecimal.valueOf(allInverter), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
		}
		output.setMonthRatio(output.getPowerRatio());
		return output;
	}

	@Override
	public StationTypePowerOutput stationTypePower() {
		return new StationTypePowerOutput();
	}

	@Override
	public List<StationPowerMonthOutput> kWhStatisticByMonth(Integer id) {
		return new ArrayList<>();
	}

	@Override
	public List<StationPowerAndRadioOutput> stationNextThirtyDayPower(Integer id) {
		return new ArrayList<>();
	}

	@Override
	public List<FaultCountOutput> stationFaultCount(Integer id) {
		return new ArrayList<>();
	}

	/**
	 * 电表读数：先通过 ES 获取 HBase rowKey，再按 rowKey 读取 HBase 数据
	 */
	@Override
	public List<DataAmmeterOutput> ammeter(DataAmmeterInput input) {
		List<DataAmmeterOutput> list = new ArrayList<>();
		if (restHighLevelClient == null) {
			System.err.println("ES client is null");
			return list;
		}

		SearchRequest searchRequest = new SearchRequest("ammeter");
		SearchSourceBuilder builder = new SearchSourceBuilder()
				.size(5)
				.sort("createTime", SortOrder.DESC)
				.timeout(new TimeValue(60, TimeUnit.SECONDS));

		if (input != null && StringUtils.isNotBlank(input.getName())) {
			String name = input.getName();
			if (name.length() <= 2) {
				name = name + "号电表";
			}
			builder.query(QueryBuilders.matchPhraseQuery("name", name));
		}
		searchRequest.source(builder);

		try {
			SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			SearchHit[] hits = response.getHits().getHits();
			for (SearchHit hit : hits) {
				Map<String, Object> source = hit.getSourceAsMap();
				Object rowKeyObj = source.get("rowKey");
				if (rowKeyObj == null) {
					continue;
				}
				Result result = null;
				if (HBaseUtil.isAvailable()) {
					try {
						final String rk = String.valueOf(rowKeyObj);
						java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newSingleThreadExecutor();
						try {
							java.util.concurrent.Future<Result> future = pool.submit(() -> HBaseUtil.getRow("AmmeterInfo", rk));
							result = future.get(2, TimeUnit.SECONDS);
						} finally {
							pool.shutdownNow();
						}
					} catch (Exception hbaseEx) {
						System.err.println("HBase getRow failed, fallback ES: " + hbaseEx.getMessage());
						HBaseUtil.markUnavailable();
					}
				}
				if (result == null || result.isEmpty()) {
					// HBase 无数据/不可用时回退使用 ES 文档字段
					list.add(buildFromEsSource(source));
					continue;
				}

				List<Cell> cells = result.listCells();
				if (cells != null) {
					DataAmmeterOutput output = new DataAmmeterOutput();
					for (Cell c : cells) {
						String name = Bytes.toString(c.getQualifierArray(), c.getQualifierOffset(), c.getQualifierLength());
						String value = Bytes.toString(c.getValueArray(), c.getValueOffset(), c.getValueLength());
						switch (name) {
							case "ammeter":
								output.setAmmeter(BigDecimal.valueOf(Double.parseDouble(value))
										.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
								break;
							case "createTime":
								output.setCreateTime(new Date(Long.parseLong(value)));
								break;
							case "name":
								output.setName(value);
								break;
							case "station":
								output.setStation(Integer.valueOf(value));
								break;
							case "inverter":
								output.setInverter(value);
								break;
							default:
								break;
						}
					}
					list.add(output);
				}
			}
		} catch (Exception e) {
			System.err.println("ammeter query failed: " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	private DataAmmeterOutput buildFromEsSource(Map<String, Object> source) {
		DataAmmeterOutput output = new DataAmmeterOutput();
		try {
			if (source.get("station") != null) {
				output.setStation(Integer.valueOf(String.valueOf(source.get("station"))));
			}
			if (source.get("name") != null) {
				output.setName(String.valueOf(source.get("name")));
			}
			if (source.get("ammeter") != null) {
				output.setAmmeter(BigDecimal.valueOf(Double.parseDouble(String.valueOf(source.get("ammeter"))))
						.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			if (source.get("inverter") != null) {
				output.setInverter(String.valueOf(source.get("inverter")));
			}
			if (source.get("createTime") != null) {
				output.setCreateTime(new Date(Long.parseLong(String.valueOf(source.get("createTime")))));
			}
		} catch (Exception ignore) {
		}
		return output;
	}
}
