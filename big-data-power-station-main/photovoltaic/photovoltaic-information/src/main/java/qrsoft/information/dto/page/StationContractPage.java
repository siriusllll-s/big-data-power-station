package qrsoft.information.dto.page;

import lombok.Data;
import java.io.Serializable;

@Data
public class StationContractPage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer pageNo = 1;
    private Integer pageSize = 10;
    // 查询参数占位
    private String keyword;
}
