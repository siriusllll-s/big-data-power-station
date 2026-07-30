package qrsoft.information.dto.page;

import lombok.Data;

import java.io.Serializable;

@Data
public class StationSolarPricePage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String stationCode;
}
