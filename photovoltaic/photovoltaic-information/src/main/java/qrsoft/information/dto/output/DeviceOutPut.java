package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
@Data
public class DeviceOutPut implements Serializable {
  private Integer id;
  private String no;
  private String name;
  private Integer type;
  private Integer factory;
  private String factoryName;
  private String deviceAddress;
  private String specifications;
  private String model;
  private String daiId;
  private String installTime;
  private String endTime;
  private String memo;
}
