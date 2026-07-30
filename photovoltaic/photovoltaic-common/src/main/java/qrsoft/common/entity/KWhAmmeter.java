package qrsoft.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("k_wh_ammeter")
public class KWhAmmeter {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private Date powerDate;
	private Double kwh;
	private String ammeter;
	private Double endKwh;
}
