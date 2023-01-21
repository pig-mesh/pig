package com.pig4cloud.pig.common.feign.retry;

import com.pig4cloud.pig.common.feign.annotation.FeignRetry;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * FeignRetry 注解切面注入 retryTemplate
 *
 * @author lengleng
 * @date 2023/1/21
 * {@link org.springframework.cloud.loadbalancer.blocking.retry.BlockingLoadBalancedRetryPolicy}.
 */
@Slf4j
@Aspect
@Component
public class FeignRetryAspect {

	@Around("@annotation(feignRetry)")
	public Object retry(ProceedingJoinPoint joinPoint, FeignRetry feignRetry) throws Throwable {
		Method method = getCurrentMethod(joinPoint);

		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setBackOffPolicy(prepareBackOffPolicy(feignRetry));
		retryTemplate.setRetryPolicy(prepareSimpleRetryPolicy(feignRetry));

		// 重试
		return retryTemplate.execute(arg0 -> {
			int retryCount = arg0.getRetryCount();
			log.info("Sending request method: {}, max attempt: {}, delay: {}, retryCount: {}", method.getName(),
					feignRetry.maxAttempt(), feignRetry.backoff().delay(), retryCount);
			return joinPoint.proceed(joinPoint.getArgs());
		});
	}

	/**
	 * 构造重试策略
	 * @param feignRetry 重试注解
	 * @return BackOffPolicy
	 */
	private BackOffPolicy prepareBackOffPolicy(FeignRetry feignRetry) {
		if (feignRetry.backoff().multiplier() != 0) {
			ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
			backOffPolicy.setInitialInterval(feignRetry.backoff().delay());
			backOffPolicy.setMaxInterval(feignRetry.backoff().maxDelay());
			backOffPolicy.setMultiplier(feignRetry.backoff().multiplier());
			return backOffPolicy;
		}
		else {
			FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
			fixedBackOffPolicy.setBackOffPeriod(feignRetry.backoff().delay());
			return fixedBackOffPolicy;
		}
	}

	/**
	 * 构造重试策略
	 * @param feignRetry 重试注解
	 * @return SimpleRetryPolicy
	 */
	private SimpleRetryPolicy prepareSimpleRetryPolicy(FeignRetry feignRetry) {
		Map<Class<? extends Throwable>, Boolean> policyMap = new HashMap<>();
		policyMap.put(RetryableException.class, true); // Connection refused or time out

		if (feignRetry.include().length != 0) {
			for (Class<? extends Throwable> t : feignRetry.include()) {
				policyMap.put(t, true);
			}
		}

		return new SimpleRetryPolicy(feignRetry.maxAttempt(), policyMap, true);
	}

	private Method getCurrentMethod(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getMethod();
	}

}
