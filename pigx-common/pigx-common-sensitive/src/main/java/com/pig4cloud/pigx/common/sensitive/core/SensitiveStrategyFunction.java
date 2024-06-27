package com.pig4cloud.pigx.common.sensitive.core;

/**
 * @author lengleng
 * @date 2024/6/27
 */
@FunctionalInterface
public interface SensitiveStrategyFunction<T, U, R> {

    R apply(T t, U u);
}
