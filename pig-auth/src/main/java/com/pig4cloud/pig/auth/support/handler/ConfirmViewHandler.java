package com.pig4cloud.pig.auth.support.handler;

import cn.dev33.satoken.oauth2.function.SaOAuth2ConfirmViewFunction;
import cn.dev33.satoken.stp.StpUtil;
import com.pig4cloud.pig.common.core.util.WebUtils;
import freemarker.template.Template;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.List;
import java.util.Map;

/**
 * 确认页配置
 *
 * @author lengleng
 * @date 2024/11/13
 */
@Service
@RequiredArgsConstructor
public class ConfirmViewHandler implements SaOAuth2ConfirmViewFunction {

	private final FreeMarkerConfigurer freeMarker;

	@Override
	@SneakyThrows
	public Object apply(String clientId, List<String> scopes) {

		HttpServletRequest httpServletRequest = WebUtils.getRequest().get();

		Template confirmTemplate = freeMarker.getConfiguration().getTemplate("confirm.ftl");
		Map<String, Object> model = Map.of("clientId", clientId, "scopes", scopes, "loginId", StpUtil.getLoginId(),
				"redirectUri", httpServletRequest.getParameter("redirect_uri"), "contextPath",
				httpServletRequest.getContextPath());
		return FreeMarkerTemplateUtils.processTemplateIntoString(confirmTemplate, model);
	}

}
