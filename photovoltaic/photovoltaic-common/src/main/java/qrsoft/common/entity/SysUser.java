package qrsoft.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
public class SysUser {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private String no;
	private String name;
	private String password;
	@TableField("true_name")
	private String trueName;
	private String phone;
	@TableField("e_mail")
	private String eMail;
	private Integer type;
	private String memo;
	@TableField("del_flag")
	private Integer delFlag;
}
