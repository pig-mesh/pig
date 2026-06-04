package com.pig4cloud.pig.auth.support.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.CaptchaFlagTypeEnum;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.security.captcha.CaptchaResult;
import com.pig4cloud.pig.common.security.captcha.CaptchaValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 授权登录验证码支持组件
 *
 * <p>
 * 统一处理验证码开关判断、授权客户端解析和验证码校验，避免在多个过滤器和控制器中重复实现。
 * </p>
 *
 * @author pig
 * @date 2026-03-20
 */
@Component
@RequiredArgsConstructor
public class AuthCaptchaSupport {

	private final RequestCache requestCache = new HttpSessionRequestCache();

	private final CaptchaValidator captchaValidator;

	private final OauthClientDetailsLoader clientDetailsLoader;

	/**
	 * 解析当前授权流程中的真实客户端ID
	 * @param request 当前请求
	 * @param response 当前响应
	 * @param includeCurrentRequestId 是否优先读取当前请求参数中的 client_id
	 * @return 授权客户端ID
	 */
	public String resolveAuthorizationClientId(HttpServletRequest request, HttpServletResponse response,
			boolean includeCurrentRequestId) {
		return resolveAuthorizationParameter(request, response, OAuth2ParameterNames.CLIENT_ID,
				includeCurrentRequestId);
	}

	/**
	 * 判断客户端是否开启验证码
	 * @param clientId 客户端ID
	 * @return true 开启验证码
	 */
	public boolean isCaptchaEnabled(String clientId) {
		if (StrUtil.isBlank(clientId)) {
			return false;
		}

		SysOauthClientDetails clientDetails = clientDetailsLoader.getByClientId(clientId);
		if (clientDetails == null || StrUtil.isBlank(clientDetails.getAdditionalInformation())) {
			return true;
		}

		JSONObject information = JSONUtil.parseObj(clientDetails.getAdditionalInformation());
		return !StrUtil.equals(CaptchaFlagTypeEnum.OFF.getType(), information.getStr(CommonConstants.CAPTCHA_FLAG));
	}

	/**
	 * 校验请求中的验证码
	 * @param request 当前请求
	 * @throws ValidateCodeException 验证码校验失败
	 */
	public void validateCode(HttpServletRequest request) throws ValidateCodeException {
		String code = request.getParameter("code");
		if (StrUtil.isBlank(code)) {
			throw new ValidateCodeException(MsgUtils.getMessage(AuthErrorCodes.AUTH_CAPTCHA_EMPTY));
		}

		String randomStr = request.getParameter("randomStr");
		// https://gitee.com/log4j/pig/issues/IWA0D 手机号场景以 mobile 作为缓存键
		String mobile = request.getParameter("mobile");
		if (StrUtil.isNotBlank(mobile)) {
			randomStr = mobile;
		}

		boolean isBehavior = StrUtil.equalsAnyIgnoreCase(randomStr, CommonConstants.IMAGE_CODE_BLOCK_PUZZLE,
				CommonConstants.IMAGE_CODE_CLICK_WORD);
		String captchaType = isBehavior ? randomStr : CommonConstants.IMAGE_CODE_MATH;
		String captchaVerification = isBehavior ? code
				: randomStr + CommonConstants.CAPTCHA_VERIFICATION_SEPARATOR + code;

		CaptchaResult result = captchaValidator.validate(captchaType, captchaVerification);
		if (!result.isOk()) {
			String errorCode = isBehavior ? AuthErrorCodes.AUTH_CAPTCHA_EMPTY : AuthErrorCodes.AUTH_CAPTCHA_INVALID;
			throw new ValidateCodeException(MsgUtils.getMessage(errorCode));
		}
	}

	private String resolveAuthorizationParameter(HttpServletRequest request, HttpServletResponse response,
			String parameterName, boolean includeCurrentRequestId) {
		if (includeCurrentRequestId) {
			String parameterValue = request.getParameter(parameterName);
			if (StrUtil.isNotBlank(parameterValue)) {
				return parameterValue;
			}
		}

		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (Objects.isNull(savedRequest)) {
			return null;
		}

		String[] parameterValues = savedRequest.getParameterValues(parameterName);
		if (Objects.isNull(parameterValues) || parameterValues.length == 0) {
			return null;
		}

		return parameterValues[0];
	}

}
