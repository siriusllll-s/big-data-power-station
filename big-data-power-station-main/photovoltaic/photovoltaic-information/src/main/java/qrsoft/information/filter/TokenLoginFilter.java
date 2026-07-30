package qrsoft.information.filter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import qrsoft.common.entity.SysAuth;
import qrsoft.common.entity.SysUser;
import qrsoft.information.dto.vo.AuthAndMenu;
import qrsoft.information.dto.vo.TokenVO;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.mapper.SysAuthMapper;
import qrsoft.information.utils.TokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录拦截器
 */
public class TokenLoginFilter extends AbstractAuthenticationProcessingFilter {

	private final StringRedisTemplate redisTemplate;

	private final SysAuthMapper authMapper;

	public TokenLoginFilter(String url, AuthenticationManager authManager, StringRedisTemplate redisTemplate, SysAuthMapper authMapper) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
		this.redisTemplate = redisTemplate;
		this.authMapper = authMapper;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		if (StringUtils.isBlank(name) && StringUtils.isBlank(password)) {
			JSONObject jsonObject = JSONObject.parseObject(request.getInputStream(), JSONObject.class);
			if (jsonObject == null || jsonObject.isEmpty()) throw new BadCredentialsException("用户名或密码错误");
			name = jsonObject.getString("name");
			password = jsonObject.getString("password");
		}
		if (name == null || name.contains(" ")) throw new BadCredentialsException("用户名或密码错误");
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(name, password));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			if (auth.getDetails() instanceof SysUser) {
				SysUser user = (SysUser) auth.getDetails();
				Map<String, Object> payload = new HashMap<>();
				payload.put("id", user.getId());
				payload.put("name", auth.getName());
				String token = TokenUtil.genToken(payload);
				TokenVO tokenVO = new TokenVO();
				tokenVO.setName(auth.getName());
				tokenVO.setTrueName(user.getTrueName());
				tokenVO.setId(user.getId());
				tokenVO.setType(user.getType());
				Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
				List<String> authorityList = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
				Map<String, List<AuthAndMenu>> authAndAuth = getAuthAndAuth(user, authorityList);
				tokenVO.setAuthList(authAndAuth.get("authList"));
				tokenVO.setMenuList(authAndAuth.get("menuList"));
				Boolean hasUser = redisTemplate.hasKey(user.getId().toString());
				if (hasUser != null && hasUser) {
					String userToken = redisTemplate.boundValueOps(user.getId().toString()).get();
					if (userToken != null) {
						Boolean hasToken = redisTemplate.hasKey(userToken);
						if (hasToken != null && hasToken) redisTemplate.delete(userToken);
					}
					redisTemplate.delete(user.getId().toString());
				}
				redisTemplate.boundValueOps(user.getId().toString()).set(token);
				redisTemplate.expire(user.getId().toString(), TokenUtil.EXP_TIME, TimeUnit.MINUTES);
				if (authorityList.isEmpty()) authorityList.add("only menu");
				BoundSetOperations<String, String> setOperations = redisTemplate.boundSetOps(token);
				setOperations.add(authorityList.toArray(new String[]{}));
				redisTemplate.expire(token, TokenUtil.EXP_TIME, TimeUnit.MINUTES);
				response.setHeader("Authorization", token);
				response.addHeader("Access-Control-Expose-Headers", "Authorization");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print(JSONObject.toJSON(WrappedResult.successWrappedResult(tokenVO)));
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().print(JSONObject.toJSON(WrappedResult.failedWrappedResult("登录失败")));
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print(JSONObject.toJSON("Token生成失败"));
		}
	}

	private Map<String, List<AuthAndMenu>> getAuthAndAuth(SysUser sysUser, List<String> list) {
		Map<String, List<AuthAndMenu>> map = new HashMap<>();
		List<SysAuth> authList = authMapper.getAuthByUser(sysUser.getId());
		if (authList != null && authList.size() > 0) {
			List<AuthAndMenu> authListVO = authList.stream().filter(o -> list.contains(o.getCode())).map(AuthAndMenu::authTOAuth).collect(Collectors.toList());
			map.put("authList", authListVO);
		} else {
			map.put("authList", new ArrayList<>());
		}
		List<SysAuth> menuList = authMapper.getMenuByUser(sysUser.getId());
		if (menuList != null && menuList.size() > 0) {
			List<AuthAndMenu> mList = menuList.stream().map(AuthAndMenu::authTOMenu).collect(Collectors.toList());
			Map<Integer, List<AuthAndMenu>> menuMap = new HashMap<>();
			for (AuthAndMenu menu : mList) {
				int parentId = menu.getParentId() == null ? 0 : menu.getParentId();
				boolean flag = menuMap.containsKey(parentId);
				List<AuthAndMenu> tempList = flag ? menuMap.get(parentId) : new ArrayList<>();
				tempList.add(menu);
				menuMap.put(parentId, tempList);
			}
			List<AuthAndMenu> parents = menuMap.get(0);
			if (parents == null) {
				parents = new ArrayList<>();
			} else {
				for (AuthAndMenu output : parents) {
					output.setChilds(createMenuTree(menuMap, output.getId()));
				}
			}
			map.put("menuList", parents);
		} else {
			map.put("menuList", new ArrayList<>());
		}
		return map;
	}

	private List<AuthAndMenu> createMenuTree(Map<Integer, List<AuthAndMenu>> map, Integer parentId) {
		List<AuthAndMenu> outputs = map.get(parentId);
		if (outputs != null && !outputs.isEmpty()) {
			for (AuthAndMenu output : outputs) output.setChilds(createMenuTree(map, output.getId()));
		}
		return outputs;
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(JSONObject.toJSON(WrappedResult.failedWrappedResult(failed.getMessage())));
	}
}
