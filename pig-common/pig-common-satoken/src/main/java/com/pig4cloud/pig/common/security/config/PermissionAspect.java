package com.pig4cloud.pig.common.security.config;

import cn.dev33.satoken.stp.StpUtil;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author lengleng
 * @date 2024/7/22
 */
@Aspect
public class PermissionAspect {

	@Around("@annotation(hasPermission)")
	public Object checkPermission(ProceedingJoinPoint joinPoint, HasPermission hasPermission) throws Throwable {
		// 获取权限字符串
		String[] permissions = hasPermission.value();

		// 调用 @SaCheckPermission 的逻辑
		StpUtil.checkPermissionOr(permissions);
		// 继续执行原方法
		return joinPoint.proceed();
	}

}
