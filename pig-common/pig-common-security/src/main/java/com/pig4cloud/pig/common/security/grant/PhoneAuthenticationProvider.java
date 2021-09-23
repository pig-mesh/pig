package com.pig4cloud.pig.common.security.grant;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.security.service.PigUserDetailsServiceImpl;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author hzq
 * @since 2021-09-14
 */
@Slf4j
public class PhoneAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Setter
	private UserDetailsService userDetailsService;

	/**
	 * 校验 请求信息userDetails
	 * @param userDetails 用户信息
	 * @param authentication 认证信息
	 * @throws AuthenticationException
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			this.logger.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException(this.messages
					.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}

	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			log.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException("Bad credentials");
		}

		// 手机号
		String phone = authentication.getName();

		if (StrUtil.equals(phone, "17034642999")) {
			throw new UsernameNotFoundException(phone);
		}

		String code = authentication.getCredentials().toString();
		UserDetails userDetails = ((PigUserDetailsServiceImpl) userDetailsService).loadUserByPhone(phone);
		PhoneAuthenticationToken token = new PhoneAuthenticationToken(userDetails);
		token.setDetails(authentication.getDetails());
		return token;
	}

	@Override
	protected UserDetails retrieveUser(String phone, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(PhoneAuthenticationToken.class);
	}

}
