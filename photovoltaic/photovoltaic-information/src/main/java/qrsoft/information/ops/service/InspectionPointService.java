package qrsoft.information.ops.service;

import qrsoft.information.dto.input.InspectionPointInput;
import qrsoft.information.dto.output.InspectionPointDetailOutput;
import qrsoft.information.dto.output.InspectionPointOutput;
import qrsoft.information.dto.page.InspectionPointPage;
import qrsoft.information.shared.dto.vo.ResultPage;

import java.util.List;

public interface InspectionPointService {
	ResultPage<InspectionPointOutput> pagePointByParam(InspectionPointPage input);

	List<InspectionPointOutput> getPointList();

	InspectionPointInput detailPoint(Integer id);

	InspectionPointDetailOutput viewPoint(Integer id);

	void saveOrUpdatePoint(InspectionPointInput input);

	void deletePoint(Integer id);
}
