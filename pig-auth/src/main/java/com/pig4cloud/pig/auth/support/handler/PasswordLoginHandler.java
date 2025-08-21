package com.pig4cloud.pig.auth.support.handler;

import java.util.Optional;

import cn.dev33.satoken.oauth2.granttype.handler.PasswordGrantTypeHandler;
import cn.dev33.satoken.oauth2.granttype.handler.model.PasswordAuthResult;
import org.springframework.stereotype.Service;

import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RetOps;

import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import cn.dev33.satoken.oauth2.function.SaOAuth2DoLoginHandleFunction;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;

/**
 * 密码登录处理器
 *
 * @author lengleng
 * @date 2024/07/23
 */
@Service
@RequiredArgsConstructor
public class PasswordLoginHandler extends PasswordGrantTypeHandler implements SaOAuth2DoLoginHandleFunction {

	private final Optional<RemoteUserService> remoteUserServiceOptional;

	/**
	 * 使用用户名和密码登录的方法（OAuth2密码模式专用）
	 */
	@Override
	public PasswordAuthResult loginByUsernamePassword(String username, String password) {
		try {
			processLogin(username, password);
			return new PasswordAuthResult(username);
		}
		catch (Exception e) {
			throw new SaOAuth2Exception(MsgUtils.getSecurityMessage("BindAuthenticator.badCredentials"));
		}
	}

	/**
	 * 登录逻辑
	 */
	@Override
	public Object apply(String username, String password) {
		try {
			processLogin(username, password);
			return SaResult.ok();
		}
		catch (Exception e) {
			throw new SaOAuth2Exception(MsgUtils.getSecurityMessage("BindAuthenticator.badCredentials"));
		}
	}

	/**
	 * 处理登录的核心逻辑（提取公共部分）
	 */
	private void processLogin(String username, String password) {
		if (remoteUserServiceOptional.isEmpty()) {
			throw new SaOAuth2Exception("用户服务不可用");
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(username);
		R<UserInfo> result = remoteUserServiceOptional.get().info(userDTO);

		SysUser user = RetOps.of(result)
			.getData()
			.map(UserInfo::getSysUser)
			.orElseThrow(() -> new SaOAuth2Exception("用户不存在"));

		if (!isPasswordMatch(password, user.getPassword())) {
			throw new SaOAuth2Exception("密码验证失败");
		}

		StpUtil.login(username);
	}

	/**
	 * BCrypt验证密码
	 */
	private boolean isPasswordMatch(String rawPassword, String encryptedPassword) {
		return BCrypt.checkpw(rawPassword, encryptedPassword);
	}

}