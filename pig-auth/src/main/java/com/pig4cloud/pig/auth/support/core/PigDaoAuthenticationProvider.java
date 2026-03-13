package com.pig4cloud.pig.auth.support.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.auth.support.filter.AuthSecurityConfigProperties;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.pig4cloud.pig.common.data.resolver.ParamResolver;
import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.service.PigUserDetailsService;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Pig 数据访问对象认证提供者
 * <p>
 * 扩展自 Spring Security 的 {@link AbstractUserDetailsAuthenticationProvider}，
 * 仿照 {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider} 实现，
 * 提供了更丰富的认证功能。
 * </p>
 * 
 * <h3>主要功能：</h3>
 * <ul>
 * <li>1. 增加用户过期、锁定、凭证过期、禁用校验</li>
 * <li>2. 增加密码过期校验，支持配置密码有效期</li>
 * <li>3. 增加多种登录方式，灵活使用 UserDetailsService</li>
 * <li>4. 支持 APP 模式免密码验证</li>
 * <li>5. 提供定时攻击保护机制</li>
 * <li>6. 支持多租户上下文设置</li>
 * </ul>
 * 
 * <h3>使用场景：</h3>
 * <p>
 * 适用于需要复杂认证逻辑的系统，特别是多租户、多客户端的应用场景。
 * 可以根据不同的客户端 ID 和授权类型选择合适的用户详情服务。
 * </p>
 * 
 * @author lengleng
 * @date 2022-06-04
 * @version 1.0
 * @since 1.0.0
 */
