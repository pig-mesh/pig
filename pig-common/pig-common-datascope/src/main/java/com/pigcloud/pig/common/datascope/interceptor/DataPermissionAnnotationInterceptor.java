package com.pigcloud.pig.common.datascope.interceptor;

import com.pigcloud.pig.common.datascope.annotation.DataPermission;
import com.pigcloud.pig.common.datascope.holder.DataPermissionAnnotationHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * DataPermission注解的拦截器，在执行方法前将当前方法的对应注解压栈，执行后弹出注解
 *
 * @author hccake
 */
public class DataPermissionAnnotationInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		// 当前方法
		Method method = methodInvocation.getMethod();
		// 获取执行类
		Object invocationThis = methodInvocation.getThis();
		Class<?> clazz = invocationThis != null ? invocationThis.getClass() : method.getDeclaringClass();
		// 寻找对应的 DataPermission 注解属性
		DataPermission dataPermission = DataPermissionFinder.findDataPermission(method, clazz);
		DataPermissionAnnotationHolder.push(dataPermission);
		try {
			return methodInvocation.proceed();
		}
		finally {
			DataPermissionAnnotationHolder.poll();
		}
	}

}
