package com.pig4cloud.pig.common.feign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试具体的策略
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Backoff {

	long delay() default 1000L;;

	long maxDelay() default 0L;

	double multiplier() default 0.0D;;

}
