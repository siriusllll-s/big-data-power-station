package qrsoft.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("station_contract")
public class StationContract implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Integer id;
	/** 所属电站 id */
	private Integer station;
	/** 合同编号 */
	private String no;
	/** 合同开始日期 */
	private Date beginDate;
	/** 合同结束日期 */
	private Date endDate;
	/** 合同期电量 kWh */
	private Double contractPower;
	/** 协议效能比 % */
	private Double protocolPr;
	/** 模拟发电效率 % */
	private Double efficiency;
	/** 预计年均辐照值 Wh/㎡ */
	private Double avgRadio;
	/** 备注 */
	private String memo;
	/** 0 正常 1 删除 */
	private Integer delFlag;
}
