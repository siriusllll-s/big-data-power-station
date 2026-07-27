package qrsoft.information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import qrsoft.common.entity.Station;

@Mapper
public interface StationMapper extends BaseMapper<Station> {

	@Select("select count(1) from station where name = #{name} and del_flag = 0")
	Integer countByName(String name);

	@Select("select id,name,short_name,type,status,own,install_capacity,maintain_person,lon,lat from station where id = #{id}")
	Station getSimpleById(Integer id);
}
