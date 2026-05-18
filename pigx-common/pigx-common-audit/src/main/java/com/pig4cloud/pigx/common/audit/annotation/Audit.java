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

    /**
     * 主键表达式（SpEL）。配置后会通过 MyBatis-Plus Mapper selectById 查询切面执行前后的实体快照，
     * 解析结果必须实现 {@link java.io.Serializable}。
     *
     * @return 主键 SpEL 表达式
     * @since 6.0
     */
    String id() default "";

    /**
     * 显式指定 MyBatis-Plus Mapper 类型，必须是被 Spring 管理的 BaseMapper Bean。
     * 默认 {@code Void.class} 表示从当前 Service 的 {@code getBaseMapper()} 自动获取。
     *
     * @return Mapper 类型
     * @since 6.0
     */
    Class<?> mapper() default Void.class;

}
