package qrsoft.information.service;
import qrsoft.information.dto.input.DeviceInput;
import qrsoft.information.dto.output.DeviceOutPut;
import qrsoft.information.dto.page.DevicePage;
import qrsoft.information.dto.vo.ResultPage;
import java.util.List;
public interface IDeviceService {
  ResultPage<DeviceOutPut> pageByParam(DevicePage input);
  void saveOrUpdate(DeviceInput input);
  void delete(Integer id);
  DeviceOutPut detail(Integer id);
  List<DeviceOutPut> getDeviceByType(Integer type);
  List<DeviceOutPut> deviceList();
}
