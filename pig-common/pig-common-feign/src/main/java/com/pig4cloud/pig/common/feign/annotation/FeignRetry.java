package com.pig4cloud.pig.common.feign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试注解，作用在 @FeignClient 注解之上
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignRetry {

	Backoff backoff() default @Backoff();

	int maxAttempt() default 3;

	Class<? extends Throwable>[] include() default {};

}
