package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
@Data
public class InspectionItemOutput implements Serializable {
  private Integer id;
  private String name;
  private Integer contentId;
}
