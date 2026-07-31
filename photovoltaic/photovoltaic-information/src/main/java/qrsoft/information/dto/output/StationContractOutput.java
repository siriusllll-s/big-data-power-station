package qrsoft.information.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import qrsoft.common.entity.StationContract;

import java.io.Serializable;
import java.util.Date;

@Data
public class StationContractOutput implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer station;
	private String no;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	private Date beginDate;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	private Date endDate;
	private Double contractPower;
	private Double protocolPr;
	private Double efficiency;
	private Double avgRadio;
	private String memo;
	private StationSimpleOutput stationObj;

	public static StationContractOutput entityToOutput(StationContract e) {
		StationContractOutput o = new StationContractOutput();
		if (e == null) {
			return o;
		}
		o.setId(e.getId());
		o.setStation(e.getStation());
		o.setNo(e.getNo());
		o.setBeginDate(e.getBeginDate());
		o.setEndDate(e.getEndDate());
		o.setContractPower(e.getContractPower());
		o.setProtocolPr(e.getProtocolPr());
		o.setEfficiency(e.getEfficiency());
		o.setAvgRadio(e.getAvgRadio());
		o.setMemo(e.getMemo());
		return o;
	}
}
