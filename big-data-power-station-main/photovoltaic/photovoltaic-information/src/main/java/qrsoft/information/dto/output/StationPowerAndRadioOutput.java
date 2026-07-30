package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.entity.KWhStation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Data
public class StationPowerAndRadioOutput {
	/** 日期字符串 yyyy-MM-dd（前端 powerDate.substr） */
	private String powerDate;
	/** 发电量 kWh */
	private Double kwh;
	/** 发电效率 % */
	private Double powerRatio;

	/** 兼容旧字段 */
	private String date;
	private Double power;
	private Double ratio;

	public static StationPowerAndRadioOutput entityToOutput(KWhStation e) {
		StationPowerAndRadioOutput o = new StationPowerAndRadioOutput();
		if (e == null) {
			return o;
		}
		if (e.getPowerDate() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			String d = sdf.format(e.getPowerDate());
			o.setPowerDate(d);
			o.setDate(d);
		}
		Double kwh = e.getKwh() == null ? 0D : BigDecimal.valueOf(e.getKwh()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		Double ratio = e.getPowerRatio() == null ? 0D : BigDecimal.valueOf(e.getPowerRatio()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		o.setKwh(kwh);
		o.setPower(kwh);
		o.setPowerRatio(ratio);
		o.setRatio(ratio);
		return o;
	}
}
