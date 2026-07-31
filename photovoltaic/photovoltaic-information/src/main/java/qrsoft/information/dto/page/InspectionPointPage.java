package qrsoft.information.dto.page;
import lombok.Data;
import java.io.Serializable;
@Data
public class InspectionPointPage implements Serializable {
  private Integer page = 1;
  private Integer limit = 10;
  /** 前端可能传 ""，用 Object 兼容 */
  private Object project;
  private String pointName;

  public Integer projectId() {
    if (project == null || "".equals(String.valueOf(project))) {
      return null;
    }
    try {
      return Integer.valueOf(String.valueOf(project));
    } catch (Exception e) {
      return null;
    }
  }
}
