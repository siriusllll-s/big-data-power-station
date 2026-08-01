package qrsoft.information.identity.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import qrsoft.common.entity.SysAuth;
import qrsoft.common.entity.SysUser;
import qrsoft.information.mapper.SysAuthMapper;
import qrsoft.information.mapper.SysUserMapper;

import java.util.List;

/**
 * 用于处理认证实体
 */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private SysUserMapper userMapper;

	@Autowired
	private SysAuthMapper authMapper;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/**
	 * 身份认证
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication.getPrincipal() == null || authentication.getCredentials() == null)
			throw new BadCredentialsException("用户名或密码错误");
		String name = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		SysUser sysUser = userMapper.getByName(name);
		if (sysUser == null) throw new UsernameNotFoundException("用户不存在或已删除");
		if (!passwordEncoder.matches(password, sysUser.getPassword())) throw new BadCredentialsException("用户名或密码错误");
		List<GrantedAuthority> authorities = getGrantedAuthorities(sysUser);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(sysUser.getName(), sysUser.getPassword(), authorities);
		token.setDetails(sysUser);
		return token;
	}

	private List<GrantedAuthority> getGrantedAuthorities(SysUser sysUser) {
		List<SysAuth> authList = authMapper.getAuthByUser(sysUser.getId());
		return AuthorityUtils.createAuthorityList(authList.stream().map(SysAuth::getCode).filter(StringUtils::isNotBlank).distinct().toArray(String[]::new));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
