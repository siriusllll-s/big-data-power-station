package qrsoft.information.service;
import qrsoft.information.dto.input.DataAmmeterInput;
import qrsoft.information.dto.output.*;
import java.util.List;
public interface IStationScreenService {
	WeatherOutput latestWeather();
	List<StationPowerAndRadioOutput> stationLastThirtyDayPower(Integer id);
	StationDayAndYearPowerOutput stationDayAndYearPower(Integer id);
	StationAllAndAverOutput stationAllAndAverage();
	StationMonthPowerOutput stationMonthPower();
	StationTypePowerOutput stationTypePower();
	List<StationPowerMonthOutput> kWhStatisticByMonth(Integer id);
	List<StationPowerAndRadioOutput> stationNextThirtyDayPower(Integer id);
	List<FaultCountOutput> stationFaultCount(Integer id);
	List<DataAmmeterOutput> ammeter(DataAmmeterInput input);
}
