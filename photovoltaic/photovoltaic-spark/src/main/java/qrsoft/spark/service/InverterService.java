package qrsoft.spark.service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import qrsoft.common.constant.SparkConstant;
import qrsoft.common.entity.DataInverter;
import qrsoft.common.entity.Fault;
import qrsoft.common.entity.KWhInverter;
import qrsoft.common.util.DateUtil;
import qrsoft.spark.mapper.FaultMapper;
import qrsoft.spark.mapper.KWhInverterMapper;
import qrsoft.spark.util.HBaseUtil;
import java.math.BigDecimal;
import java.util.*;
@Component
public class InverterService {
	@Autowired private FaultMapper faultMapper;
	@Autowired private StringRedisTemplate redisTemplate;
	@Autowired private RestHighLevelClient restHighLevelClient;
	@Autowired private KWhInverterMapper inverterMapper;
	private static final String HBASE_TABLE = "InverterInfo";
	private static final String FAMILY = "f";
	public void save(DataInverter inverter) {
		Date date;
		try {
			Fault fault = new Fault();
			fault.setStation(inverter.getStation());
			fault.setDeviceName(inverter.getName());
			fault.setDeviceType(SparkConstant.INVERTER);
			fault.setFaultLevel(new Random().nextInt(3));
			fault.setFaultTime(DateUtil.stringToDate(inverter.getCreateTime(), DateUtil.YYMMDD_HHMMSS));
			date = fault.getFaultTime();
			fault.setFaultDesc("逆变器数据异常：" + inverter);
			double percentage = (Double.parseDouble(inverter.getInverter()) * (Double.parseDouble(inverter.getDcCabinet()) * 15)) / 1000;
			if (percentage > 1.00) { faultMapper.insert(fault); return; }
		} catch (Exception e) { System.err.println("逆变器清洗失败：" + e.getMessage()); return; }
		String rowKey = String.valueOf(date.getTime()) + inverter.getName().hashCode();
		Map<String, String> map = new HashMap<String, String>();
		map.put("station", inverter.getStation().toString());
		map.put("ammeterName", inverter.getAmmeterName());
		map.put("name", inverter.getName());
		map.put("createTime", String.valueOf(date.getTime()));
		map.put("dcCabinet", inverter.getDcCabinet());
		map.put("inverter", inverter.getInverter());
		Map<String, Object> mapES = new HashMap<String, Object>();
		mapES.put("station", inverter.getStation());
		mapES.put("name", inverter.getName());
		mapES.put("createTime", date.getTime());
		mapES.put("inverter", Double.valueOf(inverter.getInverter()));
		mapES.put("rowKey", rowKey);
		try { HBaseUtil.insertRowData(HBASE_TABLE, rowKey, FAMILY, map); } catch (Exception e) { System.err.println("逆变器HBase失败：" + e.getMessage()); return; }
		try {
			IndexRequest request = new IndexRequest("inverter");
			request.source(JSONObject.toJSONString(mapES), XContentType.JSON);
			restHighLevelClient.index(request, RequestOptions.DEFAULT);
		} catch (Exception e) { System.err.println("逆变器ES失败：" + e.getMessage()); }
		try {
			String key = inverter.getStation() + "," + inverter.getAmmeterName() + "," + inverter.getName();
			Boolean hasKey = redisTemplate.hasKey(key);
			if (hasKey != null && hasKey) redisTemplate.delete(key);
			redisTemplate.boundValueOps(key).set(JSONObject.toJSONString(inverter));
		} catch (Exception e) { System.err.println("逆变器Redis失败：" + e.getMessage()); }
		try {
			Date today = DateUtil.getToDayDate();
			KWhInverter kWhInverter = inverterMapper.selectOne(new QueryWrapper<KWhInverter>().eq("power_date", today).eq("inverter", inverter.getName()));
			if (kWhInverter == null) {
				kWhInverter = new KWhInverter();
				kWhInverter.setPowerDate(today);
				kWhInverter.setInverter(inverter.getName());
				kWhInverter.setAmmeter(inverter.getAmmeterName());
				inverterMapper.insert(kWhInverter);
			}
			double cur = Double.parseDouble(inverter.getInverter());
			kWhInverter.setKwh(kWhInverter.getKwh() == null ? cur : BigDecimal.valueOf(kWhInverter.getKwh()).add(BigDecimal.valueOf(cur)).doubleValue());
			inverterMapper.updateById(kWhInverter);
		} catch (Exception e) { System.err.println("统计inverter天发电量出错: " + e.getMessage()); }
	}
}
