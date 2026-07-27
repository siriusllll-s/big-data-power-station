package qrsoft.information.dto.output;

import lombok.Data;

@Data
public class StationAllAndAverOutput {
	private Double allKWh;
	private Double averageKWh;
	private Double allInCome;
	private Double todayInCome;
	private Double averageInCome;
}
