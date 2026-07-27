package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.entity.SysUser;

@Data
public class SysUserSimpleOutput {
	private Integer id;
	private String no;
	private String name;
	private String trueName;
	private String phone;
	private String eMail;

	public static SysUserSimpleOutput entityToOutput(SysUser u) {
		if (u == null) return null;
		SysUserSimpleOutput o = new SysUserSimpleOutput();
		o.setId(u.getId());
		o.setNo(u.getNo());
		o.setName(u.getName());
		o.setTrueName(u.getTrueName());
		o.setPhone(u.getPhone());
		o.setEMail(u.getEMail());
		return o;
	}
}
