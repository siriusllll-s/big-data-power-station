package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
@Data
public class StationDailyPowerOutput implements Serializable {
  private Double inPower;
  private Double lossKwh;
  private Double outPower;
  private String powerDate;
  private Double powerRatio;
}
