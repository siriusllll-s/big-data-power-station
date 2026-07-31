package qrsoft.information.service;

import qrsoft.information.dto.input.StationSolarPriceInput;
import qrsoft.information.dto.output.StationSolarPriceOutput;
import qrsoft.information.dto.page.StationSolarPricePage;
import qrsoft.information.dto.vo.ResultPage;

public interface IStationSolarPriceService {
    void saveOrUpdate(StationSolarPriceInput input);

    ResultPage<StationSolarPriceOutput> pageByParam(StationSolarPricePage page);

    StationSolarPriceOutput detail(Integer id);

    void delete(Integer id);
}
