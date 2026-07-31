package qrsoft.information.service;

import qrsoft.information.dto.input.DataAmmeterInput;
import qrsoft.information.dto.output.*;

import java.util.List;

/**
 * @author Created by Young on 2021/4/25
 */
public interface IStationScreenService {

	/**
	 * 获取最近一条天气信息
	 *
	 * @return 最近一条天气信息
	 */
	WeatherOutput latestWeather();

	/**
	 * 查询近30天发电效率
	 *
	 * @param id 电站id
	 * @return 近30天发电效率
	 */
	List<StationPowerAndRadioOutput> stationLastThirtyDayPower(Integer id);

	/**
	 * 查询今日发电量年发电量，计算节煤量和二氧化碳量
	 *
	 * @param id 电站id
	 * @return 今日发电量，年发电量，节煤量和二氧化碳量
	 */
	StationDayAndYearPowerOutput stationDayAndYearPower(Integer id);

	/**
	 * 电站综合及平均发电相关统计
	 *
	 * @return 电站综合及平均发电相关统计
	 */
	StationAllAndAverOutput stationAllAndAverage();

	/**
	 * 电站本月发电量和本月发电效率
	 *
	 * @return 电站本月发电量和本月发电效率
	 */
	StationMonthPowerOutput stationMonthPower();

	/**
	 * 各类电站分布数和日发电效率
	 *
	 * @return 各类电站分布数和日发电效率
	 */
	StationTypePowerOutput stationTypePower();

	/**
	 * 按照月统计过去12个月发电量
	 *
	 * @param id 电站id
	 * @return 过去12个月发电量
	 */
	List<StationPowerMonthOutput> kWhStatisticByMonth(Integer id);

	/**
	 * 预测未来30天发电量
	 *
	 * @param id 电站id
	 * @return 未来30天发电量
	 */
	List<StationPowerAndRadioOutput> stationNextThirtyDayPower(Integer id);

	/**
	 * 电站设备故障统计
	 *
	 * @param id 电站id
	 * @return 电站设备故障统计结果
	 */
	List<FaultCountOutput> stationFaultCount(Integer id);

	/**
	 * 电表读数
	 *
	 * @param input 电表读书查询条件
	 * @return 电表读数结果
	 */
	List<DataAmmeterOutput> ammeter(DataAmmeterInput input);
}
