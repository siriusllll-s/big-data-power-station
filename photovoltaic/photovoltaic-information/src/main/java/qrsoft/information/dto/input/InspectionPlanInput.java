package qrsoft.information.dto.input;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class InspectionPlanInput implements Serializable {
  private Integer id;
  private String name;
  private String beginDate;
  private String endDate;
  private String memo;
  private List<Integer> pointIds = new ArrayList<>();
  private List<Integer> userIds = new ArrayList<>();
}
