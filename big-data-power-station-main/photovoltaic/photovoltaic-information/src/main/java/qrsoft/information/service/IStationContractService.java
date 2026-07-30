package qrsoft.information.service;

import qrsoft.information.dto.input.StationContractInput;
import qrsoft.information.dto.output.StationContractOutput;
import qrsoft.information.dto.page.StationContractPage;
import qrsoft.information.dto.vo.ResultPage;

public interface IStationContractService {
    void saveOrUpdate(StationContractInput input);

    ResultPage<StationContractOutput> pageByParam(StationContractPage page);

    StationContractOutput detail(Integer id);

    void delete(Integer id);
}
