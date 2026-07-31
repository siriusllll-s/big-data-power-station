package qrsoft.information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import qrsoft.common.entity.Weather;

@Mapper
public interface WeatherMapper extends BaseMapper<Weather> {

	@Select("select * from weather where station = #{station} order by weather_time desc limit 1")
	Weather queryLastWeather();
}
