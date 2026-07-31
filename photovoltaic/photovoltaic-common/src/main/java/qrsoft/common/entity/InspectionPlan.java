package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
@Data
@TableName("inspection_plan")
public class InspectionPlan implements Serializable {
  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.AUTO) private Integer id;
  private String name;
  private Date beginDate;
  private Date endDate;
  private Integer station;
  private String memo;
  private Integer delFlag;
}
