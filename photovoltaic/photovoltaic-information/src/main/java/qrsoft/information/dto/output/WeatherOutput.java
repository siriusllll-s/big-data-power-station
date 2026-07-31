package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.entity.Weather;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Data
public class WeatherOutput {
	private Double irradiance;
	private Double ambientTemperature;
	private Double batteryPanelTemperature;
	private Double windSpeed;
	private Double windDirection;
	private String createTime;

	public static WeatherOutput entityToOutput(Weather e) {
		WeatherOutput o = new WeatherOutput();
		if (e == null) {
			return o;
		}
		o.setIrradiance(e.getIrradiance());
		o.setAmbientTemperature(e.getAmbientTemperature());
		o.setBatteryPanelTemperature(e.getBatteryPanelTemperature());
		o.setWindSpeed(e.getWindSpeed());
		o.setWindDirection(e.getWindDirection());
		if (e.getWeatherTime() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			o.setCreateTime(sdf.format(e.getWeatherTime()));
		}
		return o;
	}
}
