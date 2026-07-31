package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class WorkOrderOutput implements Serializable {
  private Integer id;
  private String title;
  private Integer status;
  private Integer type;
  private Integer deviceType;
  private String exceptionTime;
  private String forecastTime;
  private String description;
  private List<String> deviceNames = new ArrayList<>();
  private List<String> userNames = new ArrayList<>();
  private List<WorkOrderHistoryOutput> history = new ArrayList<>();
}
