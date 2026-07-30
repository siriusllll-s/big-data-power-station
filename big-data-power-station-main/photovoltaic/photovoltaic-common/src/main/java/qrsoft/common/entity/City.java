package qrsoft.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("city")
public class City {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private String cityCode;
	private String city;
	private String provinceCode;
}
