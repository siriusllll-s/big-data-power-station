package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
@Data
@TableName("weather")
public class Weather implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	@TableField("station")
	private Integer station;
	@TableField("weather_name")
	private String weatherName;
	@TableField("weather_time")
	private Date weatherTime;
	@TableField("irradiance")
	private Double irradiance;
	@TableField("ambient_temperature")
	private Double ambientTemperature;
	@TableField("battery_panel_temperature")
	private Double batteryPanelTemperature;
	@TableField("wind_speed")
	private Double windSpeed;
	@TableField("wind_direction")
	private Double windDirection;
}
