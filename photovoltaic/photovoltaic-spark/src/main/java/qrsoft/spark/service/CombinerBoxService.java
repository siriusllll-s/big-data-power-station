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
import qrsoft.common.entity.DataCombinerBox;
import qrsoft.common.entity.Fault;
import qrsoft.common.util.DateUtil;
import qrsoft.spark.mapper.FaultMapper;
import qrsoft.spark.util.HBaseUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@Component
public class CombinerBoxService {
	@Autowired private FaultMapper faultMapper;
	@Autowired private StringRedisTemplate redisTemplate;
	@Autowired private RestHighLevelClient restHighLevelClient;
	private static final String HBASE_TABLE = "CombinerBoxInfo";
	private static final String FAMILY = "f";
	public void save(DataCombinerBox box) {
		Date date;
		try {
			Fault fault = new Fault();
			fault.setStation(box.getStation());
			fault.setDeviceName(box.getName());
			fault.setDeviceType(SparkConstant.COMBINER_BOX);
			fault.setFaultLevel(new Random().nextInt(3));
			fault.setFaultTime(DateUtil.stringToDate(box.getCreateTime(), DateUtil.YYMMDD_HHMMSS));
			date = fault.getFaultTime();
			fault.setFaultDesc("汇流箱数据异常：" + box);
			double sum = 0;
			for (String s : box.getCombinerBoxIns()) sum += Double.parseDouble(s);
			double percentage = Double.parseDouble(box.getCombinerBox()) / sum;
			if (percentage > 1.00) { faultMapper.insert(fault); return; }
		} catch (Exception e) {
			System.err.println("汇流箱数据清洗失败：" + e.getMessage());
			return;
		}
		String rowKey = String.valueOf(date.getTime()) + box.getName().hashCode();
		Map<String, String> map = new HashMap<String, String>();
		map.put("station", box.getStation().toString());
		map.put("ammeterName", box.getAmmeterName());
		map.put("inverterName", box.getInverterName());
		map.put("dcCabinetName", box.getDcCabinetName());
		map.put("name", box.getName());
		map.put("createTime", String.valueOf(date.getTime()));
		map.put("combinerBoxIns", JSONObject.toJSONString(box.getCombinerBoxIns()));
		map.put("combinerBox", box.getCombinerBox());
		Map<String, Object> mapES = new HashMap<String, Object>();
		mapES.put("station", box.getStation());
		mapES.put("name", box.getName());
		mapES.put("createTime", date.getTime());
		mapES.put("combinerBox", Double.valueOf(box.getCombinerBox()));
		mapES.put("rowKey", rowKey);
		try { HBaseUtil.insertRowData(HBASE_TABLE, rowKey, FAMILY, map); }
		catch (Exception e) { System.err.println("汇流箱保存HBase失败：" + e.getMessage()); return; }
		try {
			IndexRequest request = new IndexRequest("combiner_box");
			request.source(JSONObject.toJSONString(mapES), XContentType.JSON);
			restHighLevelClient.index(request, RequestOptions.DEFAULT);
		} catch (Exception e) { System.err.println("汇流箱保存ES失败：" + e.getMessage()); }
		try {
			String key = box.getStation() + "," + box.getAmmeterName() + "," + box.getInverterName() + "," + box.getDcCabinetName() + "," + box.getName();
			Boolean hasKey = redisTemplate.hasKey(key);
			if (hasKey != null && hasKey) redisTemplate.delete(key);
			redisTemplate.boundValueOps(key).set(JSONObject.toJSONString(box));
		} catch (Exception e) { System.err.println("汇流箱保存Redis失败：" + e.getMessage()); }
	}
}
