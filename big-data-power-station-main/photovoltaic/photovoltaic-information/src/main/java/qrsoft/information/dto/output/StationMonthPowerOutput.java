package qrsoft.information.dto.output;

import lombok.Data;

@Data
public class StationMonthPowerOutput {
	/** 本月发电量 */
	private Double allKWh;
	/** 本月发电效率 */
	private Double powerRatio;
	/** 电站简略信息 */
	private StationSimpleOutput stationInfo;

	/** 兼容旧字段 */
	private Double monthPower;
	private Double monthRatio;
}
