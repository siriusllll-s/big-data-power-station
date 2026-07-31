package qrsoft.information.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import qrsoft.common.entity.StationSolarPrice;
import qrsoft.common.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

@Data
public class StationSolarPriceOutput implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer station;
	private Double price;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	private Date beginDate;
	private String memo;
	private StationSimpleOutput stationObj;

	public static StationSolarPriceOutput entityToOutput(StationSolarPrice e) {
		StationSolarPriceOutput o = new StationSolarPriceOutput();
		if (e == null) {
			return o;
		}
		o.setId(e.getId());
		o.setStation(e.getStation());
		o.setPrice(e.getPrice());
		o.setBeginDate(e.getBeginDate());
		o.setMemo(e.getMemo());
		return o;
	}

	public String getBeginDateStr() {
		if (beginDate == null) {
			return null;
		}
		return DateUtil.dateToString(beginDate, DateUtil.YYMMDD);
	}
}
