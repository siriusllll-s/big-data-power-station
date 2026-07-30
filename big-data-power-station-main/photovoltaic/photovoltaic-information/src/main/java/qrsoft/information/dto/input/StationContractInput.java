package qrsoft.information.dto.input;

import lombok.Data;

import java.io.Serializable;

@Data
public class StationContractInput implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    // 添加占位字段，可根据实际业务扩展
    private String contractName;
}
