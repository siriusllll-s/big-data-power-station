package qrsoft.information.dto.output;

import lombok.Data;

@Data
public class StationDayAndYearPowerOutput {
	/** 今日发电量 kWh */
	private Double dayKWh;
	/** 今年发电量 kWh */
	private Double yearKWh;
	/** 今日节煤量 kg */
	private Double dayReduceCoal;
	/** 今年节煤量 kg */
	private Double yearReduceCoal;
	/** 今日 CO2 减排 kg */
	private Double dayReduceCO2;
	/** 今年 CO2 减排 kg */
	private Double yearReduceCO2;

	/** 兼容旧字段 */
	private Double dayPower;
	private Double yearPower;
	private Double coalSave;
	private Double co2Reduce;
}
