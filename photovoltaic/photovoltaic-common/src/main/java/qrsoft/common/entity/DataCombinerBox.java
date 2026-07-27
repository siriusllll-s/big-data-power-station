package qrsoft.common.entity;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
@Data
public class DataCombinerBox implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer station;
	private String ammeterName;
	private String inverterName;
	private String dcCabinetName;
	private String name;
	private String createTime;
	private List<String> combinerBoxIns;
	private String combinerBox;
	private List<String> dcCabinetIns;
	private String dcCabinet;
	private String inverter;
	private String ammeter;
	private Double irradiance;
	private Double ambientTemperature;
	private Double batteryPanelTemperature;
	private Double windSpeed;
	private Double windDirection;
}
