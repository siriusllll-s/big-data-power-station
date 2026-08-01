package qrsoft.information.monitor.service;
import qrsoft.information.dto.output.*;
import qrsoft.information.dto.page.MonitorPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import java.util.List;
import java.util.Map;
public interface IRealDataService {
  List<DeviceRealTimeOutput> pageHistory(MonitorPage input);
  ResultPage<StationDailyPowerOutput> pageStationPower(MonitorPage input);
  List<InverterOutput> getInverterData();
  Map<String, List<CombinerBoxOutput>> getCombinerBoxData();
  List<MeterOutput> getMeterData();
}
