package qrsoft.information.ops.service;

import qrsoft.information.dto.input.HandleOrderInput;
import qrsoft.information.dto.input.WorkOrderInput;
import qrsoft.information.dto.output.WorkOrderOutput;
import qrsoft.information.dto.page.WorkOrderPage;
import qrsoft.information.shared.dto.vo.ResultPage;

public interface IWorkOrderService {
	ResultPage<WorkOrderOutput> pageByParam(WorkOrderPage input);

	WorkOrderOutput detail(Integer id);

	void saveOrUpdate(WorkOrderInput input);

	void delete(Integer id);

	void handleOrder(HandleOrderInput input);
}
