package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class InspectionPlanDetailOutput implements Serializable {
  private Integer id;
  private String name;
  private String beginDate;
  private String endDate;
  private String memo;
  private List<String> pointNames = new ArrayList<>();
  private List<String> userNames = new ArrayList<>();
}
