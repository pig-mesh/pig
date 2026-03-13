package com.pig4cloud.pig.auth.support.core;

/**
 * Auth 模块错误编码
 *
 * @author lengleng
 * @date 2022/3/30
 */
public interface AuthErrorCodes {

    // === 验证码相关 ===
    String AUTH_CAPTCHA_EMPTY = "auth.captcha.empty";

    String AUTH_CAPTCHA_INVALID = "auth.captcha.invalid";

    // === 客户端相关 ===
    String AUTH_CLIENT_INVALID = "auth.client.invalid";
}
