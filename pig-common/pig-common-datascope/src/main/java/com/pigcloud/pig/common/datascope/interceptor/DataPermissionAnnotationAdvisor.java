package com.pigcloud.pig.common.datascope.interceptor;

import com.pigcloud.pig.common.datascope.annotation.DataPermission;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * @author hccake
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class DataPermissionAnnotationAdvisor extends AbstractPointcutAdvisor {

	private final Advice advice;

	private final Pointcut pointcut;

	public DataPermissionAnnotationAdvisor() {
		this.advice = new DataPermissionAnnotationInterceptor();
		this.pointcut = buildPointcut();
	}

	protected Pointcut buildPointcut() {
		Pointcut cpc = new AnnotationMatchingPointcut(DataPermission.class, true);
		Pointcut mpc = new AnnotationMatchingPointcut(null, DataPermission.class, true);
		return new ComposablePointcut(cpc).union(mpc);
	}

}
