package qrsoft.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_auth")
public class SysAuth {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private String name;
	private String code;
	private Integer type;
	@TableField("parent_id")
	private Integer parentId;
	@TableField("menu_url")
	private String menuUrl;
	@TableField("menu_icon")
	private String menuIcon;
	@TableField("menu_order")
	private Integer menuOrder;
	@TableField("del_flag")
	private Integer delFlag;
}
