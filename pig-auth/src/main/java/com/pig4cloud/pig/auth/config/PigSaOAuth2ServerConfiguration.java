package com.pig4cloud.pig.auth.config;

/**
 * @author lengleng
 * @date 2024/7/22
 */

import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.config.SaOAuth2ServerConfig;
import com.pig4cloud.pig.auth.support.SaOAuth2TokenDataGenerateImpl;
import com.pig4cloud.pig.auth.support.handle.ConfirmViewHandle;
import com.pig4cloud.pig.auth.support.handle.NoLoginViewHandle;
import com.pig4cloud.pig.auth.support.handle.PasswordLoginHandle;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Pig SA OAuth2 服务器配置
 *
 * @author lengleng
 * @date 2024/07/23
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class PigSaOAuth2ServerConfiguration {

	/**
	 * 设置 SA OAuth2 配置
	 * @param cfg cfg
	 * @param confirmViewHandle 确认视图
	 * @param passwordLoginHandle 密码登录
	 * @param noLoginViewHandle 无登录视图
	 */
	@Autowired
	@SneakyThrows
	public void setSaOAuth2Config(SaOAuth2ServerConfig cfg, ConfirmViewHandle confirmViewHandle,
			PasswordLoginHandle passwordLoginHandle, NoLoginViewHandle noLoginViewHandle) {

		// 配置登录页面
		cfg.notLoginView = noLoginViewHandle;
		// 配置登录处理逻辑
		cfg.doLoginHandle = passwordLoginHandle;
		// 配置授权页面
		cfg.confirmView = confirmViewHandle;

		// 注入自定义的 oauth2 数据生成处理
		SaOAuth2Manager.setDataGenerate(new SaOAuth2TokenDataGenerateImpl());
	}

}
