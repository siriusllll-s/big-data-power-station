package qrsoft.spark.service;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import qrsoft.common.constant.SparkConstant;
import qrsoft.common.entity.DataDCCabinet;
import qrsoft.common.entity.Fault;
import qrsoft.common.util.DateUtil;
import qrsoft.spark.mapper.FaultMapper;
import qrsoft.spark.util.HBaseUtil;
import java.util.*;
@Component
public class DcCabinetService {
	@Autowired private FaultMapper faultMapper;
	@Autowired private StringRedisTemplate redisTemplate;
	@Autowired private RestHighLevelClient restHighLevelClient;
	private static final String HBASE_TABLE = "DcCabinetInfo";
	private static final String FAMILY = "f";
	public void save(DataDCCabinet dcCabinet) {
		Date date;
		try {
			Fault fault = new Fault();
			fault.setStation(dcCabinet.getStation());
			fault.setDeviceName(dcCabinet.getName());
			fault.setDeviceType(SparkConstant.DC_CABINET);
			fault.setFaultLevel(new Random().nextInt(3));
			fault.setFaultTime(DateUtil.stringToDate(dcCabinet.getCreateTime(), DateUtil.YYMMDD_HHMMSS));
			date = fault.getFaultTime();
			fault.setFaultDesc("直流柜数据异常：" + dcCabinet);
			double sum = 0; for (String s : dcCabinet.getDcCabinetIns()) sum += Double.parseDouble(s);
			if (Double.parseDouble(dcCabinet.getDcCabinet()) / sum > 1.00) { faultMapper.insert(fault); return; }
		} catch (Exception e) { System.err.println("直流柜清洗失败：" + e.getMessage()); return; }
		String rowKey = String.valueOf(date.getTime()) + dcCabinet.getName().hashCode();
		Map<String, String> map = new HashMap<String, String>();
		map.put("station", dcCabinet.getStation().toString());
		map.put("ammeterName", dcCabinet.getAmmeterName());
		map.put("inverterName", dcCabinet.getInverterName());
		map.put("name", dcCabinet.getName());
		map.put("createTime", String.valueOf(date.getTime()));
		map.put("dcCabinetIns", JSONObject.toJSONString(dcCabinet.getDcCabinetIns()));
		map.put("dcCabinet", dcCabinet.getDcCabinet());
		Map<String, Object> mapES = new HashMap<String, Object>();
		mapES.put("station", dcCabinet.getStation());
		mapES.put("name", dcCabinet.getName());
		mapES.put("createTime", date.getTime());
		mapES.put("dcCabinet", Double.valueOf(dcCabinet.getDcCabinet()));
		mapES.put("rowKey", rowKey);
		try { HBaseUtil.insertRowData(HBASE_TABLE, rowKey, FAMILY, map); } catch (Exception e) { System.err.println("直流柜HBase失败：" + e.getMessage()); return; }
		try {
			IndexRequest request = new IndexRequest("dc_cabinet");
			request.source(JSONObject.toJSONString(mapES), XContentType.JSON);
			restHighLevelClient.index(request, RequestOptions.DEFAULT);
		} catch (Exception e) { System.err.println("直流柜ES失败：" + e.getMessage()); }
		try {
			String key = dcCabinet.getStation() + "," + dcCabinet.getAmmeterName() + "," + dcCabinet.getInverterName() + "," + dcCabinet.getName();
			Boolean hasKey = redisTemplate.hasKey(key);
			if (hasKey != null && hasKey) redisTemplate.delete(key);
			redisTemplate.boundValueOps(key).set(JSONObject.toJSONString(dcCabinet));
		} catch (Exception e) { System.err.println("直流柜Redis失败：" + e.getMessage()); }
	}
}
