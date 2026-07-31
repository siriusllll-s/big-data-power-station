package qrsoft.information.dto.page;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
@Data
public class MonitorPage implements Serializable {
  private Integer page = 1;
  private Integer limit = 10;
  private Integer dateType;
  private Integer type;
  private List<String> devices;
  private String startDate;
  private String endDate;
}
