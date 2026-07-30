package qrsoft.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("area")
public class Area {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private String areaCode;
	private String area;
	private String cityCode;
}
