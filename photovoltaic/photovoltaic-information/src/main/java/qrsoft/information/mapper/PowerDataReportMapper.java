package qrsoft.information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import qrsoft.common.entity.PowerDataReport;

@Mapper
public interface PowerDataReportMapper extends BaseMapper<PowerDataReport> {

	@Select("select count(1) from power_data_report where station = #{station} and report_date = #{reportDate}")
	Integer countByStationAndDate(@Param("station") Integer station, @Param("reportDate") String reportDate);
}
