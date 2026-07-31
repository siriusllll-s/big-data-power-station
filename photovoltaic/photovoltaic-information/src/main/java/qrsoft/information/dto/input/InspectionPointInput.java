package qrsoft.information.dto.input;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class InspectionPointInput implements Serializable {
  private Integer id;
  private String name;
  private Integer projectId;
  private Integer project;
  private String memo;
  private List<Integer> itemIds = new ArrayList<>();
  private List<String> deviceNames = new ArrayList<>();
  private Integer deviceType;
  public Integer resolveProjectId() {
    return projectId != null ? projectId : project;
  }
}
