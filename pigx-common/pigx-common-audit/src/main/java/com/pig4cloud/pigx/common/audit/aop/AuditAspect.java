package com.pig4cloud.pigx.common.audit.aop;

import com.pig4cloud.pigx.common.audit.annotation.Audit;
import com.pig4cloud.pigx.common.audit.handle.ICompareHandle;
import com.pig4cloud.pigx.common.audit.support.AuditValueResolver;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author lengleng
 * @date 2023/2/26
 *
 * 拦截被@Audit注解标记的方法，并记录前后变化值
 */

@Aspect
@RequiredArgsConstructor
public class AuditAspect {

	private final ICompareHandle compareHandle;

    private final AuditValueResolver auditValueResolver;

	@Around("@annotation(audit)")
	public Object auditLog(ProceedingJoinPoint joinPoint, Audit audit) throws Throwable {
		// 获取变更之前的结果
        Object oldVal = auditValueResolver.resolveOldVal(joinPoint, audit);
		Object result = joinPoint.proceed();

		compareHandle.compare(oldVal, joinPoint, audit);
		return result;
	}

}
