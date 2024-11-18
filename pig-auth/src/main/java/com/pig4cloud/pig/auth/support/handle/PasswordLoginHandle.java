package com.pig4cloud.pig.auth.support.handle;

import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import cn.dev33.satoken.oauth2.function.SaOAuth2DoLoginHandleFunction;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * 密码登录处理器
 *
 * @author lengleng
 * @date 2024/07/23
 */
@Service
@RequiredArgsConstructor
public class PasswordLoginHandle implements SaOAuth2DoLoginHandleFunction {

	private final Optional<RemoteUserService> remoteUserServiceOptional;

	/**
	 * 登录逻辑
	 * @param username 用户名
	 * @param password 密码
	 * @return {@link Object }
	 */
	@Override
	public Object apply(String username, String password) {
		if (remoteUserServiceOptional.isEmpty()) {
			return SaResult.error();
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(username);
		R<UserInfo> result = remoteUserServiceOptional.get().info(userDTO);

		if (Objects.nonNull(result.getData())
				&& BCrypt.checkpw(password, result.getData().getSysUser().getPassword())) {
			StpUtil.login(username);
			return SaResult.ok();
		}

		// 如果验证失败，抛出badCredentials
		throw new SaOAuth2Exception(MsgUtils.getSecurityMessage("BindAuthenticator.badCredentials"));
	}

}
