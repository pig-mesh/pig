package com.pig4cloud.pig.common.feign.annotation;

import java.lang.annotation.*;

/**
 * 服务无token调用声明注解
 * <p>
 * 只有发起方没有 token 时候才需要添加此注解，  @NoToken + @Inner
 * <p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoToken {
}
