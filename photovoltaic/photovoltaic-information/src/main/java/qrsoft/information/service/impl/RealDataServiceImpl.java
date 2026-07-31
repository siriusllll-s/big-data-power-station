package qrsoft.information.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qrsoft.common.constant.BaseConstant;
import qrsoft.common.entity.Device;
import qrsoft.common.entity.KWhStation;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.output.*;
import qrsoft.information.dto.page.MonitorPage;
import qrsoft.information.dto.vo.ResultPage;
import qrsoft.information.mapper.DeviceMapper;
import qrsoft.information.mapper.KWhStationMapper;
import qrsoft.information.service.IRealDataService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RealDataServiceImpl implements IRealDataService {

  @Autowired private KWhStationMapper kWhStationMapper;
  @Autowired(required = false) private DeviceMapper deviceMapper;

  @Override
  public List<DeviceRealTimeOutput> pageHistory(MonitorPage input) {
    List<DeviceRealTimeOutput> list = new ArrayList<>();
    if (input == null || input.getDevices() == null || input.getDevices().isEmpty()) return list;
    String start = input.getStartDate();
    String end = input.getEndDate();
    if (StringUtils.isBlank(start) || StringUtils.isBlank(end)) {
      end = DateUtil.currentDateStr(DateUtil.YYMMDD);
      start = end;
    }
    try {
      Date s = DateUtil.stringToDate(start.substring(0, 10), DateUtil.YYMMDD);
      Date e = DateUtil.stringToDate(end.substring(0, 10), DateUtil.YYMMDD);
      Calendar cal = Calendar.getInstance();
      cal.setTime(s);
      int guard = 0;
      while (!cal.getTime().after(e) && guard < 62) {
        String day = DateUtil.dateToString(cal.getTime(), DateUtil.YYMMDD);
        for (String name : input.getDevices()) {
          DeviceRealTimeOutput o = new DeviceRealTimeOutput();
          o.setDate(day);
          o.setName(name);
          // 稳定伪数据：日期+设备名哈希
          double v = 80 + Math.abs((day + name).hashCode() % 5000) / 10.0;
          o.setValue(BigDecimal.valueOf(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
          list.add(o);
        }
        cal.add(Calendar.DAY_OF_YEAR, 1);
        guard++;
      }
    } catch (Exception ex) {
      // fallback empty
    }
    return list;
  }

  @Override
  public ResultPage<StationDailyPowerOutput> pageStationPower(MonitorPage input) {
    if (input == null) input = new MonitorPage();
    QueryWrapper<KWhStation> q = new QueryWrapper<>();
    q.eq("station", BaseConstant.STATION);
    try {
      if (StringUtils.isNotBlank(input.getStartDate())) {
        q.ge("power_date", input.getStartDate().substring(0, 10));
      }
      if (StringUtils.isNotBlank(input.getEndDate())) {
        q.le("power_date", input.getEndDate().substring(0, 10));
      }
    } catch (Exception ignored) {}
    q.orderByDesc("power_date");
    int pageNo = input.getPage() == null || input.getPage() < 1 ? 1 : input.getPage();
    int limit = input.getLimit() == null || input.getLimit() < 1 ? 10 : input.getLimit();
    Page<KWhStation> page = kWhStationMapper.selectPage(new Page<>(pageNo, limit), q);
    ResultPage<StationDailyPowerOutput> result = new ResultPage<>(page);
    List<StationDailyPowerOutput> list = new ArrayList<>();
    for (KWhStation row : page.getRecords()) {
      StationDailyPowerOutput o = new StationDailyPowerOutput();
      double kwh = row.getKwh() == null ? 0 : row.getKwh();
      double ratio = row.getPowerRatio() == null ? 0 : row.getPowerRatio();
      o.setOutPower(scale(kwh));
      o.setInPower(scale(kwh * 1.05));
      o.setLossKwh(scale(kwh * 0.05));
      o.setPowerRatio(scale(ratio));
      if (row.getPowerDate() != null) {
        o.setPowerDate(DateUtil.dateToString(row.getPowerDate(), DateUtil.YYMMDD));
      }
      list.add(o);
    }
    // 无数据时填充样例，保证页面可用
    if (list.isEmpty()) {
      try {
        String day = DateUtil.currentDateStr(DateUtil.YYMMDD);
        StationDailyPowerOutput o = new StationDailyPowerOutput();
        o.setPowerDate(day);
        o.setOutPower(1200.0);
        o.setInPower(1260.0);
        o.setLossKwh(60.0);
        o.setPowerRatio(85.5);
        list.add(o);
        result.setTotal(1);
      } catch (Exception ignored) {}
    }
    result.setList(list);
    return result;
  }

  @Override
  public List<InverterOutput> getInverterData() {
    List<String> names = deviceNames(0, BaseConstant.inverterList);
    String now = DateUtil.currentDateStr(DateUtil.YYMMDD_HHMMSS);
    List<InverterOutput> list = new ArrayList<>();
    for (String n : names) {
      InverterOutput o = new InverterOutput();
      o.setName(n);
      o.setCreateTime(now);
      o.setDailyPower(scale(200 + Math.abs(n.hashCode() % 800)));
      o.setDc(scale(10 + Math.abs(n.hashCode() % 50)));
      o.setDcPower(scale(5 + Math.abs(n.hashCode() % 30)));
      o.setDcVoltage(scale(400 + Math.abs(n.hashCode() % 100)));
      list.add(o);
    }
    return list;
  }

  @Override
  public Map<String, List<CombinerBoxOutput>> getCombinerBoxData() {
    String now = DateUtil.currentDateStr(DateUtil.YYMMDD_HHMMSS);
    String[] boxes = {"01号汇流箱", "02号汇流箱", "03号汇流箱", "04号汇流箱", "05号汇流箱"};
    Map<String, List<CombinerBoxOutput>> map = new LinkedHashMap<>();
    // 按所属逆变器分组展示
    String[] groups = BaseConstant.inverterList;
    int i = 0;
    for (String g : groups) {
      List<CombinerBoxOutput> rows = new ArrayList<>();
      for (int j = 0; j < 2 && i < boxes.length; j++, i++) {
        CombinerBoxOutput o = new CombinerBoxOutput();
        o.setName(boxes[i]);
        o.setCreateTime(now);
        o.setCombinerBoxIns(Arrays.asList("1.2", "1.3", "1.1"));
        o.setCombinerBox(String.valueOf(scale(3 + Math.abs(boxes[i].hashCode() % 10))));
        rows.add(o);
      }
      map.put(g, rows);
    }
    return map;
  }

  @Override
  public List<MeterOutput> getMeterData() {
    List<String> names = deviceNames(4, BaseConstant.ammeterList);
    String now = DateUtil.currentDateStr(DateUtil.YYMMDD_HHMMSS);
    List<MeterOutput> list = new ArrayList<>();
    for (String n : names) {
      MeterOutput o = new MeterOutput();
      o.setName(n);
      o.setCreateTime(now);
      o.setDailyPower(scale(1000 + Math.abs(n.hashCode() % 5000)));
      list.add(o);
    }
    return list;
  }

  private List<String> deviceNames(int type, String[] fallback) {
    try {
      if (deviceMapper != null) {
        List<Device> ds = deviceMapper.selectList(new QueryWrapper<Device>().eq("del_flag", 0).eq("type", type));
        if (ds != null && !ds.isEmpty()) {
          return ds.stream().map(Device::getName).collect(Collectors.toList());
        }
      }
    } catch (Exception ignored) {}
    return Arrays.asList(fallback);
  }

  private static double scale(double v) {
    return BigDecimal.valueOf(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
  }
}
