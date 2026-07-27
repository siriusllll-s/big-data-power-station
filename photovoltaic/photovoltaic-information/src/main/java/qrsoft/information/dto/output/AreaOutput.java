package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.entity.Area;

@Data
public class AreaOutput {
	private Integer id;
	private String areaCode;
	private String area;
	private String cityCode;

	public static AreaOutput entityToOutput(Area a) {
		if (a == null) return null;
		AreaOutput o = new AreaOutput();
		o.setId(a.getId());
		o.setAreaCode(a.getAreaCode());
		o.setArea(a.getArea());
		o.setCityCode(a.getCityCode());
		return o;
	}
}
