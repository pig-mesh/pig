package com.pig4cloud.pig.common.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解：用于方法级别的权限控制
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@pms.hasPermission('{value}'.split(','))")
public @interface HasPermission {

	/**
	 * 权限字符串
	 * @return {@link String[] }
	 */
	String[] value();

}
