package qrsoft.information.dto.output;

import lombok.Data;

import java.util.Date;

@Data
public class DataAmmeterOutput {
	private Integer station;
	private String name;
	private Double ammeter;
	private String inverter;
	private Date createTime;
}
