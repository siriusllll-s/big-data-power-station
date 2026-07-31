package qrsoft.information.dto.input;

import lombok.Data;
import qrsoft.common.entity.PowerDataReport;

import java.io.Serializable;

@Data
public class PowerDataReportInput implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 天气（可编辑） */
	private String weather;
	/** 总结（可编辑） */
	private String summary;
	/** 以下字段编辑页禁用，但仍可能回传 */
	private String reportDate;
	private Double kwh;
	private Double radiation;
	private Double powerRatio;

	public static void inputToEntity(PowerDataReportInput input, PowerDataReport entity) {
		if (input == null || entity == null) {
			return;
		}
		if (input.getWeather() != null) {
			entity.setWeather(input.getWeather());
		}
		if (input.getSummary() != null) {
			entity.setSummary(input.getSummary());
		}
	}
}
