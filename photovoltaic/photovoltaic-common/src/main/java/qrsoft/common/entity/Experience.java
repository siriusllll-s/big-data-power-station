package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
@Data
@TableName("experience")
public class Experience implements Serializable {
  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.AUTO) private Integer id;
  private String title;
  private String deviceType;
  private String content;
  private Date createTime;
  private Date updateTime;
  private Integer delFlag;
}
