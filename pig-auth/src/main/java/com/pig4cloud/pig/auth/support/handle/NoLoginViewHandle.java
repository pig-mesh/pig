package com.pig4cloud.pig.auth.support.handle;

import cn.dev33.satoken.oauth2.function.SaOAuth2NotLoginViewFunction;
import com.pig4cloud.pig.common.core.util.WebUtils;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
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
		return get(new HashMap<>());
	}

	@SneakyThrows
	public String get(Map<String, Object> model) {
		Template loginTemplate = freeMarker.getConfiguration().getTemplate("login.ftl");
		model.put("contextPath", WebUtils.getRequest().get().getContextPath());
		return FreeMarkerTemplateUtils.processTemplateIntoString(loginTemplate, model);
	}

}
