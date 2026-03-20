package com.pig4cloud.pig.auth.support.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.CaptchaFlagTypeEnum;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class AuthCaptchaSupport {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * 解析当前授权流程中的真实客户端ID
     *
     * @param request                 当前请求
     * @param response                当前响应
     * @param includeCurrentRequestId 是否优先读取当前请求参数中的 client_id
     * @return 授权客户端ID
     */
    public String resolveAuthorizationClientId(HttpServletRequest request, HttpServletResponse response,
                                               boolean includeCurrentRequestId) {
        return resolveAuthorizationParameter(request, response, OAuth2ParameterNames.CLIENT_ID, includeCurrentRequestId);
    }

    /**
     * 解析当前授权流程中的租户ID
     *
     * @param request                 当前请求
     * @param response                当前响应
     * @param includeCurrentRequestId 是否优先读取当前请求参数中的租户ID
     * @return 租户ID
     */
    public String resolveAuthorizationTenantId(HttpServletRequest request, HttpServletResponse response,
                                               boolean includeCurrentRequestId) {
        return resolveAuthorizationParameter(request, response, CommonConstants.TENANT_ID, includeCurrentRequestId);
    }

    /**
     * 判断客户端是否开启验证码
     *
     * @param tenantId 租户ID
     * @param clientId 客户端ID
     * @return true 开启验证码
     */
    public boolean isCaptchaEnabled(String tenantId, String clientId) {
        if (StrUtil.isBlank(clientId)) {
            return false;
        }

        String finalTenantId = StrUtil.isBlank(tenantId) ? CommonConstants.TENANT_ID_1.toString() : tenantId;
        String key = String.format("%s:%s:%s", finalTenantId, CacheConstants.CLIENT_FLAG, clientId);
        String val = RedisUtils.get(key);

        if (val == null) {
            return false;
        }

        JSONObject information = JSONUtil.parseObj(val);
        return !StrUtil.equals(CaptchaFlagTypeEnum.OFF.getType(), information.getStr(CommonConstants.CAPTCHA_FLAG));
    }

    /**
     * 校验请求中的验证码
     *
     * @param request 当前请求
     * @throws ValidateCodeException 验证码校验失败
     */
    public void validateCode(HttpServletRequest request) throws ValidateCodeException {
        String code = request.getParameter("code");

        if (StrUtil.isBlank(code)) {
            throw new ValidateCodeException(MsgUtils.getMessage(AuthErrorCodes.AUTH_CAPTCHA_EMPTY));
        }

        String randomStr = request.getParameter("randomStr");

        // https://gitee.com/log4j/pig/issues/IWA0D
        String mobile = request.getParameter("mobile");
        if (StrUtil.isNotBlank(mobile)) {
            randomStr = mobile;
        }

        // 若是滑块登录
        if (StrUtil.equalsAnyIgnoreCase(randomStr, CommonConstants.IMAGE_CODE_BLOCK_PUZZLE, CommonConstants.IMAGE_CODE_CLICK_WORD)) {
            CaptchaService captchaService = SpringContextHolder.getBean(CaptchaService.class);
            CaptchaVO vo = new CaptchaVO();
            vo.setCaptchaVerification(code);
            vo.setCaptchaType(randomStr);
            if (!captchaService.verification(vo).isSuccess()) {
                throw new ValidateCodeException(MsgUtils.getMessage(AuthErrorCodes.AUTH_CAPTCHA_EMPTY));
            }
            return;
        }

        String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;
        if (!RedisUtils.hasKey(key)) {
            throw new ValidateCodeException(MsgUtils.getMessage(AuthErrorCodes.AUTH_CAPTCHA_INVALID));
        }

        String codeObj = RedisUtils.get(key);

        if (codeObj == null) {
            throw new ValidateCodeException(MsgUtils.getMessage(AuthErrorCodes.AUTH_CAPTCHA_INVALID));
        }

        if (StrUtil.isBlank(codeObj)) {
            RedisUtils.delete(key);
            throw new ValidateCodeException(MsgUtils.getMessage(AuthErrorCodes.AUTH_CAPTCHA_INVALID));
        }

        if (!StrUtil.equals(codeObj, code)) {
            RedisUtils.delete(key);
            throw new ValidateCodeException(MsgUtils.getMessage(AuthErrorCodes.AUTH_CAPTCHA_INVALID));
        }

        RedisUtils.delete(key);
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
