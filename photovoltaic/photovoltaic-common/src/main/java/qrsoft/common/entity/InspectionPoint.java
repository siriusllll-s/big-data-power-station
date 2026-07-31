package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
@Data
@TableName("inspection_point")
public class InspectionPoint implements Serializable {
  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.AUTO) private Integer id;
  private String name;
  private Integer projectId;
  private Integer station;
  private String memo;
  private Integer delFlag;
}
