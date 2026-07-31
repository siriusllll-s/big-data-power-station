package qrsoft.common.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
@Data
@TableName("device_factory")
public class DeviceFactory implements Serializable {
  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.AUTO) private Integer id;
  private String name;
  private String address;
  private String person;
  private String personTel;
  private String memo;
  private Integer delFlag;
}
