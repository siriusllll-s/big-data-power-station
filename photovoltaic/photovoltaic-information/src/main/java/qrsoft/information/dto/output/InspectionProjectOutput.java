package qrsoft.information.dto.output;
import lombok.Data;
import qrsoft.common.entity.InspectionProject;
import java.io.Serializable;
@Data
public class InspectionProjectOutput implements Serializable {
  private Integer id;
  private String name;
  public static InspectionProjectOutput of(InspectionProject p) {
    InspectionProjectOutput o = new InspectionProjectOutput();
    if (p == null) return o;
    o.setId(p.getId()); o.setName(p.getName());
    return o;
  }
}
