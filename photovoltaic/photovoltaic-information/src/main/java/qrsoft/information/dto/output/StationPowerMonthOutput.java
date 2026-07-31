package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.util.DateUtil;

import java.util.Date;

@Data
public class StationPowerMonthOutput {
	private String month;
	private Double power;
	private Date powerDate;
	private Double kwh;
	private String powerDateStr;

	public String getPowerDate() {
		if (powerDate == null) {
			return powerDateStr != null ? powerDateStr : "";
		}
		try {
			return DateUtil.dateToString(powerDate, DateUtil.YYMM);
		} catch (Exception e) {
			return powerDateStr != null ? powerDateStr : "";
		}
	}
}
