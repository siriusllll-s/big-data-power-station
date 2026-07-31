package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
@Data
@TableName("threshold")
public class Threshold implements Serializable {
  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.AUTO) private Integer id;
  private Integer classification;
  private Integer type;
  private Integer level;
  private Integer cycle;
  private Integer startTime;
  private Integer endTime;
  private Integer isEnable;
  private String memo;
  private Integer station;
  private Integer delFlag;
}
