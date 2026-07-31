package qrsoft.information.dto.page;
import lombok.Data;
import java.io.Serializable;
@Data
public class WorkOrderPage implements Serializable {
  private Integer page = 1;
  private Integer limit = 10;
  private String beginDate;
  private String endDate;
  private Object status;
  private Object type;
  private Object deviceType;
  private String deviceName;
  private String userName;

  public Integer statusInt() { return toInt(status); }
  public Integer typeInt() { return toInt(type); }
  public Integer deviceTypeInt() { return toInt(deviceType); }
  private Integer toInt(Object v) {
    if (v == null || "".equals(String.valueOf(v))) return null;
    try { return Integer.valueOf(String.valueOf(v)); } catch (Exception e) { return null; }
  }
}
