package com.pig4cloud.pig.common.core.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * i18n 工具类
 *
 * @author lengleng
 * @date 2022/3/30
 */
@UtilityClass
public class MsgUtils {

    /**
     * 通过code 获取中文错误信息
     *
     * @param code
     * @return
     */
    public String getMessage(String code) {
        MessageSource messageSource = SpringContextHolder.getBean("pigMessageSource");
        return resolveMessage(messageSource, code, null);
    }

    /**
     * 通过code 和参数获取中文错误信息
     *
     * @param code
     * @return
     */
    public String getMessage(String code, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("pigMessageSource");
        return resolveMessage(messageSource, code, objects);
    }

    /**
     * 通过错误码和参数获取安全相关的中文错误信息
     *
     * @param code    错误码
     * @param objects 格式化参数
     * @return 格式化后的错误信息
     */
    public String getSecurityMessage(String code, Object... objects) {
        MessageSource messageSource = SpringContextHolder.getBean("securityMessageSource");
        return resolveMessage(messageSource, code, objects);
    }

    /**
     * 解析消息，根据当前区域设置获取消息，如果找不到消息则使用中国区域设置重试
     *
     * @param messageSource 消息源
     * @param code          消息代码
     * @param objects       消息参数
     * @return 解析后的消息
     */
    private String resolveMessage(MessageSource messageSource, String code, Object[] objects) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(code, objects, locale);
        } catch (NoSuchMessageException ex) {
            return messageSource.getMessage(code, objects, Locale.CHINA);
        }
    }

}
