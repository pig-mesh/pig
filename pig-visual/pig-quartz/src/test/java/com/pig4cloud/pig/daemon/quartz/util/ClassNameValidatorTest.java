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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassNameValidator 测试类
 *
 * @author lengleng
 * @date 2025/10/22
 */
class ClassNameValidatorTest {

	@Test
	void testDangerousClassNames() {
		// 测试危险的EL处理器类
		assertFalse(ClassNameValidator.isValidClassName("jakarta.el.ELProcessor"),
				"jakarta.el.ELProcessor should be blocked");
		assertFalse(ClassNameValidator.isValidClassName("javax.el.ELProcessor"),
				"javax.el.ELProcessor should be blocked");

		// 测试脚本引擎类
		assertFalse(ClassNameValidator.isValidClassName("javax.script.ScriptEngineManager"),
				"ScriptEngineManager should be blocked");

		// 测试运行时类
		assertFalse(ClassNameValidator.isValidClassName("java.lang.Runtime"), "Runtime should be blocked");
		assertFalse(ClassNameValidator.isValidClassName("java.lang.ProcessBuilder"),
				"ProcessBuilder should be blocked");

		// 测试反射类
		assertFalse(ClassNameValidator.isValidClassName("java.lang.reflect.Method"), "Method should be blocked");

		// 测试类加载器
		assertFalse(ClassNameValidator.isValidClassName("java.lang.ClassLoader"), "ClassLoader should be blocked");

		// 测试JNDI类
		assertFalse(ClassNameValidator.isValidClassName("javax.naming.InitialContext"),
				"InitialContext should be blocked");
	}

	@Test
	void testDangerousPackages() {
		// 测试危险包前缀
		assertFalse(ClassNameValidator.isValidClassName("sun.misc.Unsafe"), "sun.* packages should be blocked");
		assertFalse(ClassNameValidator.isValidClassName("com.sun.jndi.rmi.registry.RegistryContext"),
				"com.sun.* packages should be blocked");
		assertFalse(ClassNameValidator.isValidClassName("jdk.internal.reflect.Reflection"),
				"jdk.internal.* packages should be blocked");
	}

	@Test
	void testValidClassNames() {
		// 测试合法的类名
		assertTrue(ClassNameValidator.isValidClassName("com.pig4cloud.pig.daemon.quartz.util.TaskUtil"),
				"Valid application class should be allowed");
		assertTrue(ClassNameValidator.isValidClassName("com.example.MyTask"), "Valid custom class should be allowed");
		assertTrue(ClassNameValidator.isValidClassName("org.apache.commons.lang3.StringUtils"),
				"Valid utility class should be allowed");
	}

	@Test
	void testInvalidInputs() {
		// 测试空输入
		assertFalse(ClassNameValidator.isValidClassName(null), "Null class name should be invalid");
		assertFalse(ClassNameValidator.isValidClassName(""), "Empty class name should be invalid");
		assertFalse(ClassNameValidator.isValidClassName("   "), "Blank class name should be invalid");
	}

	@Test
	void testDangerousMethodNames() {
		// 测试危险方法名
		assertFalse(ClassNameValidator.isValidMethodName("eval"), "eval method should be blocked");
		assertFalse(ClassNameValidator.isValidMethodName("exec"), "exec method should be blocked");
		assertFalse(ClassNameValidator.isValidMethodName("execute"), "execute method should be blocked");
		assertFalse(ClassNameValidator.isValidMethodName("invoke"), "invoke method should be blocked");
		assertFalse(ClassNameValidator.isValidMethodName("getRuntime"), "getRuntime method should be blocked");
	}

	@Test
	void testValidMethodNames() {
		// 测试合法方法名
		assertTrue(ClassNameValidator.isValidMethodName("run"), "Valid method name should be allowed");
		assertTrue(ClassNameValidator.isValidMethodName("process"), "Valid method name should be allowed");
		assertTrue(ClassNameValidator.isValidMethodName("doTask"), "Valid method name should be allowed");
		assertTrue(ClassNameValidator.isValidMethodName("handleJob"), "Valid method name should be allowed");
	}

	@Test
	void testMethodNameInvalidInputs() {
		// 测试方法名空输入
		assertFalse(ClassNameValidator.isValidMethodName(null), "Null method name should be invalid");
		assertFalse(ClassNameValidator.isValidMethodName(""), "Empty method name should be invalid");
		assertFalse(ClassNameValidator.isValidMethodName("   "), "Blank method name should be invalid");
	}

	@Test
	void testCaseInsensitiveDangerousPatterns() {
		// 测试大小写不敏感的危险模式检测
		assertFalse(ClassNameValidator.isValidClassName("com.example.MyRuntimeClass"),
				"Class name containing 'runtime' should be blocked");
		assertFalse(ClassNameValidator.isValidClassName("com.example.ELProcessorHelper"),
				"Class name containing 'elprocessor' should be blocked");
		assertFalse(ClassNameValidator.isValidClassName("com.example.MyClassLoader"),
				"Class name containing 'classloader' should be blocked");
	}

}
