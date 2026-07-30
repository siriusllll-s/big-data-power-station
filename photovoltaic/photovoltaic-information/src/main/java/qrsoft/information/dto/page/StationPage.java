package qrsoft.information.dto.page;

import lombok.Data;

@Data
public class StationPage {
	private Integer page = 1;
	private Integer limit = 10;
	private String name;
	private Integer type;
	private Integer status;
	private String own;
	private String maintainPerson;
}