public class PigDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	/** 用户未找到时使用的默认密码字符串 */
	private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

	/** 基础认证转换器，用于从请求中提取客户端凭证 */
	private final static BasicAuthenticationConverter basicConvert = new BasicAuthenticationConverter();

	/** 密码编码器，用于密码的编码和验证 */
	private PasswordEncoder passwordEncoder;

	/** 用户未找到时的编码密码，用于防止定时攻击 */
	private volatile String userNotFoundEncodedPassword;

	/** 用户详情服务，用于加载用户信息 */
	private UserDetailsService userDetailsService;

	/** 用户详情密码服务，用于密码升级 */
	private UserDetailsPasswordService userDetailsPasswordService;

	/**
	 * 构造函数，初始化认证提供者。
	 * <p>
	 * 执行以下初始化操作：
	 * <ul>
	 * <li>设置国际化消息源，用于错误信息的本地化</li>
	 * <li>配置委托密码编码器，支持多种密码编码格式</li>
	 * </ul>
	 * </p>
	 * 
	 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories#createDelegatingPasswordEncoder()
	 */
	public PigDaoAuthenticationProvider() {
		setMessageSource(SpringUtil.getBean("securityMessageSource"));
		setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
	}

	/**
	 * 执行额外的身份验证检查。
	 * <p>
	 * 该方法在用户详情加载完成后执行，主要进行以下验证：
	 * <ul>
	 * <li>1. 检查授权类型，APP 模式下跳过密码验证</li>
	 * <li>2. 验证密码凭证是否为空</li>
	 * <li>3. 验证提供的密码是否与存储的密码匹配</li>
	 * <li>4. 检查密码是否过期（如果启用了密码过期策略）</li>
	 * </ul>
	 * </p>
	 * 
	 * @param userDetails 表示用户的 UserDetails 对象，其凭证应该被检查
	 * @param authentication 认证请求，此时仍然不受信任
	 * @throws AuthenticationException 如果身份验证失败
	 * @throws BadCredentialsException 如果密码不匹配或为空
	 * @throws CredentialsExpiredException 如果密码已过期
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		// app 模式不用校验密码
		String grantType = WebUtils.getRequest().getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (StrUtil.equals(SecurityConstants.APP, grantType)) {
			return;
		}

		if (authentication.getCredentials() == null) {
			this.logger.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException(this.messages
				.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		String presentedPassword = authentication.getCredentials().toString();
		if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			this.logger.debug("Failed to authenticate since password does not match stored value");
			throw new BadCredentialsException(this.messages
				.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}

		// 密码过期判断，如果开启的
		AuthSecurityConfigProperties configProperties = SpringUtil.getBean(AuthSecurityConfigProperties.class);
		if (configProperties.isExpirePassword() && userDetails instanceof PigUser pigUser
				&& Objects.nonNull(pigUser.getPasswordModifyTime())) {
			Long expireDays = ParamResolver.getLong("PASSWORD_EXPIRE_DAYS", 90L);
			long daysBetween = ChronoUnit.DAYS.between(pigUser.getPasswordModifyTime(), LocalDateTime.now());
			if (daysBetween > expireDays) {
				this.logger.debug("Failed to authenticate since password expired");
				throw new CredentialsExpiredException("User credentials have expired");
			}
		}
	}

	/**
	 * 根据用户名检索用户详情。
	 * <p>
	 * 该方法实现了智能的用户详情服务选择机制：
	 * <ul>
	 * <li>1. 从请求中提取授权类型和客户端 ID</li>
	 * <li>2. 如果客户端 ID 为空，则从 Basic 认证头中提取</li>
	 * <li>3. 根据客户端 ID 和授权类型选择合适的 UserDetailsService</li>
	 * <li>4. 按照优先级（Order）选择最合适的服务</li>
	 * <li>5. 加载用户详情并设置租户上下文（如果是多租户用户）</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * <strong>注意：</strong>该方法包含定时攻击保护机制，确保在用户不存在时
	 * 仍然执行相同的时间消耗操作。
	 * </p>
	 * 
	 * @param username 要检索的用户名
	 * @param authentication 认证请求，此时仍然不受信任
	 * @return 加载的用户详情对象
	 * @throws UsernameNotFoundException 如果用户名不存在
	 * @throws InternalAuthenticationServiceException 如果没有注册合适的 UserDetailsService 或服务返回 null
	 * @see PigUserDetailsService#support(String, String)
	 */
	@SneakyThrows
	@Override
	protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
		prepareTimingAttackProtection();
		String grantType = WebUtils.getRequest().getParameter(OAuth2ParameterNames.GRANT_TYPE);
		String clientId = WebUtils.getRequest().getParameter(OAuth2ParameterNames.CLIENT_ID);

		if (StrUtil.isBlank(clientId)) {
			clientId = basicConvert.convert(WebUtils.getRequest()).getName();
		}

		Map<String, PigUserDetailsService> userDetailsServiceMap = SpringUtil
			.getBeansOfType(PigUserDetailsService.class);

		String finalClientId = clientId;
		Optional<PigUserDetailsService> optional = userDetailsServiceMap.values()
			.stream()
			.filter(service -> service.support(finalClientId, grantType))
			.max(Comparator.comparingInt(Ordered::getOrder));

		if (optional.isEmpty()) {
			throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
		}

		try {
			UserDetails loadedUser = optional.get().loadUserByUsername(username);
			if (loadedUser == null) {
				throw new InternalAuthenticationServiceException(
						"UserDetailsService returned null, which is an interface contract violation");
			}
			return loadedUser;
		}
		catch (UsernameNotFoundException ex) {
			mitigateAgainstTimingAttack(authentication);
			throw ex;
		}
		catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 创建成功的身份验证对象。
	 * <p>
	 * 该方法在身份验证成功后被调用，主要执行以下操作：
	 * <ul>
	 * <li>1. 检查是否需要升级密码编码格式</li>
	 * <li>2. 如果需要升级，使用新的编码格式重新编码密码</li>
	 * <li>3. 通过 UserDetailsPasswordService 更新用户密码</li>
	 * <li>4. 调用父类方法创建最终的认证对象</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * <strong>密码升级机制：</strong>当系统升级了密码编码算法时，
	 * 该方法会自动将用户的旧格式密码升级为新格式，提高系统安全性。
	 * </p>
	 * 
	 * @param principal 由认证提供者返回的主体对象
	 * @param authentication 原始的认证请求
	 * @param user 由认证提供者返回的 UserDetails 实例
	 * @return 成功的认证对象
	 * @see UserDetailsPasswordService#updatePassword(UserDetails, String)
	 * @see PasswordEncoder#upgradeEncoding(String)
	 */
	@Override
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
			UserDetails user) {
		boolean upgradeEncoding = this.userDetailsPasswordService != null
				&& this.passwordEncoder.upgradeEncoding(user.getPassword());
		if (upgradeEncoding) {
			String presentedPassword = authentication.getCredentials().toString();
			String newPassword = this.passwordEncoder.encode(presentedPassword);
			user = this.userDetailsPasswordService.updatePassword(user, newPassword);
		}
		return super.createSuccessAuthentication(principal, authentication, user);
	}

	/**
	 * 准备定时攻击保护机制。
	 * <p>
	 * 该方法为防止定时攻击做准备工作。定时攻击是一种安全攻击方式，
	 * 攻击者通过测量系统响应时间的差异来推断用户是否存在。
	 * </p>
	 * 
	 * <p>
	 * <strong>工作原理：</strong>
	 * <ul>
	 * <li>预先编码一个固定的假密码</li>
	 * <li>当用户不存在时，使用这个假密码进行密码验证操作</li>
	 * <li>确保无论用户是否存在，密码验证的时间消耗基本一致</li>
	 * </ul>
	 * </p>
	 * 
	 * @see #mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken)
	 */
	private void prepareTimingAttackProtection() {
		if (this.userNotFoundEncodedPassword == null) {
			this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
		}
	}

	/**
	 * 执行定时攻击缓解措施。
	 * <p>
	 * 当用户不存在时，该方法会执行一次假的密码验证操作，
	 * 确保系统响应时间与真实用户验证时间保持一致，
	 * 防止攻击者通过响应时间差异判断用户是否存在。
	 * </p>
	 * 
	 * <p>
	 * <strong>安全意义：</strong>
	 * 没有这种保护机制时，攻击者可能会发现：
	 * <ul>
	 * <li>用户存在时：查询数据库 + 密码验证 = 较长时间</li>
	 * <li>用户不存在时：仅返回错误 = 较短时间</li>
	 * </ul>
	 * 通过时间差异，攻击者可以枚举出系统中存在的用户名。
	 * </p>
	 * 
	 * @param authentication 包含用户凭证的认证对象
	 * @see #prepareTimingAttackProtection()
	 */
	private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
		if (authentication.getCredentials() != null) {
			String presentedPassword = authentication.getCredentials().toString();
			this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
		}
	}

	/**
	 * 设置密码编码器实例，用于密码的编码和验证。
	 * <p>
	 * 密码编码器负责以下功能：
	 * <ul>
	 * <li>对明文密码进行安全编码（加密/哈希）</li>
	 * <li>验证用户输入的密码是否与存储的编码密码匹配</li>
	 * <li>支持密码编码格式的升级迁移</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * <strong>注意事项：</strong>
	 * <ul>
	 * <li>如果不设置编码器，密码将以明文形式进行比较（不推荐）</li>
	 * <li>对于已经使用旧版本编码器的系统，建议使用兼容的编码器类型</li>
	 * <li>设置新编码器后，会重置用户未找到时的编码密码缓存</li>
	 * </ul>
	 * </p>
	 * 
	 * @param passwordEncoder 密码编码器实例，必须是 {@code PasswordEncoder} 类型之一
	 * @throws IllegalArgumentException 如果 passwordEncoder 为 null
	 * @see org.springframework.security.crypto.password.PasswordEncoder
	 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
		this.passwordEncoder = passwordEncoder;
		this.userNotFoundEncodedPassword = null;
	}

}
