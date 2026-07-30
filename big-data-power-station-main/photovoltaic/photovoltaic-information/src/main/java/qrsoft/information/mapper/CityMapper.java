package qrsoft.information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import qrsoft.common.entity.City;

import java.util.List;

@Mapper
public interface CityMapper extends BaseMapper<City> {

	@Select("select c.* from city c left join province p on c.province_code = p.province_code where p.id = #{province}")
	List<City> cityList(Integer province);
}
