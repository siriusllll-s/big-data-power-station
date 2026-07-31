package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
@Data
public class InspectionPlanOutput implements Serializable {
  private Integer id;
  private String name;
  private String beginDate;
  private String endDate;
  private String memo;
}
