package qrsoft.information.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import qrsoft.common.entity.SysUser;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.mapper.SysUserMapper;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class AuthController {

	private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{4,20}$");
	private static final Pattern PHONE_PATTERN = Pattern.compile("^1\\d{10}$");

	@Autowired
	private SysUserMapper userMapper;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@PostMapping("/register")
	public WrappedResult register(@RequestBody Map<String, String> body) {
		String name = StringUtils.trimToEmpty(body.get("name"));
		String password = StringUtils.trimToEmpty(body.get("password"));
		String trueName = StringUtils.trimToEmpty(body.get("trueName"));
		String phone = StringUtils.trimToEmpty(body.get("phone"));

		if (!NAME_PATTERN.matcher(name).matches()) {
			return WrappedResult.failedWrappedResult("用户名需为4-20位字母数字或下划线");
		}
		if (password.length() < 6 || password.length() > 16) {
			return WrappedResult.failedWrappedResult("密码长度需为6-16位");
		}
		if (StringUtils.isBlank(trueName) || trueName.length() > 30) {
			return WrappedResult.failedWrappedResult("请填写真实姓名");
		}
		if (StringUtils.isNotBlank(phone) && !PHONE_PATTERN.matcher(phone).matches()) {
			return WrappedResult.failedWrappedResult("手机号格式不正确");
		}
		if (userMapper.getByName(name) != null) {
			return WrappedResult.failedWrappedResult("用户名已存在");
		}

		SysUser user = new SysUser();
		user.setName(name);
		user.setPassword(passwordEncoder.encode(password));
		user.setTrueName(trueName);
		user.setPhone(StringUtils.isBlank(phone) ? null : phone);
		user.setType(0);
		user.setDelFlag(0);
		userMapper.insert(user);

		// 默认绑定角色 1（管理员角色在本实验库中拥有菜单权限）
		try {
			userMapper.bindDefaultRole(user.getId(), 1);
		} catch (Exception ignored) {
		}

		return WrappedResult.successWrappedResult(user.getId());
	}
}
