package qrsoft.information.monitor.port;

import qrsoft.information.dto.output.CombinerBoxOutput;
import qrsoft.information.dto.output.InverterOutput;
import qrsoft.information.dto.output.MeterOutput;

import java.util.List;
import java.util.Map;

/**
 * 实时设备读模型端口（六边形架构）。
 * <p>
 * 应用服务依赖此接口，不关心数据来自设备表、Redis、ES 还是样例适配器。
 */
public interface RealtimeDataPort {

	List<InverterOutput> loadInverters();

	/**
	 * key：所属分组名（如逆变器名），value：该组下汇流箱列表
	 */
	Map<String, List<CombinerBoxOutput>> loadCombinerBoxes();

	List<MeterOutput> loadMeters();
}
