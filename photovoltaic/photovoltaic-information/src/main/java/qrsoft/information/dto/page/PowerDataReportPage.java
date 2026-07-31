package qrsoft.information.dto.page;

import lombok.Data;

import java.io.Serializable;

@Data
public class PowerDataReportPage implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 页码 */
	private Integer page = 1;
	/** 每页条数 */
	private Integer limit = 10;
	/** 开始日期 yyyy-MM-dd */
	private String start;
	/** 结束日期 yyyy-MM-dd */
	private String end;
	/** 电站 id（可选） */
	private Integer station;
}
