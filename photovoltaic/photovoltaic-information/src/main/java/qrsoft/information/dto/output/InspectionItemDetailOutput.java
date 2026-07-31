package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class InspectionItemDetailOutput implements Serializable {
  private Integer contentId;
  private String contentName;
  private List<InspectionItemOutput> items = new ArrayList<>();
}
