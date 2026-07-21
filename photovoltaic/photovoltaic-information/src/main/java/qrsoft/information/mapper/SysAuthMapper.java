package qrsoft.information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import qrsoft.common.entity.SysAuth;

import java.util.List;

@Mapper
public interface SysAuthMapper extends BaseMapper<SysAuth> {

	@Select("SELECT DISTINCT a.* FROM sys_auth a " +
			"INNER JOIN sys_role_auth ra ON a.id = ra.auth " +
			"INNER JOIN sys_user_role ur ON ra.role = ur.role " +
			"WHERE ur.user = #{userId} AND a.del_flag = 0 AND a.type = 0 AND a.code IS NOT NULL")
	List<SysAuth> getAuthByUser(@Param("userId") Integer userId);

	@Select("SELECT DISTINCT a.* FROM sys_auth a " +
			"INNER JOIN sys_role_auth ra ON a.id = ra.auth " +
			"INNER JOIN sys_user_role ur ON ra.role = ur.role " +
			"WHERE ur.user = #{userId} AND a.del_flag = 0 AND a.type = 1 " +
			"ORDER BY a.menu_order ASC, a.id ASC")
	List<SysAuth> getMenuByUser(@Param("userId") Integer userId);
}
