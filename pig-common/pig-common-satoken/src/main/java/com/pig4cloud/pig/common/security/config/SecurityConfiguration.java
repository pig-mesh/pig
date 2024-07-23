package com.pig4cloud.pig.common.security.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.oauth2.model.AccessTokenModel;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lengleng
 * @date 2024/7/22
 */
@RequiredArgsConstructor
public class SecurityConfiguration implements WebMvcConfigurer {

	@Autowired
	@Lazy
	private PermitAllUrlProperties permitAllUrlProperties;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注解拦截器
		SaInterceptor saInterceptor = new SaInterceptor();
		saInterceptor.isAnnotation(true);
		registry.addInterceptor(saInterceptor).addPathPatterns("/**");
	}

	/**
	 * 校验是否从网关转发
	 */
	@Bean
	public SaServletFilter getSaServletFilter() {

		SaServletFilter saServletFilter = new SaServletFilter().addInclude("/**").setAuth(obj -> {
			SaRouter.match("/**").check(() -> {

				// Authorization: bearer token
				String token = WebUtils.getToken();
				AccessTokenModel accessTokenModel = SaOAuth2Util.checkAccessToken(token);

				// 登录
				String loginId = accessTokenModel.loginId.toString();
				StpUtil.login(loginId, SaLoginConfig.setToken(token));
			});
		}).setError(e -> {
			SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
			SaHolder.getResponse().setStatus(424);
			// 使用封装的 JSON 工具类转换数据格式
			return JSONUtil.toJsonStr(R.failed("认证失败，无法访问系统资源"));
		});

		for (String url : permitAllUrlProperties.getUrls()) {
			saServletFilter.addExclude(url);
		}
		return saServletFilter;
	}

}