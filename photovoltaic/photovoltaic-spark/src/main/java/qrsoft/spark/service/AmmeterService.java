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
import qrsoft.common.entity.DataAmmeter;
import qrsoft.common.entity.Fault;
import qrsoft.common.entity.KWhAmmeter;
import qrsoft.common.util.DateUtil;
import qrsoft.spark.mapper.FaultMapper;
import qrsoft.spark.mapper.KWhAmmeterMapper;
import qrsoft.spark.util.HBaseUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@Component
public class AmmeterService {
	@Autowired private FaultMapper faultMapper;
	@Autowired private StringRedisTemplate redisTemplate;
	@Autowired private RestHighLevelClient restHighLevelClient;
	@Autowired private KWhAmmeterMapper ammeterMapper;
	private static final String HBASE_TABLE = "AmmeterInfo";
	private static final String FAMILY = "f";
	private static Map<String, KWhAmmeter> yAndTMap = new HashMap<String, KWhAmmeter>();
	private final Map<String, Double> ammeterMap = new HashMap<String, Double>();
	private Date todayDatePublic = null;
	public void save(DataAmmeter ammeter) {
		Date date;
		try {
			Fault fault = new Fault();
			fault.setStation(ammeter.getStation());
			fault.setDeviceName(ammeter.getName());
			fault.setDeviceType(SparkConstant.AMMETER);
			fault.setFaultLevel(new Random().nextInt(3));
			fault.setFaultTime(DateUtil.stringToDate(ammeter.getCreateTime(), DateUtil.YYMMDD_HHMMSS));
			date = fault.getFaultTime();
			fault.setFaultDesc("电表数据异常：" + ammeter);
			int i = 0;
			if (ammeterMap.containsKey(ammeter.getName())) {
				double percentage = (Double.parseDouble(ammeter.getAmmeter()) - ammeterMap.get(ammeter.getName())) / Double.parseDouble(ammeter.getInverter());
				if (percentage > 1.00) i = faultMapper.insert(fault);
			}
			ammeterMap.put(ammeter.getName(), Double.parseDouble(ammeter.getAmmeter()));
			if (i != 0) return;
		} catch (Exception e) {
			System.err.println("电表数据清洗失败：" + e.getMessage());
			return;
		}
		String rowKey = String.valueOf(date.getTime()) + ammeter.getName().hashCode();
		Map<String, String> map = new HashMap<String, String>();
		map.put("station", ammeter.getStation().toString());
		map.put("name", ammeter.getName());
		map.put("createTime", String.valueOf(date.getTime()));
		map.put("inverter", ammeter.getInverter());
		map.put("ammeter", ammeter.getAmmeter());
		Map<String, Object> mapES = new HashMap<String, Object>();
		mapES.put("station", ammeter.getStation());
		mapES.put("name", ammeter.getName());
		mapES.put("createTime", date.getTime());
		mapES.put("inverter", Double.valueOf(ammeter.getInverter()));
		mapES.put("ammeter", Double.valueOf(ammeter.getAmmeter()));
		mapES.put("rowKey", rowKey);
		try { HBaseUtil.insertRowData(HBASE_TABLE, rowKey, FAMILY, map); }
		catch (Exception e) { System.err.println("电表数据保存HBase失败：" + e.getMessage()); return; }
		try {
			IndexRequest request = new IndexRequest("ammeter");
			request.source(JSONObject.toJSONString(mapES), XContentType.JSON);
			restHighLevelClient.index(request, RequestOptions.DEFAULT);
		} catch (Exception e) { System.err.println("电表数据保存ES失败：" + e.getMessage()); }
		try {
			String key = ammeter.getStation() + "," + ammeter.getName();
			Boolean hasKey = redisTemplate.hasKey(key);
			if (hasKey != null && hasKey) redisTemplate.delete(key);
			redisTemplate.boundValueOps(key).set(JSONObject.toJSONString(ammeter));
		} catch (Exception e) { System.err.println("电表数据保存Redis失败：" + e.getMessage()); }
		try {
			Date todayDate = DateUtil.getToDayDate();
			if (todayDatePublic == null) todayDatePublic = todayDate;
			String yesterdayKey = ammeter.getName() + "yesterday";
			String todayKey = ammeter.getName() + "today";
			KWhAmmeter yesterday = yAndTMap.get(yesterdayKey);
			if (yesterday == null || !todayDatePublic.equals(todayDate)) {
				yesterday = ammeterMapper.selectOne(new QueryWrapper<KWhAmmeter>().eq("power_date", DateUtil.addDay(todayDate, -1)).eq("ammeter", ammeter.getName()));
				yAndTMap.put(yesterdayKey, yesterday);
			}
			KWhAmmeter today = yAndTMap.get(todayKey);
			if (today == null || !todayDatePublic.equals(todayDate)) {
				today = ammeterMapper.selectOne(new QueryWrapper<KWhAmmeter>().eq("power_date", todayDate).eq("ammeter", ammeter.getName()));
				if (today == null) {
					today = new KWhAmmeter();
					today.setPowerDate(todayDate);
					today.setAmmeter(ammeter.getName());
					ammeterMapper.insert(today);
				}
				yAndTMap.put(todayKey, today);
				todayDatePublic = todayDate;
			}
			double end = Double.parseDouble(ammeter.getAmmeter());
			double yEnd = (yesterday == null || yesterday.getEndKwh() == null || yesterday.getEndKwh() > end) ? 0 : yesterday.getEndKwh();
			today.setKwh(BigDecimal.valueOf(end).subtract(BigDecimal.valueOf(yEnd)).doubleValue());
			today.setEndKwh(end);
			ammeterMapper.updateById(today);
		} catch (Exception e) { System.err.println("统计ammeter天发电量出错: " + e.getMessage()); }
	}
}
