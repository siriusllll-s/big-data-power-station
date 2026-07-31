package qrsoft.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("station_solar_price")
public class StationSolarPrice implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Integer id;
	/** 所属电站 id */
	private Integer station;
	/** 电价（元） */
	private Double price;
	/** 实施日期 */
	private Date beginDate;
	/** 备注 */
	private String memo;
	/** 0 正常 1 删除 */
	private Integer delFlag;
}
