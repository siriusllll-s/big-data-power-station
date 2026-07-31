package qrsoft.information.service;

import qrsoft.information.dto.output.InspectionItemDetailOutput;
import qrsoft.information.dto.output.InspectionManageOutput;
import qrsoft.information.dto.output.InspectionProjectOutput;

import java.util.List;
import java.util.Map;

public interface InspectionService {
	List<InspectionProjectOutput> getProjectList();

	List<InspectionItemDetailOutput> getItemListByProject(Integer projectId);

	Map<String, List<InspectionManageOutput>> getManageList();
}
