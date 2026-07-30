package qrsoft.information.dto.vo;

import lombok.Data;

import java.util.List;

@Data
public class TokenVO {
	private Integer id;
	private String name;
	private String trueName;
	private Integer type;
	private List<AuthAndMenu> authList;
	private List<AuthAndMenu> menuList;
}
