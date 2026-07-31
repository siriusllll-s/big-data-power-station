package qrsoft.information.dto.input;
import lombok.Data;
import java.io.Serializable;
@Data
public class ThresholdInput implements Serializable {
  private Integer id;
  private Integer classification;
  private Integer type;
  private Integer level;
  private Integer cycle;
  private Integer startTime;
  private Integer endTime;
  private Integer isEnable;
  private String memo;
}
