package qrsoft.information.dto.page;

import lombok.Data;

import java.io.Serializable;

@Data
public class StationSolarPricePage implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 页码（前端 form.page） */
	private Integer page = 1;
	/** 每页条数（前端 form.limit） */
	private Integer limit = 10;
	/** 实施日期起 yyyy-MM-dd */
	private String start;
	/** 实施日期止 yyyy-MM-dd */
	private String end;
	/** 电站 id 过滤 */
	private Integer station;
}
