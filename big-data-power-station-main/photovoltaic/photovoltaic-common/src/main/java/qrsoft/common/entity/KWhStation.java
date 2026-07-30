package qrsoft.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("k_wh_station")
public class KWhStation {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private Integer station;
	private Double kwh;
	private Double radiation;
	private Double powerRatio;
	private Date powerDate;
}
