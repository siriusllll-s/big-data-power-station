package qrsoft.information.service;
import qrsoft.information.dto.input.DeviceFactoryInput;
import qrsoft.information.dto.output.DeviceFactoryOutput;
import qrsoft.information.dto.page.DeviceFactoryPage;
import qrsoft.information.dto.vo.ResultPage;
import java.util.List;
public interface IDeviceFactoryService {
  void saveOrUpdate(DeviceFactoryInput input);
  ResultPage<DeviceFactoryOutput> pageByParam(DeviceFactoryPage input);
  void delete(Integer id);
  DeviceFactoryOutput detail(Integer id);
  List<DeviceFactoryOutput> factoryList();
}
