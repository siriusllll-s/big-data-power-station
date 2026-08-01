package qrsoft.information.monitor.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import qrsoft.common.constant.BaseConstant;
import qrsoft.common.entity.Device;
import qrsoft.common.util.DateUtil;
import qrsoft.information.dto.output.CombinerBoxOutput;
import qrsoft.information.dto.output.InverterOutput;
import qrsoft.information.dto.output.MeterOutput;
import qrsoft.information.mapper.DeviceMapper;
import qrsoft.information.monitor.port.RealtimeDataPort;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 优先从 device 主数据表取设备名，再组装实时读模型；
 * 表空或不可用时回退 {@link BaseConstant} 中的实验设备名。
 * <p>
 * Order 高于样例适配器，作为默认 {@link org.springframework.context.annotation.Primary} 候选由
 * {@link CompositeRealtimeDataPort} 聚合时优先选用。
 */
@Component
@Order(1)
public class DeviceTableRealtimeDataAdapter implements RealtimeDataPort {

	@Autowired(required = false)
	private DeviceMapper deviceMapper;

	@Override
	public List<InverterOutput> loadInverters() {
		List<String> names = deviceNames(BaseConstant.DEVICE_INVERTER, BaseConstant.inverterList);
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
	public Map<String, List<CombinerBoxOutput>> loadCombinerBoxes() {
		String now = DateUtil.currentDateStr(DateUtil.YYMMDD_HHMMSS);
		String[] boxes = {"01号汇流箱", "02号汇流箱", "03号汇流箱", "04号汇流箱", "05号汇流箱"};
		List<String> groups = deviceNames(BaseConstant.DEVICE_INVERTER, BaseConstant.inverterList);
		Map<String, List<CombinerBoxOutput>> map = new LinkedHashMap<>();
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
	public List<MeterOutput> loadMeters() {
		List<String> names = deviceNames(BaseConstant.DEVICE_AMMETER, BaseConstant.ammeterList);
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
				List<Device> ds = deviceMapper.selectList(
						new QueryWrapper<Device>().eq("del_flag", 0).eq("type", type));
				if (ds != null && !ds.isEmpty()) {
					return ds.stream().map(Device::getName).collect(Collectors.toList());
				}
			}
		} catch (Exception ignored) {
			// fall through
		}
		return Arrays.asList(fallback);
	}

	private static double scale(double v) {
		return BigDecimal.valueOf(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
