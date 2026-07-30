package qrsoft.spark.service;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import qrsoft.common.entity.DataWeather;
import qrsoft.common.entity.Weather;
import qrsoft.common.util.DateUtil;
import qrsoft.spark.mapper.WeatherMapper;
@Component
public class WeatherService {
	@Autowired private WeatherMapper weatherMapper;
	@Autowired private StringRedisTemplate redisTemplate;
	public void save(DataWeather dataWeather) {
		try {
			Weather weather = inputToEntity(dataWeather);
			int i = weatherMapper.insert(weather);
			if (i != 1) throw new RuntimeException("气象数据保存数据库失败");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		try {
			String key = dataWeather.getStation() + "," + dataWeather.getName();
			Boolean hasKey = redisTemplate.hasKey(key);
			if (hasKey != null && hasKey) redisTemplate.delete(key);
			redisTemplate.boundValueOps(key).set(JSONObject.toJSONString(dataWeather));
		} catch (Exception e) {
			System.err.println("气象数据保存Redis失败：" + e.getMessage());
		}
	}
	private Weather inputToEntity(DataWeather dataWeather) throws Exception {
		Weather weather = new Weather();
		weather.setWeatherName(dataWeather.getName());
		weather.setStation(dataWeather.getStation());
		weather.setWeatherTime(DateUtil.stringToDate(dataWeather.getCreateTime(), DateUtil.YYMMDD_HHMMSS));
		weather.setIrradiance(dataWeather.getIrradiance());
		weather.setAmbientTemperature(dataWeather.getAmbientTemperature());
		weather.setBatteryPanelTemperature(dataWeather.getBatteryPanelTemperature());
		weather.setWindSpeed(dataWeather.getWindSpeed());
		weather.setWindDirection(dataWeather.getWindDirection());
		return weather;
	}
}
