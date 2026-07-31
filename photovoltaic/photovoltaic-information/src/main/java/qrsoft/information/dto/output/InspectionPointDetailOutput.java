package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class InspectionPointDetailOutput implements Serializable {
  private Integer id;
  private String name;
  private Integer projectId;
  private String projectName;
  private String memo;
  private List<String> itemNames = new ArrayList<>();
  private List<String> deviceNames = new ArrayList<>();
}
