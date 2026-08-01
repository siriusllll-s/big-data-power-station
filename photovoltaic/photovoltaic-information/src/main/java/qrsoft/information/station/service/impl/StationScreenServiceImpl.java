package qrsoft.information.station.service.impl;

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
import qrsoft.common.entity.*;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.input.DataAmmeterInput;
import qrsoft.information.dto.output.*;
import qrsoft.information.mapper.*;
import qrsoft.information.station.service.IStationScreenService;
import qrsoft.information.shared.utils.DateUtils;
import qrsoft.information.shared.utils.HBaseUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Created by Young on 2021/4/25
 */
@Service
public class StationScreenServiceImpl implements IStationScreenService {

	@Autowired
	private KWhStationMapper kWhStationMapper;

	@Autowired
	private KWhAmmeterMapper kWhAmmeterMapper;

	@Autowired
	private KWhInverterMapper kWhInverterMapper;

	@Autowired
	private WeatherMapper weatherMapper;

	@Autowired
	private StationMapper stationMapper;

	@Autowired
	private StationForecastMapper forecastMapper;

	@Autowired
	private FaultCountMapper faultCountMapper;

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	/**
	 * 获取最近一条天气信息
	 *
	 * @return 最近一条天气信息
	 */
	@Override
	public WeatherOutput latestWeather() {
		Weather weather = weatherMapper.queryLastWeather();
		return WeatherOutput.entityToOutput(weather);
	}

	/**
	 * 查询近30天发电效率
	 *
	 * @param id 电站id
	 * @return 近30天发电效率
	 */
	@Override
	public List<StationPowerAndRadioOutput> stationLastThirtyDayPower(Integer id) {
		String[] between = DateUtils.lastThityDayBetween();
		QueryWrapper<KWhStation> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("station", id);
		queryWrapper.between("power_date", between[0], between[1]);
		List<KWhStation> list = kWhStationMapper.selectList(queryWrapper);
		return list.stream().map(StationPowerAndRadioOutput::entityToOutput).collect(Collectors.toList());
	}

