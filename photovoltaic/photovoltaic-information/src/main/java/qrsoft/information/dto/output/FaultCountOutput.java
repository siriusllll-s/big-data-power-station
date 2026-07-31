package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.entity.FaultCount;

@Data
public class FaultCountOutput {
	private String deviceName;
	private Integer faultCount;
	private String type;
	private Integer count;

	public static FaultCountOutput entityToOutput(FaultCount e) {
		FaultCountOutput o = new FaultCountOutput();
		if (e == null) {
			return o;
		}
		o.setDeviceName(e.getDeviceName());
		o.setFaultCount(e.getFaultCount());
		o.setType(e.getDeviceName());
		o.setCount(e.getFaultCount());
		return o;
	}
}
