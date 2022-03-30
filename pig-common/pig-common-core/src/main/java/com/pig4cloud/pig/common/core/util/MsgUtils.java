package com.pig4cloud.pig.common.core.util;

import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;

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
	 * @param code
	 * @return
	 */
	public String getMessage(String code) {
		MessageSource messageSource = SpringUtil.getBean("messageSource");
		return messageSource.getMessage(code, null, Locale.CHINA);
	}

	/**
	 * 通过code 和参数获取中文错误信息
	 * @param code
	 * @return
	 */
	public String getMessage(String code, Object... objects) {
		MessageSource messageSource = SpringUtil.getBean("messageSource");
		return messageSource.getMessage(code, objects, Locale.CHINA);
	}

}
