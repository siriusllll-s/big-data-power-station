package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
@Data
@TableName("work_order")
public class WorkOrder implements Serializable {
  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.AUTO) private Integer id;
  private String title;
  private Integer station;
  private Integer status;
  private Integer type;
  private Integer deviceType;
  private Date exceptionTime;
  private Date forecastTime;
  private String description;
  private Date createTime;
  private Date updateTime;
  private Integer delFlag;
}
