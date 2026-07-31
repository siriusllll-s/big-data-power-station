package qrsoft.information.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qrsoft.information.aspect.SysLog;
import qrsoft.information.dto.input.ThresholdInput;
import qrsoft.information.dto.output.ThresholdOutput;
import qrsoft.information.dto.page.ThresholdPage;
import qrsoft.information.dto.vo.ResultPage;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.service.IThresholdService;

@RestController
@Api(tags = "阈值管理")
@RequestMapping("/threshold")
public class ThresholdController {
  @Autowired private IThresholdService thresholdService;

  @PostMapping("/pageByParam")
  @SysLog(action = "阈值信息分页查询")
  @ApiOperation("阈值信息分页查询")
  public WrappedResult<ResultPage<ThresholdOutput>> pageByParam(@RequestBody ThresholdPage input) {
    return WrappedResult.successWrappedResult(thresholdService.pageByParam(input));
  }

  @PostMapping("/save")
  @SysLog(action = "保存阈值")
  @ApiOperation("保存阈值")
  public WrappedResult<Boolean> saveOrUpdate(@RequestBody ThresholdInput input) {
    thresholdService.saveOrUpdate(input);
    return WrappedResult.successWrappedResult(true);
  }

  @GetMapping("/detail/{id}")
  @SysLog(action = "获取阈值")
  @ApiOperation("获取阈值")
  public WrappedResult<ThresholdOutput> detail(@ApiParam(value = "阈值id") @PathVariable Integer id) {
    return WrappedResult.successWrappedResult(thresholdService.detail(id));
  }
}
