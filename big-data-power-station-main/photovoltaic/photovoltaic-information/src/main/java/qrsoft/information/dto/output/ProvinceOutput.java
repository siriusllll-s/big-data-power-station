package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.entity.Province;

@Data
public class ProvinceOutput {
	private Integer id;
	private String provinceCode;
	private String province;

	public static ProvinceOutput entityToOutput(Province p) {
		if (p == null) return null;
		ProvinceOutput o = new ProvinceOutput();
		o.setId(p.getId());
		o.setProvinceCode(p.getProvinceCode());
		o.setProvince(p.getProvince());
		return o;
	}
}
