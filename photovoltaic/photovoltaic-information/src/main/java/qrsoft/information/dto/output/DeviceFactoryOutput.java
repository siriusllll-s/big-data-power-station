package qrsoft.information.dto.output;
import lombok.Data;
import qrsoft.common.entity.DeviceFactory;
import java.io.Serializable;
@Data
public class DeviceFactoryOutput implements Serializable {
  private Integer id;
  private String name;
  private String address;
  private String person;
  private String personTel;
  private String memo;
  public static DeviceFactoryOutput of(DeviceFactory e) {
    DeviceFactoryOutput o = new DeviceFactoryOutput();
    if (e == null) return o;
    o.setId(e.getId()); o.setName(e.getName()); o.setAddress(e.getAddress());
    o.setPerson(e.getPerson()); o.setPersonTel(e.getPersonTel()); o.setMemo(e.getMemo());
    return o;
  }
}
