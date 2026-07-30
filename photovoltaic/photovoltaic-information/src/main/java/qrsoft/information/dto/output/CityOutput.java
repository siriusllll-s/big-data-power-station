package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.entity.City;

@Data
public class CityOutput {
	private Integer id;
	private String cityCode;
	private String city;
	private String provinceCode;

	public static CityOutput entityToOutput(City c) {
		if (c == null) return null;
		CityOutput o = new CityOutput();
		o.setId(c.getId());
		o.setCityCode(c.getCityCode());
		o.setCity(c.getCity());
		o.setProvinceCode(c.getProvinceCode());
		return o;
	}
}
