package qrsoft.information.dto.input;
import lombok.Data;
import java.io.Serializable;
@Data
public class DeviceInput implements Serializable {
  private Integer id;
  private String no;
  private String name;
  private Object type;
  private Object factory;
  private String deviceAddress;
  private String specifications;
  private String model;
  private String daiId;
  private String installTime;
  private String endTime;
  private String memo;
  public Integer typeInt() { return toInt(type); }
  public Integer factoryInt() { return toInt(factory); }
  private Integer toInt(Object v) {
    if (v == null || "".equals(String.valueOf(v))) return null;
    try { return Integer.valueOf(String.valueOf(v)); } catch (Exception e) { return null; }
  }
}
