package qrsoft.information.dto.input;

import lombok.Data;

import java.io.Serializable;

@Data
public class StationContractInput implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 电站 id */
	private Integer station;
	/** 合同编号 */
	private String no;
	/** 开始 yyyy-MM-dd */
	private String beginDate;
	/** 结束 yyyy-MM-dd */
	private String endDate;
	/** 合同期电量 kWh */
	private Double contractPower;
	/** 协议效能比 % */
	private Double protocolPr;
	/** 模拟发电效率 % */
	private Double efficiency;
	/** 预计年均辐照值 */
	private Double avgRadio;
	/** 备注 */
	private String memo;
}
