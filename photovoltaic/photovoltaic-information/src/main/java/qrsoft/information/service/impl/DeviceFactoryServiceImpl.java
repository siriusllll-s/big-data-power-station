package qrsoft.information.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qrsoft.common.entity.DeviceFactory;
import qrsoft.information.dto.input.DeviceFactoryInput;
import qrsoft.information.dto.output.DeviceFactoryOutput;
import qrsoft.information.dto.page.DeviceFactoryPage;
import qrsoft.information.dto.vo.ResultPage;
import qrsoft.information.mapper.DeviceFactoryMapper;
import qrsoft.information.service.IDeviceFactoryService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceFactoryServiceImpl implements IDeviceFactoryService {
  @Autowired private DeviceFactoryMapper factoryMapper;

  @Override @Transactional
  public void saveOrUpdate(DeviceFactoryInput input) {
    if (input == null || StringUtils.isBlank(input.getName())) throw new RuntimeException("厂商名称不能为空");
    DeviceFactory e = input.getId() == null ? new DeviceFactory() : factoryMapper.selectById(input.getId());
    if (input.getId() != null && e == null) throw new RuntimeException("厂商不存在");
    e.setName(input.getName());
    e.setAddress(input.getAddress());
    e.setPerson(input.getPerson());
    e.setPersonTel(input.getPersonTel());
    e.setMemo(input.getMemo());
    if (e.getDelFlag() == null) e.setDelFlag(0);
    if (input.getId() == null) factoryMapper.insert(e); else factoryMapper.updateById(e);
  }

  @Override
  public ResultPage<DeviceFactoryOutput> pageByParam(DeviceFactoryPage input) {
    if (input == null) input = new DeviceFactoryPage();
    QueryWrapper<DeviceFactory> q = new QueryWrapper<>();
    q.eq("del_flag", 0);
    if (StringUtils.isNotBlank(input.getName())) q.like("name", input.getName());
    if (StringUtils.isNotBlank(input.getPerson())) q.like("person", input.getPerson());
    q.orderByDesc("id");
    int pageNo = input.getPage() == null || input.getPage() < 1 ? 1 : input.getPage();
    int limit = input.getLimit() == null || input.getLimit() < 1 ? 10 : input.getLimit();
    Page<DeviceFactory> page = factoryMapper.selectPage(new Page<>(pageNo, limit), q);
    ResultPage<DeviceFactoryOutput> r = new ResultPage<>(page);
    r.setList(page.getRecords().stream().map(DeviceFactoryOutput::of).collect(Collectors.toList()));
    return r;
  }

  @Override @Transactional
  public void delete(Integer id) {
    DeviceFactory e = factoryMapper.selectById(id);
    if (e == null) throw new RuntimeException("厂商不存在");
    e.setDelFlag(1);
    factoryMapper.updateById(e);
  }

  @Override
  public DeviceFactoryOutput detail(Integer id) {
    DeviceFactory e = factoryMapper.selectById(id);
    if (e == null || (e.getDelFlag() != null && e.getDelFlag() == 1)) throw new RuntimeException("厂商不存在");
    return DeviceFactoryOutput.of(e);
  }

  @Override
  public List<DeviceFactoryOutput> factoryList() {
    return factoryMapper.selectList(new QueryWrapper<DeviceFactory>().eq("del_flag", 0).orderByAsc("id"))
        .stream().map(DeviceFactoryOutput::of).collect(Collectors.toList());
  }
}
