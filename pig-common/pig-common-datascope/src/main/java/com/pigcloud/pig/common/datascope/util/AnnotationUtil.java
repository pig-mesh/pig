package com.pigcloud.pig.common.datascope.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Hccake 2021/1/27
 * @version 1.0
 */
public final class AnnotationUtil {

	private AnnotationUtil() {
	}

	/**
	 * 获取数据权限注解 优先获取方法上的注解，再获取类上的注解
	 * @param mappedStatementId 类名.方法名
	 * @return 数据权限注解
	 */
	public static <A extends Annotation> A findAnnotationByMappedStatementId(String mappedStatementId,
			Class<A> aClass) {
		if (mappedStatementId == null || "".equals(mappedStatementId)) {
			return null;
		}
		// 1.得到类路径和方法路径
		int lastIndexOfDot = mappedStatementId.lastIndexOf(".");
		if (lastIndexOfDot < 0) {
			return null;
		}
		String className = mappedStatementId.substring(0, lastIndexOfDot);
		String methodName = mappedStatementId.substring(lastIndexOfDot + 1);
		if ("".equals(className) || "".equals(methodName)) {
			return null;
		}

		// 2.字节码
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (clazz == null) {
			return null;
		}

		A annotation = null;
		// 3.得到方法上的注解
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (methodName.equals(name)) {
				annotation = method.getAnnotation(aClass);
				break;
			}
		}
		if (annotation == null) {
			annotation = clazz.getAnnotation(aClass);
		}
		return annotation;
	}

}
