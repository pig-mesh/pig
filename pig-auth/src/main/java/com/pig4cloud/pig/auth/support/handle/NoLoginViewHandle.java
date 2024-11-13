package com.pig4cloud.pig.auth.support.handle;

import cn.dev33.satoken.oauth2.function.SaOAuth2NotLoginViewFunction;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Map;

/**
 * 未登录页面配置
 *
 * @author lengleng
 * @date 2024/11/13
 */
@Service
@RequiredArgsConstructor
public class NoLoginViewHandle implements SaOAuth2NotLoginViewFunction {

	private final FreeMarkerConfigurer freeMarker;

	/**
	 * Gets a result.
	 * @return a result
	 */
	@Override
	public Object get() {
		return get(Map.of());
	}

	@SneakyThrows
	public String get(Map<String, Object> model) {
		Template loginTemplate = freeMarker.getConfiguration().getTemplate("login.ftl");
		return FreeMarkerTemplateUtils.processTemplateIntoString(loginTemplate, model);
	}

}
