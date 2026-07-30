package qrsoft.information.service;

import qrsoft.information.dto.input.StationInput;
import qrsoft.information.dto.input.StationPhotoInput;
import qrsoft.information.dto.output.StationOutput;
import qrsoft.information.dto.output.StationSimpleOutput;
import qrsoft.information.dto.page.StationPage;
import qrsoft.information.dto.vo.ResultPage;

public interface IStationService {

	void saveOrUpdate(StationInput input);

	void savePhoto(StationPhotoInput input);

	ResultPage<StationSimpleOutput> pageByParam(StationPage input);

	/**
	 * 电站信息详情
	 */
	StationOutput detail(Integer id);

	void delete(Integer id);
}
