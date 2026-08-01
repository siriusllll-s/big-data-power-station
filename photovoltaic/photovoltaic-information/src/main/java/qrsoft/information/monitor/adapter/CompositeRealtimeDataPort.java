package qrsoft.information.monitor.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import qrsoft.information.dto.output.CombinerBoxOutput;
import qrsoft.information.dto.output.InverterOutput;
import qrsoft.information.dto.output.MeterOutput;
import qrsoft.information.monitor.port.RealtimeDataPort;

import java.util.List;
import java.util.Map;

/**
 * 默认注入的实时端口：委托给 {@link DeviceTableRealtimeDataAdapter}。
 * 后续可在此切换 Redis/ES 实现而不改应用服务。
 */
@Component
@Primary
public class CompositeRealtimeDataPort implements RealtimeDataPort {

	private final RealtimeDataPort delegate;

	@Autowired
	public CompositeRealtimeDataPort(DeviceTableRealtimeDataAdapter deviceTableRealtimeDataAdapter) {
		this.delegate = deviceTableRealtimeDataAdapter;
	}

	@Override
	public List<InverterOutput> loadInverters() {
		return delegate.loadInverters();
	}

	@Override
	public Map<String, List<CombinerBoxOutput>> loadCombinerBoxes() {
		return delegate.loadCombinerBoxes();
	}

	@Override
	public List<MeterOutput> loadMeters() {
		return delegate.loadMeters();
	}
}
