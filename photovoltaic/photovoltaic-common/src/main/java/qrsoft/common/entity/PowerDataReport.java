package qrsoft.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 电站运行日报
 */
@Data
@TableName("power_data_report")
public class PowerDataReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Integer id;
	/** 电站 id */
	private Integer station;
	/** 日报日期 */
	private Date reportDate;
	/** 天气描述 */
	private String weather;
	/** 当日发电量 kWh */
	private Double kwh;
	/** 当日辐照量 kWh/㎡ */
	private Double radiation;
	/** 发电效率 % */
	private Double powerRatio;
	/** 运行总结 */
	private String summary;
	/** 创建时间 */
	private Date createTime;
	/** 更新时间 */
	private Date updateTime;
}
