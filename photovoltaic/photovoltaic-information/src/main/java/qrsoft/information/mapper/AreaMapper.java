package qrsoft.information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import qrsoft.common.entity.Area;

import java.util.List;

@Mapper
public interface AreaMapper extends BaseMapper<Area> {

	@Select("select a.* from area a left join city c on a.city_code = c.city_code where c.id = #{city}")
	List<Area> areaList(Integer city);
}
