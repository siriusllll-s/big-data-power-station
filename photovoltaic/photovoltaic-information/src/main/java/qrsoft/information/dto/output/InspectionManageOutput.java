package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
@Data
public class InspectionManageOutput implements Serializable {
  private Integer id;
  private String name;
  private Integer status;
  private Integer planId;
  private Integer pointId;
}
