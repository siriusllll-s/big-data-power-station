package qrsoft.information.monitor.service;
import qrsoft.information.dto.input.ThresholdInput;
import qrsoft.information.dto.output.ThresholdOutput;
import qrsoft.information.dto.page.ThresholdPage;
import qrsoft.information.shared.dto.vo.ResultPage;
public interface IThresholdService {
  ResultPage<ThresholdOutput> pageByParam(ThresholdPage input);
  void saveOrUpdate(ThresholdInput input);
  ThresholdOutput detail(Integer id);
}
