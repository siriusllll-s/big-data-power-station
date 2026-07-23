package qrsoft.information.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qrsoft.common.constant.ScreenConstant;
import qrsoft.common.entity.KWhAmmeter;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.input.DataAmmeterInput;
import qrsoft.information.dto.output.*;
import qrsoft.information.mapper.KWhAmmeterMapper;
import qrsoft.information.service.IStationScreenService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class StationScreenServiceImpl implements IStationScreenService {
	@Autowired
	private KWhAmmeterMapper kWhAmmeterMapper;
	@Override public WeatherOutput latestWeather() { return new WeatherOutput(); }
	@Override public List<StationPowerAndRadioOutput> stationLastThirtyDayPower(Integer id) { return new ArrayList<StationPowerAndRadioOutput>(); }
	@Override public StationDayAndYearPowerOutput stationDayAndYearPower(Integer id) { return new StationDayAndYearPowerOutput(); }
	@Override
	public StationAllAndAverOutput stationAllAndAverage() {
		StationAllAndAverOutput output = new StationAllAndAverOutput();
		Integer dayNum = kWhAmmeterMapper.selectCount(new QueryWrapper<KWhAmmeter>().select("distinct power_date"));
		if (dayNum == null || dayNum.intValue() == 0) dayNum = Integer.valueOf(1);
		List<KWhAmmeter> allAmmeter = kWhAmmeterMapper.selectList(new QueryWrapper<KWhAmmeter>().select("kwh"));
		double all = 0.0d;
		if (allAmmeter != null) {
			for (KWhAmmeter a : allAmmeter) {
				if (a != null && a.getKwh() != null) all += a.getKwh().doubleValue();
			}
		}
		output.setAllKWh(Double.valueOf(all));
		output.setAverageKWh(Double.valueOf(BigDecimal.valueOf(all).divide(BigDecimal.valueOf(dayNum.intValue()), 2, BigDecimal.ROUND_HALF_UP).doubleValue()));
		output.setAllInCome(Double.valueOf(BigDecimal.valueOf(all).multiply(BigDecimal.valueOf(ScreenConstant.MONEY_FORMAT)).divide(BigDecimal.valueOf(10000), 2, BigDecimal.ROUND_HALF_UP).doubleValue()));
		List<KWhAmmeter> today = kWhAmmeterMapper.selectList(new QueryWrapper<KWhAmmeter>().eq("power_date", DateUtil.getToDayDate()));
		double todayKwh = 0.0d;
		if (today != null) {
			for (KWhAmmeter a : today) {
				if (a != null && a.getKwh() != null) todayKwh += a.getKwh().doubleValue();
			}
		}
		output.setTodayInCome(Double.valueOf(BigDecimal.valueOf(todayKwh).multiply(BigDecimal.valueOf(ScreenConstant.MONEY_FORMAT)).divide(BigDecimal.valueOf(10000), 2, BigDecimal.ROUND_HALF_UP).doubleValue()));
		output.setAverageInCome(Double.valueOf(BigDecimal.valueOf(output.getAllInCome().doubleValue()).divide(BigDecimal.valueOf(dayNum.intValue()), 2, BigDecimal.ROUND_HALF_UP).doubleValue()));
		output.setAllReduceCO2(Double.valueOf(BigDecimal.valueOf(all).multiply(BigDecimal.valueOf(ScreenConstant.REDUCE_CO2_FORMAT)).divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP).doubleValue()));
		output.setAllReduceCoal(Double.valueOf(BigDecimal.valueOf(all).multiply(BigDecimal.valueOf(ScreenConstant.REDUCE_COAL_FORMAT)).divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP).doubleValue()));
		output.setAverageReduceCO2(Double.valueOf(BigDecimal.valueOf(output.getAllReduceCO2().doubleValue()).divide(BigDecimal.valueOf(dayNum.intValue()), 2, BigDecimal.ROUND_HALF_UP).doubleValue()));
		output.setAverageReduceCoal(Double.valueOf(BigDecimal.valueOf(output.getAllReduceCoal().doubleValue()).divide(BigDecimal.valueOf(dayNum.intValue()), 2, BigDecimal.ROUND_HALF_UP).doubleValue()));
		output.setAllKWh(Double.valueOf(BigDecimal.valueOf(all).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
		return output;
	}
	@Override public StationMonthPowerOutput stationMonthPower() { return new StationMonthPowerOutput(); }
	@Override public StationTypePowerOutput stationTypePower() { return new StationTypePowerOutput(); }
	@Override public List<StationPowerMonthOutput> kWhStatisticByMonth(Integer id) { return new ArrayList<StationPowerMonthOutput>(); }
	@Override public List<StationPowerAndRadioOutput> stationNextThirtyDayPower(Integer id) { return new ArrayList<StationPowerAndRadioOutput>(); }
	@Override public List<FaultCountOutput> stationFaultCount(Integer id) { return new ArrayList<FaultCountOutput>(); }
	@Override public List<DataAmmeterOutput> ammeter(DataAmmeterInput input) { return new ArrayList<DataAmmeterOutput>(); }
}
