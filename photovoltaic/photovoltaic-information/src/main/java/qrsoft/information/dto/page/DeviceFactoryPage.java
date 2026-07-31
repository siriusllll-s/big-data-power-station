package qrsoft.information.dto.page;
import lombok.Data;
import java.io.Serializable;
@Data
public class DeviceFactoryPage implements Serializable {
  private Integer page = 1;
  private Integer limit = 10;
  private String name;
  private String person;
}
