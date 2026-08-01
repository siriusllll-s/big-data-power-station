package qrsoft.information.device.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qrsoft.common.entity.Device;
import qrsoft.common.entity.DeviceFactory;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.input.DeviceInput;
import qrsoft.information.dto.output.DeviceOutPut;
import qrsoft.information.dto.page.DevicePage;
import qrsoft.information.shared.dto.vo.ResultPage;
import qrsoft.information.mapper.DeviceFactoryMapper;
import qrsoft.information.mapper.DeviceMapper;
import qrsoft.information.device.service.IDeviceService;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements IDeviceService {
  @Autowired private DeviceMapper deviceMapper;
  @Autowired private DeviceFactoryMapper factoryMapper;

  @Override
  public ResultPage<DeviceOutPut> pageByParam(DevicePage input) {
    if (input == null) input = new DevicePage();
    QueryWrapper<Device> q = new QueryWrapper<>();
    q.eq("del_flag", 0);
    if (StringUtils.isNotBlank(input.getNo())) q.like("no", input.getNo());
    if (StringUtils.isNotBlank(input.getName())) q.like("name", input.getName());
    if (input.typeInt() != null) q.eq("type", input.typeInt());
    if (StringUtils.isNotBlank(input.getInstallTime())) q.ge("install_time", input.getInstallTime().substring(0, 10));
    if (StringUtils.isNotBlank(input.getEndTime())) q.le("end_time", input.getEndTime().substring(0, 10));
    q.orderByDesc("id");
    int pageNo = input.getPage() == null || input.getPage() < 1 ? 1 : input.getPage();
    int limit = input.getLimit() == null || input.getLimit() < 1 ? 10 : input.getLimit();
    Page<Device> page = deviceMapper.selectPage(new Page<>(pageNo, limit), q);
    ResultPage<DeviceOutPut> r = new ResultPage<>(page);
    r.setList(page.getRecords().stream().map(this::toOutput).collect(Collectors.toList()));
    return r;
  }

  @Override @Transactional
  public void saveOrUpdate(DeviceInput input) {
    if (input == null || StringUtils.isBlank(input.getName()) || StringUtils.isBlank(input.getNo()))
      throw new RuntimeException("设备编号和名称不能为空");
    Device e = input.getId() == null ? new Device() : deviceMapper.selectById(input.getId());
    if (input.getId() != null && e == null) throw new RuntimeException("设备不存在");
    e.setNo(input.getNo());
    e.setName(input.getName());
    e.setType(input.typeInt());
    e.setFactory(input.factoryInt());
    e.setDeviceAddress(input.getDeviceAddress());
    e.setSpecifications(input.getSpecifications());
    e.setModel(input.getModel());
    e.setDaiId(input.getDaiId());
    e.setMemo(input.getMemo());
    e.setStation(1);
    try {
      if (StringUtils.isNotBlank(input.getInstallTime()))
        e.setInstallTime(DateUtil.stringToDate(input.getInstallTime().substring(0, 10), DateUtil.YYMMDD));
      if (StringUtils.isNotBlank(input.getEndTime()))
        e.setEndTime(DateUtil.stringToDate(input.getEndTime().substring(0, 10), DateUtil.YYMMDD));
    } catch (Exception ex) { throw new RuntimeException("日期格式错误"); }
    if (e.getDelFlag() == null) e.setDelFlag(0);
    if (input.getId() == null) deviceMapper.insert(e); else deviceMapper.updateById(e);
  }

  @Override @Transactional
  public void delete(Integer id) {
    Device e = deviceMapper.selectById(id);
    if (e == null) throw new RuntimeException("设备不存在");
    e.setDelFlag(1);
    deviceMapper.updateById(e);
  }

  @Override
  public DeviceOutPut detail(Integer id) {
    Device e = deviceMapper.selectById(id);
    if (e == null || (e.getDelFlag() != null && e.getDelFlag() == 1)) throw new RuntimeException("设备不存在");
    return toOutput(e);
  }

  @Override
  public List<DeviceOutPut> getDeviceByType(Integer type) {
    QueryWrapper<Device> q = new QueryWrapper<>();
    q.eq("del_flag", 0);
    if (type != null) q.eq("type", type);
    q.orderByAsc("id");
    return deviceMapper.selectList(q).stream().map(this::toOutput).collect(Collectors.toList());
  }

  @Override
  public List<DeviceOutPut> deviceList() {
    return deviceMapper.selectList(new QueryWrapper<Device>().eq("del_flag", 0).orderByAsc("id"))
        .stream().map(this::toOutput).collect(Collectors.toList());
  }

  private DeviceOutPut toOutput(Device e) {
    DeviceOutPut o = new DeviceOutPut();
    o.setId(e.getId());
    o.setNo(e.getNo());
    o.setName(e.getName());
    o.setType(e.getType());
    o.setFactory(e.getFactory());
    o.setDeviceAddress(e.getDeviceAddress());
    o.setSpecifications(e.getSpecifications());
    o.setModel(e.getModel());
    o.setDaiId(e.getDaiId());
    o.setMemo(e.getMemo());
    if (e.getInstallTime() != null) o.setInstallTime(DateUtil.dateToString(e.getInstallTime(), DateUtil.YYMMDD));
    if (e.getEndTime() != null) o.setEndTime(DateUtil.dateToString(e.getEndTime(), DateUtil.YYMMDD));
    if (e.getFactory() != null) {
      DeviceFactory f = factoryMapper.selectById(e.getFactory());
      if (f != null) o.setFactoryName(f.getName());
    }
    return o;
  }
}
