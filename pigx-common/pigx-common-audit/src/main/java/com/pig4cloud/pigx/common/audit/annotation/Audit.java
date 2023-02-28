package com.pig4cloud.pigx.common.audit.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lengleng
 *
 * 记需要进行审计的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

	/**
	 * 此审计的名称
	 * @return 审计名称
	 */
	String name();

	@AliasFor("spel")
	String value() default "";

	/**
	 * 默认查询的表达式
	 * @return string
	 */
	@AliasFor("value")
	String spel() default "";

	/**
	 * 查询原有结果的表达式，如果为空取 spel()
	 * @return string
	 */
	String oldVal() default "";

	/**
	 * 查询编辑后的表达式，如果为空取 spel()
	 * @return string
	 */
	String newVal() default "";

}
