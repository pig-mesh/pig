package com.pig4cloud.pig.auth.config;

/**
 * @author lengleng
 * @date 2024/7/22
 */

import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.admin.api.feign.RemoteClientDetailsService;
import com.pig4cloud.pig.auth.support.PasswordLoginHandle;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * Pig SA OAuth2 服务器配置
 *
 * @author lengleng
 * @date 2024/07/23
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class PigSaOAuth2ServerConfiguration extends SaOAuth2Template {

	private final RemoteClientDetailsService remoteClientDetailsService;

	// 根据 id 获取 Client 信息
	@Override
	public SaClientModel getClientModel(String clientId) {
		R<SysOauthClientDetails> clientDetailsById = remoteClientDetailsService.getClientDetailsById(clientId);

		SysOauthClientDetails clientDetailsByIdData = clientDetailsById.getData();
		if (Objects.nonNull(clientDetailsByIdData)) {
			return new SaClientModel().setClientId(clientDetailsByIdData.getClientId())
				.setClientSecret(clientDetailsByIdData.getClientSecret())
				.setAllowUrl(clientDetailsByIdData.getWebServerRedirectUri())
				.setContractScope(clientDetailsByIdData.getScope())
				.setIsPassword(ArrayUtil.contains(clientDetailsByIdData.getAuthorizedGrantTypes(),
						SaOAuth2Consts.GrantType.password))
				.setIsClient(ArrayUtil.contains(clientDetailsByIdData.getAuthorizedGrantTypes(),
						SaOAuth2Consts.GrantType.client_credentials))
				.setIsImplicit(ArrayUtil.contains(clientDetailsByIdData.getAuthorizedGrantTypes(),
						SaOAuth2Consts.GrantType.implicit))
				.setIsCode(ArrayUtil.contains(clientDetailsByIdData.getAuthorizedGrantTypes(),
						SaOAuth2Consts.GrantType.authorization_code))
				.setAccessTokenTimeout(clientDetailsByIdData.getAccessTokenValidity())
				.setRefreshTokenTimeout(clientDetailsByIdData.getRefreshTokenValidity())
				.setIsAutoMode(BooleanUtil.toBoolean(clientDetailsByIdData.getAutoapprove()));
		}
		return null;
	}

	// 根据ClientId 和 LoginId 获取openid
	@Override
	public String getOpenid(String clientId, Object loginId) {
		return loginId.toString();
	}

	// Sa-OAuth2 定制化配置
	@Autowired
	public void setSaOAuth2Config(SaOAuth2Config cfg, PasswordLoginHandle passwordLoginHandle) {
		cfg.setDoLoginHandle(passwordLoginHandle);
		cfg.setIsCode(true);
		cfg.setIsPassword(true);
		cfg.setIsClient(true);
		cfg.setIsImplicit(true);
		cfg.setIsNewRefresh(true);
	}

}
