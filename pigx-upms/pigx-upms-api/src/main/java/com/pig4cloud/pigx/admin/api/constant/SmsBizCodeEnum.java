package com.pig4cloud.pigx.admin.api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 短信发送编码
 *
 * @author lengleng
 * @date 2024/7/18
 * <p>
 * SMS_NORMAL_CODE("SMS_NORMAL_CODE", "普通验证码"),
 */
@RequiredArgsConstructor
public enum SmsBizCodeEnum {

    SMS_NORMAL_CODE("SMS_NORMAL_CODE", "普通验证码");

    @Getter
    private final String code;

    private final String desc;
}
