package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
@Data
public class MeterOutput implements Serializable {
  private String name;
  private String createTime;
  private Double dailyPower;
}
