package qrsoft.information.dto.input;

import lombok.Data;
import java.io.Serializable;

@Data
public class StationSolarPriceInput implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String stationCode;
    private Double price;
}
