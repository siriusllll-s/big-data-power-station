package qrsoft.information.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import qrsoft.common.entity.PowerDataReport;
import qrsoft.common.util.DateUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PowerDataReportOutput implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer station;
	/** 前端表格用字符串日期 */
	private String reportDate;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	private Date reportDateRaw;
	private String weather;
	private Double kwh;
	private Double radiation;
	private Double powerRatio;
	private String summary;

	public static PowerDataReportOutput entityToOutput(PowerDataReport e) {
		PowerDataReportOutput o = new PowerDataReportOutput();
		if (e == null) {
			return o;
		}
		o.setId(e.getId());
		o.setStation(e.getStation());
		o.setReportDateRaw(e.getReportDate());
		if (e.getReportDate() != null) {
			o.setReportDate(DateUtil.dateToString(e.getReportDate(), DateUtil.YYMMDD));
		}
		o.setWeather(e.getWeather());
		o.setKwh(scale(e.getKwh()));
		o.setRadiation(scale(e.getRadiation()));
		o.setPowerRatio(scale(e.getPowerRatio()));
		o.setSummary(e.getSummary());
		return o;
	}

	private static Double scale(Double v) {
		if (v == null) {
			return null;
		}
		return BigDecimal.valueOf(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
