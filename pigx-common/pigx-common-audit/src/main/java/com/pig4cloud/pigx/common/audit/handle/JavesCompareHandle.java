package com.pig4cloud.pigx.common.audit.handle;

import com.pig4cloud.pigx.common.audit.annotation.Audit;
import com.pig4cloud.pigx.common.audit.support.AuditValueResolver;
import com.pig4cloud.pigx.common.audit.support.DataAuditor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.javers.core.Changes;

import java.util.Optional;

/**
 * javers 比较实现
 *
 * @author lengleng
 * @date 2023/2/26
 */
@RequiredArgsConstructor
public class JavesCompareHandle implements ICompareHandle {

	private final Optional<IAuditLogHandle> auditLogHandleOptional;

    private final AuditValueResolver auditValueResolver;

	/**
	 * 比较两个对象是否变更，及其变更后如何审计
	 * @param oldVal 原有值
	 */
	@Override
	public Changes compare(Object oldVal, ProceedingJoinPoint joinPoint, Audit audit) {
        Object newVal = auditValueResolver.resolveNewVal(joinPoint, audit);
		Changes compare = DataAuditor.compare(oldVal, newVal);
		auditLogHandleOptional.ifPresent(handle -> handle.handle(audit, compare));
		return compare;
	}

}
