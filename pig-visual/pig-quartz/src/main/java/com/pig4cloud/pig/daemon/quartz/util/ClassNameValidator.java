/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pig.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 类名验证器，用于防止反射执行恶意类
 *
 * @author lengleng
 * @date 2025/10/22
 */
@Slf4j
public class ClassNameValidator {

	/**
	 * 危险类的黑名单 - 禁止使用的类和包
	 */
	private static final Set<String> DANGEROUS_CLASSES = new HashSet<>(Arrays.asList(
			// EL表达式处理器 - 可执行任意代码
			"jakarta.el.ELProcessor", "javax.el.ELProcessor",
			// 脚本引擎 - 可执行任意脚本
			"javax.script.ScriptEngineManager", "javax.script.ScriptEngine",
			// 反射相关 - 可绕过安全限制
			"java.lang.reflect.Method", "java.lang.reflect.Constructor", "java.lang.reflect.Field",
			// 类加载器 - 可加载恶意类
			"java.lang.ClassLoader", "java.net.URLClassLoader",
			// 运行时 - 可执行系统命令
			"java.lang.Runtime", "java.lang.ProcessBuilder", "java.lang.Process",
			// JNDI - 可进行远程代码执行
			"javax.naming.InitialContext", "javax.naming.Context",
			// RMI - 可进行远程代码执行
			"java.rmi.registry.Registry", "java.rmi.server.UnicastRemoteObject",
			// 序列化 - 可导致反序列化漏洞
			"java.io.ObjectInputStream", "java.io.ObjectOutputStream"));

	/**
	 * 危险包前缀黑名单
	 */
	private static final Set<String> DANGEROUS_PACKAGES = new HashSet<>(Arrays.asList("sun.", "com.sun.",
			"jdk.internal.", "java.lang.reflect.", "java.security.", "javax.naming.", "javax.script.", "java.rmi.",
			"javax.management.", "org.springframework.context.support.FileSystemXmlApplicationContext",
			"org.springframework.expression.spel."));

	/**
	 * 验证类名是否安全
	 * @param className 要验证的类名
	 * @return 如果类名安全返回true，否则返回false
	 */
	public static boolean isValidClassName(String className) {
		if (StrUtil.isBlank(className)) {
			log.warn("类名为空，验证失败");
			return false;
		}

		// 检查是否在危险类黑名单中
		if (DANGEROUS_CLASSES.contains(className)) {
			log.warn("类名 [{}] 在危险类黑名单中，拒绝执行", className);
			return false;
		}

		// 检查是否以危险包前缀开头
		for (String dangerousPackage : DANGEROUS_PACKAGES) {
			if (className.startsWith(dangerousPackage)) {
				log.warn("类名 [{}] 以危险包前缀 [{}] 开头，拒绝执行", className, dangerousPackage);
				return false;
			}
		}

		// 检查是否包含危险特征
		if (className.toLowerCase().contains("runtime") || className.toLowerCase().contains("processbuilder")
				|| className.toLowerCase().contains("elprocessor") || className.toLowerCase().contains("scriptengine")
				|| className.toLowerCase().contains("classloader")) {
			log.warn("类名 [{}] 包含危险特征，拒绝执行", className);
			return false;
		}

		return true;
	}

	/**
	 * 验证方法名是否安全
	 * @param methodName 要验证的方法名
	 * @return 如果方法名安全返回true，否则返回false
	 */
	public static boolean isValidMethodName(String methodName) {
		if (StrUtil.isBlank(methodName)) {
			log.warn("方法名为空，验证失败");
			return false;
		}

		// 检查方法名是否包含危险特征
		Set<String> dangerousMethods = new HashSet<>(Arrays.asList("exec", "eval", "execute", "invoke", "newInstance",
				"forName", "getRuntime", "loadClass", "defineClass", "getMethod", "getDeclaredMethod"));

		if (dangerousMethods.contains(methodName)) {
			log.warn("方法名 [{}] 在危险方法黑名单中，拒绝执行", methodName);
			return false;
		}

		return true;
	}

}
