package qrsoft.information.dto.page;
import lombok.Data;
import java.io.Serializable;
@Data
public class DevicePage implements Serializable {
  private Integer page = 1;
  private Integer limit = 10;
  private String no;
  private String name;
  private Object type;
  private String installTime;
  private String endTime;
  public Integer typeInt() {
    if (type == null || "".equals(String.valueOf(type))) return null;
    try { return Integer.valueOf(String.valueOf(type)); } catch (Exception e) { return null; }
  }
}
