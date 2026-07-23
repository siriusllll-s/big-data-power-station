package qrsoft.information.dto.output;
import lombok.Data;
@Data
public class StationPowerMonthOutput {
	private Double irradiance;
	private Double ambientTemperature;
	private Double batteryPanelTemperature;
	private Double windSpeed;
	private Double windDirection;
	private Double allKWh;
	private Double powerRatio;
	private String powerDate;
	private String name;
	private String ammeter;
	private String createTime;
	private Integer count;
	private String deviceType;
}
