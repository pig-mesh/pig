package com.pig4cloud.pig.auth.grant;

import com.pig4cloud.pig.common.security.service.PigUserDetailsServiceImpl;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author hzq
 * @since 2021-09-14
 */
@Slf4j
public class PhoneAuthenticationProvider implements AuthenticationProvider {

	@Setter
	private UserDetailsService userDetailsService;
	@Setter
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			log.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException("Bad credentials");
		}

		// 手机号
		String phone = authentication.getName();

		// 验证码/密码
		// 验证码模式 自己去实现验证码检验
		// 这里的code指的是密码
		String code = authentication.getCredentials().toString();

		UserDetails userDetails = ((PigUserDetailsServiceImpl) userDetailsService).loadUserByPhone(phone);

		String password = userDetails.getPassword();

		boolean matches = passwordEncoder.matches(code, password);
		if (!matches) {
			throw new BadCredentialsException("Bad credentials");
		}

		PhoneAuthenticationToken token = new PhoneAuthenticationToken(userDetails);

		token.setDetails(authentication.getDetails());

		return token;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(PhoneAuthenticationToken.class);
	}
}