package com.pig4cloud.pigx.admin.api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 系统配置枚举
 *
 * @author lengleng
 * @date 2024/7/14
 */
@RequiredArgsConstructor
public enum SystemConfigTypeEnum {
    /**
     * 存储
     */
    STORAGE("storage"),
    /**
     * 短信
     */
    SMS("sms"),
    /**
     * 网站
     */
    WEBSITE("website"),
    /**
     * 邮件
     */
    EMAIL("email"),


    /**
     * webhook
     */
    WEBHOOK("webhook"),

    /**
     * 叮叮
     */
    DINGDING("dingding"),
    /**
     * 飞书
     */
    FEISHU("feishu"),
    /**
     * 微信
     */
    WETALK("wetalk");

    @Getter
    private final String value;


}
