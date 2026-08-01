package qrsoft.information.shared.dto.vo;

import lombok.Data;
import qrsoft.common.entity.SysAuth;

import java.util.List;

@Data
public class AuthAndMenu {
	private Integer id;
	private String name;
	private String code;
	private Integer type;
	private Integer parentId;
	private String menuUrl;
	private String menuIcon;
	private Integer menuOrder;
	private List<AuthAndMenu> childs;

	public static AuthAndMenu authTOAuth(SysAuth auth) {
		AuthAndMenu vo = new AuthAndMenu();
		vo.setId(auth.getId());
		vo.setName(auth.getName());
		vo.setCode(auth.getCode());
		vo.setType(auth.getType());
		vo.setParentId(auth.getParentId());
		return vo;
	}

	public static AuthAndMenu authTOMenu(SysAuth auth) {
		AuthAndMenu vo = new AuthAndMenu();
		vo.setId(auth.getId());
		vo.setName(auth.getName());
		vo.setCode(auth.getCode());
		vo.setType(auth.getType());
		vo.setParentId(auth.getParentId());
		vo.setMenuUrl(auth.getMenuUrl());
		vo.setMenuIcon(auth.getMenuIcon());
		vo.setMenuOrder(auth.getMenuOrder());
		return vo;
	}
}
