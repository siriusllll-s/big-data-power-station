package qrsoft.information.dto.input;

import lombok.Data;

import java.io.Serializable;

@Data
public class StationSolarPriceInput implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 电站 id */
	private Integer station;
	/** 电价（元） */
	private Double price;
	/** 实施日期 yyyy-MM-dd */
	private String beginDate;
	/** 备注 */
	private String memo;
	/** 前端展示用电站名称（忽略入库） */
	private String name;
}
