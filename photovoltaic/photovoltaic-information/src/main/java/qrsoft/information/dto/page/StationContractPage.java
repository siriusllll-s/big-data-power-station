package qrsoft.information.dto.page;

import lombok.Data;

import java.io.Serializable;

@Data
public class StationContractPage implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer page = 1;
	private Integer limit = 10;
	/** 合同开始/结束筛选 */
	private String beginDate;
	private String endDate;
	private Integer station;
	private String no;
}
