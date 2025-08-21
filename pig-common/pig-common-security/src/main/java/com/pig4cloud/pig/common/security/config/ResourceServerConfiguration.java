package com.pig4cloud.pig.common.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.WebUtils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.oauth2.data.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.template.SaOAuth2Util;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;

/**
 * Resource Server 配置
 *
 * @author lengleng
 * @date 2024/11/13
 */
@RequiredArgsConstructor
public class ResourceServerConfiguration implements WebMvcConfigurer {

	@Autowired
	@Lazy
	private PermitAllUrlProperties permitAllUrlProperties;

	/**
	 * 添加 sa-token 注解拦截器
	 * @param registry 注册表
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		SaInterceptor saInterceptor = new SaInterceptor();
		saInterceptor.isAnnotation(true);
		registry.addInterceptor(saInterceptor).addPathPatterns("/**");
	}

	/**
	 * 资源服务器配置
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
				SaLoginParameter saLoginModel = new SaLoginParameter();

				StpUtil.login(loginId, saLoginModel);
			});
		}).setError(e -> {
			// 校验令牌失败 424 (主要是和401区分)
			SaHolder.getResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			SaHolder.getResponse().setStatus(HttpStatus.FAILED_DEPENDENCY.value());
			return JSONUtil.toJsonStr(R.failed(e.getMessage()));
		});

		for (String url : permitAllUrlProperties.getUrls()) {
			saServletFilter.addExclude(url);
		}
		return saServletFilter;
	}

}
