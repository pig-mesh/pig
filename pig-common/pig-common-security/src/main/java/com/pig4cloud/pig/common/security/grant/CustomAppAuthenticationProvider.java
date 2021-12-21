package com.pig4cloud.pig.common.security.grant;

import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.common.security.service.PigUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

/**
 * @author hzq
 * @since 2021-09-14
 */
@Slf4j
public class CustomAppAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

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

		// 此处已获得 客户端认证 获取对应 userDetailsService
		Authentication clientAuthentication = SecurityContextHolder.getContext().getAuthentication();
		String clientId = clientAuthentication.getName();
		Map<String, PigUserDetailsService> userDetailsServiceMap = SpringUtil
				.getBeansOfType(PigUserDetailsService.class);
		Optional<PigUserDetailsService> optional = userDetailsServiceMap.values().stream()
				.filter(service -> service.support(clientId)).max(Comparator.comparingInt(Ordered::getOrder));

		if (!optional.isPresent()) {
			throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
		}

		// 手机号
		String phone = authentication.getName();
		UserDetails userDetails = optional.get().loadUserByUsername(phone);
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
