package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
@Data
@TableName("fault")
public class Fault implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	@TableField("station")
	private Integer station;
	@TableField("device_name")
	private String deviceName;
	@TableField("device_type")
	private String deviceType;
	@TableField("fault_desc")
	private String faultDesc;
	@TableField("fault_level")
	private Integer faultLevel;
	@TableField("fault_time")
	private Date faultTime;
}
