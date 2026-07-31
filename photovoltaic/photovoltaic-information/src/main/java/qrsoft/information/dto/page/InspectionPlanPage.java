package qrsoft.information.dto.page;
import lombok.Data;
import java.io.Serializable;
@Data
public class InspectionPlanPage implements Serializable {
  private Integer page = 1;
  private Integer limit = 10;
  private String planName;
  private String beginDate;
  private String endDate;
}
