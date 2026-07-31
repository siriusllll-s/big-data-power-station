package qrsoft.information.dto.output;
import lombok.Data;
import qrsoft.common.entity.Threshold;
import java.io.Serializable;
@Data
public class ThresholdOutput implements Serializable {
  private Integer id;
  private Integer classification;
  private Integer type;
  private Integer level;
  private Integer cycle;
  private Integer startTime;
  private Integer endTime;
  private Integer isEnable;
  private String memo;

  public static ThresholdOutput entityToOutput(Threshold e) {
    ThresholdOutput o = new ThresholdOutput();
    if (e == null) return o;
    o.setId(e.getId());
    o.setClassification(e.getClassification());
    o.setType(e.getType());
    o.setLevel(e.getLevel());
    o.setCycle(e.getCycle());
    o.setStartTime(e.getStartTime());
    o.setEndTime(e.getEndTime());
    o.setIsEnable(e.getIsEnable());
    o.setMemo(e.getMemo());
    return o;
  }
}
