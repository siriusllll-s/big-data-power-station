package qrsoft.information.monitor.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.shared.aspect.SysLog;
import qrsoft.information.dto.output.*;
import qrsoft.information.dto.page.MonitorPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.monitor.service.IRealDataService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monitor")
@Api(tags = "数据监控管理")
public class MonitorController {
  @Autowired private IRealDataService realDataService;

  @PostMapping("/historyData")
  @SysLog(action = "设备实时数据查询")
  @ApiOperation("设备历史/实时数据查询")
  public WrappedResult<List<DeviceRealTimeOutput>> pageHistory(@RequestBody MonitorPage input) {
    return WrappedResult.successWrappedResult(realDataService.pageHistory(input));
  }

  @PostMapping("/stationPower")
  @SysLog(action = "电站发电量查询")
  @ApiOperation("电站天发电量查询")
  public WrappedResult<ResultPage<StationDailyPowerOutput>> pageStationPower(@RequestBody MonitorPage input) {
    return WrappedResult.successWrappedResult(realDataService.pageStationPower(input));
  }

  @GetMapping("/inverter")
  @SysLog(action = "逆变器实时监控")
  @ApiOperation("逆变器实时数据")
  public WrappedResult<List<InverterOutput>> getInverterData() {
    return WrappedResult.successWrappedResult(realDataService.getInverterData());
  }

  @GetMapping("/combinerBox")
  @SysLog(action = "汇流箱实时监控")
  @ApiOperation("汇流箱实时数据")
  public WrappedResult<Map<String, List<CombinerBoxOutput>>> getCombinerBoxData() {
    return WrappedResult.successWrappedResult(realDataService.getCombinerBoxData());
  }

  @GetMapping("/meter")
  @SysLog(action = "电表实时监控")
  @ApiOperation("电表实时数据")
  public WrappedResult<List<MeterOutput>> getMeterData() {
    return WrappedResult.successWrappedResult(realDataService.getMeterData());
  }
}
