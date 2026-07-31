package qrsoft.information.dto.page;
import lombok.Data;
import java.io.Serializable;
@Data
public class ThresholdPage implements Serializable {
  private Integer page = 1;
  private Integer limit = 10;
  private Object classification;
  private Object type;
  public Integer classificationInt() { return toInt(classification); }
  public Integer typeInt() { return toInt(type); }
  private Integer toInt(Object v) {
    if (v == null || "".equals(String.valueOf(v))) return null;
    try { return Integer.valueOf(String.valueOf(v)); } catch (Exception e) { return null; }
  }
}
