package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import qrsoft.common.constant.BaseConstant;
import java.io.Serializable;
import java.util.Date;
@Data
@TableName("k_wh_station")
public class KWhStation implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	@TableField("station")
	private Integer station = BaseConstant.STATION;
	@TableField("radiation")
	private Double radiation;
	@TableField("power_ratio")
	private Double powerRatio;
	@TableField("power_date")
	private Date powerDate;
	@TableField("kwh")
	private Double kwh;
}
