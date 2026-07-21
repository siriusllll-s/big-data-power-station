package qrsoft.information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import qrsoft.common.entity.SysUser;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

	@Select("SELECT * FROM sys_user WHERE name = #{name} AND del_flag = 0 LIMIT 1")
	SysUser getByName(@Param("name") String name);

	@Update("UPDATE sys_user SET password = #{password} WHERE id = #{id}")
	int updatePassword(@Param("id") Integer id, @Param("password") String password);

	@org.apache.ibatis.annotations.Insert("INSERT INTO sys_user_role (`user`, role) VALUES (#{userId}, #{roleId})")
	int bindDefaultRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}
