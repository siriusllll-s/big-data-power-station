package qrsoft.information.monitor.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qrsoft.common.entity.Threshold;
import qrsoft.information.dto.input.ThresholdInput;
import qrsoft.information.dto.output.ThresholdOutput;
import qrsoft.information.dto.page.ThresholdPage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.mapper.ThresholdMapper;
import qrsoft.information.monitor.service.IThresholdService;
import java.util.stream.Collectors;

@Service
public class ThresholdServiceImpl implements IThresholdService {
  @Autowired private ThresholdMapper thresholdMapper;

  @Override
  public ResultPage<ThresholdOutput> pageByParam(ThresholdPage input) {
    if (input == null) input = new ThresholdPage();
    QueryWrapper<Threshold> query = new QueryWrapper<>();
    query.select("id", "classification", "type", "level", "cycle", "start_time", "end_time", "is_enable", "memo");
    query.eq("del_flag", 0);
    if (input.classificationInt() != null) query.eq("classification", input.classificationInt());
    if (input.typeInt() != null) query.eq("type", input.typeInt());
    query.orderByDesc("id");
    int pageNo = input.getPage() == null || input.getPage() < 1 ? 1 : input.getPage();
    int limit = input.getLimit() == null || input.getLimit() < 1 ? 10 : input.getLimit();
    Page<Threshold> page = thresholdMapper.selectPage(new Page<>(pageNo, limit), query);
    ResultPage<ThresholdOutput> outputs = new ResultPage<>(page);
    outputs.setList(page.getRecords().stream().map(ThresholdOutput::entityToOutput).collect(Collectors.toList()));
    return outputs;
  }

  @Override @Transactional
  public void saveOrUpdate(ThresholdInput input) {
    if (input == null || input.getType() == null) throw new RuntimeException("报警类型不能为空");
    Threshold threshold = input.getId() != null ? thresholdMapper.selectById(input.getId()) : new Threshold();
    if (input.getId() != null && threshold == null) throw new RuntimeException("阈值信息不存在或已被删除，请重试！");
    Threshold exists = thresholdMapper.selectOne(new QueryWrapper<Threshold>()
        .eq("type", input.getType()).eq("del_flag", 0).last("limit 1"));
    if (exists != null && (input.getId() == null || !exists.getId().equals(input.getId()))) {
      throw new RuntimeException("该报警类型在此电站下已存在,请重新选择！");
    }
    BeanUtils.copyProperties(input, threshold);
    if (threshold.getDelFlag() == null) threshold.setDelFlag(0);
    if (threshold.getStation() == null) threshold.setStation(1);
    int i = input.getId() == null ? thresholdMapper.insert(threshold) : thresholdMapper.updateById(threshold);
    if (i != 1) throw new RuntimeException("保存阈值失败，请重试！");
  }

  @Override
  public ThresholdOutput detail(Integer id) {
    Threshold threshold = thresholdMapper.selectById(id);
    if (threshold == null || (threshold.getDelFlag() != null && threshold.getDelFlag() == 1))
      throw new RuntimeException("阈值信息不存在或已被删除，请重试！");
    return ThresholdOutput.entityToOutput(threshold);
  }
}
