package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
@Data
public class DeviceRealTimeOutput implements Serializable {
  private String date;
  private String name;
  private Double value;
}
