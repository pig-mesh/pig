package com.pig4cloud.pig.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 判断是否有权限
 *
 * @author lengleng
 * @date 2024/07/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface HasPermission {

	/**
	 * 权限字符串
	 * @return {@link String[] }
	 */
	String[] value();

}