	/**
	 * 查询今日发电量年发电量，计算节煤量和二氧化碳量
	 *
	 * @param id 电站id
	 * @return 今日发电量，年发电量，节煤量和二氧化碳量
	 */
	@Override
	public StationDayAndYearPowerOutput stationDayAndYearPower(Integer id) {
		StationDayAndYearPowerOutput output = new StationDayAndYearPowerOutput();
		//获取今天的日期
		Date today = DateUtil.getToDayDate();
		//查询今日发电量
		List<KWhAmmeter> todayS = kWhAmmeterMapper.selectList(new QueryWrapper<KWhAmmeter>().eq("power_date", today));
		double dayPower = todayS.stream().mapToDouble(KWhAmmeter::getKwh).sum();
		//日发电量
		output.setDayKWh(BigDecimal.valueOf(dayPower).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//日节煤量
		output.setDayReduceCoal(BigDecimal.valueOf(dayPower).multiply(BigDecimal.valueOf(ScreenConstant.REDUCE_COAL_FORMAT))
				.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//日CO2减排
		output.setDayReduceCO2(BigDecimal.valueOf(dayPower).multiply(BigDecimal.valueOf(ScreenConstant.REDUCE_CO2_FORMAT))
				.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//获取当年第一天日期
		Date yearFirstDate = DateUtil.getToYearDate();
		//查询年发电量（除去今天）
		List<KWhAmmeter> thisYear = kWhAmmeterMapper.selectList(new QueryWrapper<KWhAmmeter>().between("power_date", yearFirstDate,
				today));
		//加上今天的发电量
		double yearPower = thisYear.stream().mapToDouble(KWhAmmeter::getKwh).sum() + dayPower;
		//年发电量
		output.setYearKWh(BigDecimal.valueOf(yearPower).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//年节煤
		output.setYearReduceCoal(BigDecimal.valueOf(yearPower).multiply(BigDecimal.valueOf(ScreenConstant.REDUCE_COAL_FORMAT))
				.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//年CO2减排
		output.setYearReduceCO2(BigDecimal.valueOf(yearPower).multiply(BigDecimal.valueOf(ScreenConstant.REDUCE_CO2_FORMAT))
				.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return output;
	}

	/**
	 * 电站综合及平均发电相关统计
	 *
	 * @return 电站综合及平均发电相关统计
	 */
	@Override
	public StationAllAndAverOutput stationAllAndAverage() {
		StationAllAndAverOutput output = new StationAllAndAverOutput();
		//获取总共的天数
		Integer dayNum = kWhAmmeterMapper.selectCount(new QueryWrapper<KWhAmmeter>().select("distinct power_date"));
		//统计累计发电量
		List<KWhAmmeter> allAmmeter = kWhAmmeterMapper.selectList(new QueryWrapper<KWhAmmeter>().select("kwh"));
		output.setAllKWh(allAmmeter.stream().mapToDouble(KWhAmmeter::getKwh).sum());
		//计算日均发电量
		output.setAverageKWh(BigDecimal.valueOf(output.getAllKWh()).divide(BigDecimal.valueOf(dayNum), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//计算累计收入
		output.setAllInCome(BigDecimal.valueOf(output.getAllKWh()).multiply(BigDecimal.valueOf(ScreenConstant.MONEY_FORMAT))
				.divide(BigDecimal.valueOf(10000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//获取今日发电量
		List<KWhAmmeter> toady = kWhAmmeterMapper.selectList(new QueryWrapper<KWhAmmeter>().eq("power_date", DateUtil.getToDayDate()));
		//计算今日收入
		output.setTodayInCome(BigDecimal.valueOf(toady.stream().mapToDouble(KWhAmmeter::getKwh).sum())
				.multiply(BigDecimal.valueOf(ScreenConstant.MONEY_FORMAT))
				.divide(BigDecimal.valueOf(10000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//计算日均收入
		output.setAverageInCome(BigDecimal.valueOf(output.getAllInCome())
				.divide(BigDecimal.valueOf(dayNum), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//计算总CO2减排量
		output.setAllReduceCO2(BigDecimal.valueOf(output.getAllKWh()).multiply(BigDecimal.valueOf(ScreenConstant.REDUCE_CO2_FORMAT))
				.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//计算总节煤量
		output.setAllReduceCoal(BigDecimal.valueOf(output.getAllKWh()).multiply(BigDecimal.valueOf(ScreenConstant.REDUCE_COAL_FORMAT))
				.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//计算日均CO2减排量
		output.setAverageReduceCO2(BigDecimal.valueOf(output.getAllReduceCO2())
				.divide(BigDecimal.valueOf(dayNum), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//计算日均节煤量
		output.setAverageReduceCoal(BigDecimal.valueOf(output.getAllReduceCoal())
				.divide(BigDecimal.valueOf(dayNum), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		//累计发电量处理成2位小鼠
		output.setAllKWh(BigDecimal.valueOf(output.getAllKWh()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return output;
	}

	/**
	 * 电站本月发电量和本月发电效率
	 *
	 * @return 电站本月发电量和本月发电效率
	 */
	@Override
	public StationMonthPowerOutput stationMonthPower() {
		StationMonthPowerOutput output = new StationMonthPowerOutput();
		//获取电站简略信息
		Station station = stationMapper.getSimpleById(BaseConstant.STATION);
		output.setStationInfo(StationSimpleOutput.entityToOutput(station));
		//获取今天的日期
		Date today = DateUtil.getToDayDate();
		//获取本月的开始日期
		Date theMonth = DateUtil.getToMonthDate();
		//获取本月的全部电表数据
		List<KWhAmmeter> ammeters = kWhAmmeterMapper.selectList(new QueryWrapper<KWhAmmeter>().select("kwh")
				.ge("power_date", theMonth).le("power_date", today));
		//获取本月的全部逆变器数据
		List<KWhInverter> inverters = kWhInverterMapper.selectList(new QueryWrapper<KWhInverter>().select("kwh")
				.ge("power_date", theMonth).le("power_date", today));
		//计算发电量
		output.setAllKWh(ammeters.stream().mapToDouble(KWhAmmeter::getKwh).sum());
		//本月发电量为0时，则发电效率也为0
		if (output.getAllKWh().equals(0D)) {
			output.setPowerRatio(0D);
		} else {
			double allInverter = inverters.stream().mapToDouble(KWhInverter::getKwh).sum();
			output.setPowerRatio(BigDecimal.valueOf(output.getAllKWh())
					.divide(BigDecimal.valueOf(allInverter), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		return output;
	}

	/**
	 * 各类电站分布数和日发电效率
	 *
	 * @return 各类电站分布数和日发电效率
	 */
	@Override
	public StationTypePowerOutput stationTypePower() {
		StationTypePowerOutput output = new StationTypePowerOutput();
		//获取电站简略信息
		Station station = stationMapper.getSimpleById(BaseConstant.STATION);
		output.setType(station.getType());
		//获取今天的日期
		Date today = DateUtil.getToDayDate();
		//获取今天的全部电表数据
		List<KWhAmmeter> ammeters = kWhAmmeterMapper.selectList(new QueryWrapper<KWhAmmeter>().select("kwh").eq("power_date", today));
		double ammeter = ammeters.stream().mapToDouble(KWhAmmeter::getKwh).sum();
		//获取今天的全部逆变器数据
		List<KWhInverter> inverters = kWhInverterMapper.selectList(new QueryWrapper<KWhInverter>().select("kwh").eq("power_date", today));
		double inverter = inverters.stream().mapToDouble(KWhInverter::getKwh).sum();
		output.setPowerRatio(BigDecimal.valueOf(ammeter).divide(BigDecimal.valueOf(inverter), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return output;
	}

	/**
	 * 按照月统计过去12个月发电量
	 *
	 * @param id 电站id
	 * @return 过去12个月发电量
	 */
	@Override
	public List<StationPowerMonthOutput> kWhStatisticByMonth(Integer id) {
		//获取当前之间前12个月的月份
		String startMonth = DateUtil.dateToString(DateUtil.addYear(new Date(), -1), DateUtil.YYMM);
		//按月统计过去12个月，每个月发电情况
		List<Map<String, Object>> listResult = kWhStationMapper.kWhStatisticByMonth(id, startMonth);
		//整理数据
		return listResult.stream().map(item -> {
			StationPowerMonthOutput stationPowerMonthOutput = new StationPowerMonthOutput();
			try {
				Object monthObj = item.get("month");
				String month = monthObj == null ? null : String.valueOf(monthObj);
				if (month != null && !month.isEmpty()) {
					stationPowerMonthOutput.setPowerDate(DateUtil.stringToDate(month, DateUtil.YYMM));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Object kwhObj = item.get("kwh");
			double kwhVal = kwhObj == null ? 0D : Double.parseDouble(String.valueOf(kwhObj));
			stationPowerMonthOutput.setKwh(BigDecimal.valueOf(kwhVal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			return stationPowerMonthOutput;
		}).collect(Collectors.toList());
	}

	/**
	 * 预测未来30天发电量
	 *
	 * @param id 电站id
	 * @return 未来30天发电量
	 */
	@Override
	public List<StationPowerAndRadioOutput> stationNextThirtyDayPower(Integer id) {
		//获取未来30天日期
		String[] betweenDate = DateUtils.nextThityDayBetween();

		QueryWrapper<StationForecast> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("station", id);
		queryWrapper.between("power_date", betweenDate[0], betweenDate[1]);
		List<StationForecast> list = forecastMapper.selectList(queryWrapper);
		return list.stream().map(item -> {
			StationPowerAndRadioOutput stationPowerAndRadioOutput = new StationPowerAndRadioOutput();
			stationPowerAndRadioOutput.setPowerDate(item.getPowerDate());
			stationPowerAndRadioOutput.setKwh(BigDecimal.valueOf(item.getDailyPower()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			return stationPowerAndRadioOutput;
		}).collect(Collectors.toList());
	}

	/**
	 * 电站设备故障统计
	 *
	 * @param id 电站id
	 * @return 电站设备故障统计结果
	 */
	@Override
	public List<FaultCountOutput> stationFaultCount(Integer id) {
		List<FaultCount> list = faultCountMapper.selectList(new QueryWrapper<FaultCount>().eq("station", id));
		return list.stream().map(FaultCountOutput::entityToOutput).collect(Collectors.toList());
	}

	/**
	 * 电表读数
	 *
	 * @param input 电表读数查询条件
	 * @return 电表读数结果
	 */
	@Override
	public List<DataAmmeterOutput> ammeter(DataAmmeterInput input) {
		List<DataAmmeterOutput> list = new ArrayList<>();
		//构建查询条件
		SearchRequest searchRequest = new SearchRequest("ammeter");
		SearchSourceBuilder builder = new SearchSourceBuilder()
				.size(5)
				.sort("createTime", SortOrder.DESC)
				.timeout(new TimeValue(60, TimeUnit.SECONDS));
		if (StringUtils.isNotBlank(input.getName()))
			builder.query(QueryBuilders.matchPhraseQuery("name", input.getName()));
		searchRequest.source(builder);
		try {
			//获取查询数据
			SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			List<SearchHit> hitList = Arrays.stream(response.getHits().getHits()).collect(Collectors.toList());
			for (SearchHit hit : hitList) {
				Result result = HBaseUtil.getRow("AmmeterInfo", String.valueOf(hit.getSourceAsMap().get("rowKey")));
				List<Cell> cells = result.listCells();
				if (cells != null) {
					DataAmmeterOutput output = new DataAmmeterOutput();
					for (Cell c : cells) {
						String name = Bytes.toString(c.getQualifierArray(), c.getQualifierOffset(), c.getQualifierLength());
						String value = Bytes.toString(c.getValueArray(), c.getValueOffset(), c.getValueLength());
						switch (name) {
							case "ammeter":
								output.setAmmeter(BigDecimal.valueOf(Double.parseDouble(value)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
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
						}
					}
					list.add(output);
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return list;
	}

}
