package qrsoft.information.dto.output;

import lombok.Data;

@Data
public class FaultCountOutput {
	private String deviceName;
	private Integer faultCount;
}
