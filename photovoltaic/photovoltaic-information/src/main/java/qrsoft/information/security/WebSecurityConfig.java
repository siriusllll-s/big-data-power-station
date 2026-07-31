package qrsoft.information.security;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import qrsoft.information.dto.vo.WrappedResult;
import qrsoft.information.filter.TokenAuthenticationFilter;
import qrsoft.information.filter.TokenLoginFilter;
import qrsoft.information.mapper.SysAuthMapper;

import javax.servlet.http.HttpServletResponse;

/**
 * 安全配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SysAuthMapper authMapper;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private TokenAuthenticationProvider tokenAuthenticationProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(tokenAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.cors();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests()
				// 实验联调：大屏/电站管理/电价合同/字典/上传放行；生产请收紧
				.antMatchers(
						"/login", "/register",
						"/screen/**",
						"/station/**",
						"/stationSolarPrice/**",
						"/stationContract/**",
						"/statistics/**",
						"/dictionary/**",
						"/minio/**",
						"/powerDataReport/**",
						"/workerOrder/**",
						"/inspection/**",
						"/inspectionPlan/**",
						"/inspectionPoint/**",
						"/device/**",
						"/user/**",
						"/experience/**",
						"/data/**"
				).permitAll()
				.anyRequest().authenticated();
		http.headers().cacheControl();
		http.addFilterBefore(new TokenLoginFilter("/login", authenticationManager(), redisTemplate, authMapper), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().print(JSONObject.toJSON((WrappedResult.failedWrappedResult(accessDeniedException.getMessage(), "403"))));
		});
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/doc.html/**", "/v2/**", "/swagger-resources/**", "/webjars/**", "/minio/**");
	}
}
