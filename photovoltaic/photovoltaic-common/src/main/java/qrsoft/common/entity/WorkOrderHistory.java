package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
@Data
@TableName("work_order_history")
public class WorkOrderHistory implements Serializable {
  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.AUTO) private Integer id;
  private Integer orderId;
  private Integer status;
  private String handleDesc;
  private String handleUser;
  private Date handleTime;
}
