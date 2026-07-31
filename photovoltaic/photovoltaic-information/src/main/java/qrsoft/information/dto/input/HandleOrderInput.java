package qrsoft.information.dto.input;
import lombok.Data;
import java.io.Serializable;
@Data
public class HandleOrderInput implements Serializable {
  private Integer id;
  private Integer status;
  private String handleDesc;
  private String handleUser;
}
