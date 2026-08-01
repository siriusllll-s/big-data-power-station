package qrsoft.information.identity.filter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import qrsoft.common.util.MapUtils;
import qrsoft.information.shared.dto.vo.R;
import qrsoft.information.shared.dto.vo.WrappedResult;
import qrsoft.information.identity.utils.TokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 身份认证过滤器
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

	private final StringRedisTemplate redisTemplate;

	public TokenAuthenticationFilter(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String path = httpRequest.getRequestURI();
		if ("/login".equals(path) || path.endsWith("/login")
				|| "/register".equals(path) || path.endsWith("/register")
				|| path.startsWith("/screen")
				|| path.startsWith("/station")
				|| path.startsWith("/statistics")
				|| "OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = httpRequest.getHeader("Authorization");
		if (token != null) {
			R r = TokenUtil.valid(token);
			Boolean tokenBoo = redisTemplate.hasKey(token);
			if (tokenBoo != null && tokenBoo && r.isSuccess()) {
				String name = MapUtils.getString(r.getPayloadMap(), "name", StringUtils.EMPTY);
				Set<String> authoritySet = redisTemplate.boundSetOps(token).members();
				if (authoritySet == null) authoritySet = new HashSet<>();
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(name, StringUtils.EMPTY, AuthorityUtils.createAuthorityList(authoritySet.toArray(new String[]{})));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				filterChain.doFilter(request, response);
			} else {
				writeResponse((HttpServletResponse) response, "Token失效");
			}
		} else {
			writeResponse((HttpServletResponse) response, "未登录");
		}
	}

	private void writeResponse(HttpServletResponse response, String str) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().print(JSONObject.toJSON(WrappedResult.failedWrappedResult(str, String.valueOf(HttpServletResponse.SC_UNAUTHORIZED))));
	}
}
