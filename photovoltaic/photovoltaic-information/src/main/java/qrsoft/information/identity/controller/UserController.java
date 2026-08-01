package qrsoft.information.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import qrsoft.common.entity.SysUser;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.mapper.SysUserMapper;
import qrsoft.information.identity.utils.LoginUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private SysUserMapper userMapper;

	@Autowired
	private StringRedisTemplate redisTemplate;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	
	@GetMapping("/list")
	public WrappedResult listUsers() {
		java.util.List<SysUser> users = userMapper.selectList(
				new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SysUser>().eq("del_flag", 0));
		java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
		for (SysUser u : users) {
			java.util.Map<String, Object> m = new java.util.HashMap<>();
			m.put("id", u.getId());
			m.put("name", u.getName());
			m.put("trueName", u.getTrueName());
			m.put("phone", u.getPhone());
			m.put("eMail", u.getEMail());
			list.add(m);
		}
		return WrappedResult.successWrappedResult(list);
	}

	@PostMapping("/changePassword")
	public WrappedResult changePassword(@RequestBody Map<String, String> body, HttpServletRequest request) {
		Integer userId = LoginUtil.getUserId(request);
		if (userId == null) {
			return WrappedResult.failedWrappedResult("未登录");
		}
		String oldPassword = body.get("oldPassword");
		String newPassword = body.get("newPassword");
		SysUser user = userMapper.selectById(userId);
		if (user == null) {
			return WrappedResult.failedWrappedResult("用户不存在");
		}
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			return WrappedResult.failedWrappedResult("原密码错误");
		}
		userMapper.updatePassword(userId, passwordEncoder.encode(newPassword));
		String token = request.getHeader("Authorization");
		LoginUtil.clearToken(redisTemplate, userId, token);
		return WrappedResult.successWrappedResult(null);
	}

	@PostMapping("/logout")
	public WrappedResult logout(HttpServletRequest request) {
		Integer userId = LoginUtil.getUserId(request);
		String token = request.getHeader("Authorization");
		LoginUtil.clearToken(redisTemplate, userId, token);
		return WrappedResult.successWrappedResult(null);
	}
}
