package com.pig4cloud.pig.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.pig4cloud.pig.auth.support.SaOAuth2TokenDataGenerateImpl;
import com.pig4cloud.pig.auth.support.handler.ConfirmViewHandler;
import com.pig4cloud.pig.auth.support.handler.NoLoginViewHandler;
import com.pig4cloud.pig.auth.support.handler.PasswordLoginHandler;

import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.config.SaOAuth2ServerConfig;
import cn.dev33.satoken.oauth2.strategy.SaOAuth2Strategy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

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
	public void setSaOAuth2Config(SaOAuth2ServerConfig cfg, ConfirmViewHandler confirmViewHandle,
			PasswordLoginHandler passwordLoginHandle, NoLoginViewHandler noLoginViewHandle) {

		// 配置登录页面
		SaOAuth2Strategy.instance.notLoginView = noLoginViewHandle;
		// 配置登录处理逻辑
		SaOAuth2Strategy.instance.doLoginHandle = passwordLoginHandle;
		// 配置授权页面
		SaOAuth2Strategy.instance.confirmView = confirmViewHandle;

		// 注入自定义的 oauth2 数据生成处理
		SaOAuth2Manager.setDataGenerate(new SaOAuth2TokenDataGenerateImpl());
	}

}
