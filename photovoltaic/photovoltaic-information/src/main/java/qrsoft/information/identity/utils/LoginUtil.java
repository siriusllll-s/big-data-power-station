package qrsoft.information.identity.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import qrsoft.common.util.MapUtils;
import qrsoft.information.shared.dto.vo.R;

import javax.servlet.http.HttpServletRequest;

public class LoginUtil {

	public static Integer getUserId(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (StringUtils.isBlank(token)) {
			return null;
		}
		R r = TokenUtil.valid(token);
		if (!r.isSuccess()) {
			return null;
		}
		return MapUtils.getInteger(r.getPayloadMap(), "id", null);
	}

	public static void clearToken(StringRedisTemplate redisTemplate, Integer userId, String token) {
		if (userId != null) {
			redisTemplate.delete(userId.toString());
		}
		if (StringUtils.isNotBlank(token)) {
			redisTemplate.delete(token);
		}
	}
}
