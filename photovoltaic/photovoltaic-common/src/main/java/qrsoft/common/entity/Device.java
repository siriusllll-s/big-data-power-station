package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
@Data
@TableName("device")
public class Device implements Serializable {
  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.AUTO) private Integer id;
  private String no;
  private String name;
  private Integer type;
  private Integer factory;
  private String deviceAddress;
  private String specifications;
  private String model;
  private String daiId;
  private Date installTime;
  private Date endTime;
  private String memo;
  private Integer station;
  private Integer delFlag;
}
