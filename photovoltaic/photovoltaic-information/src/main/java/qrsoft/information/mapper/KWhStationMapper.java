package qrsoft.information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import qrsoft.common.entity.KWhStation;

import java.util.List;
import java.util.Map;

@Mapper
public interface KWhStationMapper extends BaseMapper<KWhStation> {

	@Select("SELECT DATE_FORMAT(power_date, '%Y-%m') AS month, SUM(kwh) AS kwh " +
			"FROM k_wh_station " +
			"WHERE station = #{id} AND DATE_FORMAT(power_date, '%Y-%m') >= #{startMonth} " +
			"GROUP BY DATE_FORMAT(power_date, '%Y-%m') " +
			"ORDER BY month ASC")
	List<Map<String, Object>> kWhStatisticByMonth(@Param("id") Integer id, @Param("startMonth") String startMonth);
}
