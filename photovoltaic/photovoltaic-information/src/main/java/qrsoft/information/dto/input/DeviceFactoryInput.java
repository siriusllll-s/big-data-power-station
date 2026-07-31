package qrsoft.information.dto.input;
import lombok.Data;
import java.io.Serializable;
@Data
public class DeviceFactoryInput implements Serializable {
  private Integer id;
  private String name;
  private String address;
  private String person;
  private String personTel;
  private String memo;
}
