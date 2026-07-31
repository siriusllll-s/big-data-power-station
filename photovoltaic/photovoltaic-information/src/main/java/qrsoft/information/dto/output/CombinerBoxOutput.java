package qrsoft.information.dto.output;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
@Data
public class CombinerBoxOutput implements Serializable {
  private String name;
  private String createTime;
  private List<String> combinerBoxIns;
  private String combinerBox;
}
