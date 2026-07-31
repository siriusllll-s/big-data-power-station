package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
@Data
public class InverterOutput implements Serializable {
  private String name;
  private String createTime;
  private Double dailyPower;
  private Double dc;
  private Double dcPower;
  private Double dcVoltage;
}
