package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
@Data
public class WorkOrderHistoryOutput implements Serializable {
  private Integer status;
  private String handleDesc;
  private String handleUser;
  private String handleTime;
}
