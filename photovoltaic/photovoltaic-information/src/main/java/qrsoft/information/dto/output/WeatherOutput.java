package qrsoft.information.dto.output;

import lombok.Data;

@Data
public class WeatherOutput {
	private Double irradiance;
	private Double ambientTemperature;
	private Double batteryPanelTemperature;
	private Double windSpeed;
	private Double windDirection;
	private String createTime;
}
