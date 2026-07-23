package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
@Data
@TableName("k_wh_inverter")
public class KWhInverter implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	@TableField("power_date")
	private Date powerDate;
	@TableField("kwh")
	private Double kwh;
	@TableField("inverter")
	private String inverter;
	@TableField("ammeter")
	private String ammeter;
}
