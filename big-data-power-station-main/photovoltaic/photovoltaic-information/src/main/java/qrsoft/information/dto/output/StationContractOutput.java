package qrsoft.information.dto.output;

import lombok.Data;

import java.io.Serializable;

@Data
public class StationContractOutput implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String contractName;
}
