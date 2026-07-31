package qrsoft.information.service;

import qrsoft.information.dto.input.InspectionPlanInput;
import qrsoft.information.dto.output.InspectionPlanDetailOutput;
import qrsoft.information.dto.output.InspectionPlanOutput;
import qrsoft.information.dto.page.InspectionPlanPage;
import qrsoft.information.dto.vo.ResultPage;

public interface InspectionPlanService {
	ResultPage<InspectionPlanOutput> pagePlanByParam(InspectionPlanPage input);

	InspectionPlanInput detailPlan(Integer id);

	InspectionPlanDetailOutput viewPlan(Integer id);

	void saveOrUpdatePlan(InspectionPlanInput input);

	void deletePlan(Integer id);
}
