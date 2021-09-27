package com.pig4cloud.pig.common.security.grant;

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

/**
 * @author hzq
 * @since 2021-09-14
 */
@Slf4j
public class CustomAppAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

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
		UserDetails userDetails = ((PigUserDetailsServiceImpl) userDetailsService).loadUserByPhone(phone);
		CustomAppAuthenticationToken token = new CustomAppAuthenticationToken(userDetails);
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
		return authentication.isAssignableFrom(CustomAppAuthenticationToken.class);
	}

}
