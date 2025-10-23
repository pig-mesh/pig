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

import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.exception.TaskException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JavaClassTaskInvok 安全测试类
 * 验证RCE漏洞已被修复
 *
 * @author lengleng
 * @date 2025/10/22
 */
class JavaClassTaskInvokSecurityTest {

	private final JavaClassTaskInvok taskInvok = new JavaClassTaskInvok();

	@Test
	void testBlockELProcessorExploit() {
		// 测试原始漏洞：jakarta.el.ELProcessor
		SysJob maliciousJob = new SysJob();
		maliciousJob.setJobType("1");
		maliciousJob.setClassName("jakarta.el.ELProcessor");
		maliciousJob.setMethodName("eval");
		maliciousJob.setMethodParamsValue("Runtime.getRuntime().exec(\"ping -c 1 example.com\")");

		// 验证该恶意任务会被阻止
		TaskException exception = assertThrows(TaskException.class, () -> {
			taskInvok.invokMethod(maliciousJob);
		});

		assertTrue(exception.getMessage().contains("类名验证失败") || exception.getMessage().contains("拒绝执行危险类"));
	}

	@Test
	void testBlockRuntimeExec() {
		// 测试Runtime.exec()攻击
		SysJob maliciousJob = new SysJob();
		maliciousJob.setJobType("1");
		maliciousJob.setClassName("java.lang.Runtime");
		maliciousJob.setMethodName("exec");
		maliciousJob.setMethodParamsValue("calc");

		// 验证该恶意任务会被阻止
		TaskException exception = assertThrows(TaskException.class, () -> {
			taskInvok.invokMethod(maliciousJob);
		});

		assertTrue(exception.getMessage().contains("类名验证失败") || exception.getMessage().contains("拒绝执行危险类"));
	}

	@Test
	void testBlockProcessBuilder() {
		// 测试ProcessBuilder攻击
		SysJob maliciousJob = new SysJob();
		maliciousJob.setJobType("1");
		maliciousJob.setClassName("java.lang.ProcessBuilder");
		maliciousJob.setMethodName("start");

		// 验证该恶意任务会被阻止
		TaskException exception = assertThrows(TaskException.class, () -> {
			taskInvok.invokMethod(maliciousJob);
		});

		assertTrue(exception.getMessage().contains("类名验证失败") || exception.getMessage().contains("拒绝执行危险类"));
	}

	@Test
	void testBlockScriptEngine() {
		// 测试ScriptEngine攻击
		SysJob maliciousJob = new SysJob();
		maliciousJob.setJobType("1");
		maliciousJob.setClassName("javax.script.ScriptEngineManager");
		maliciousJob.setMethodName("getEngineByName");

		// 验证该恶意任务会被阻止
		TaskException exception = assertThrows(TaskException.class, () -> {
			taskInvok.invokMethod(maliciousJob);
		});

		assertTrue(exception.getMessage().contains("类名验证失败") || exception.getMessage().contains("拒绝执行危险类"));
	}

	@Test
	void testBlockDangerousMethod() {
		// 测试危险方法名被阻止（即使类名看起来安全）
		SysJob maliciousJob = new SysJob();
		maliciousJob.setJobType("1");
		maliciousJob.setClassName("com.example.SafeClass");
		maliciousJob.setMethodName("eval"); // 危险方法名

		// 验证该恶意任务会被阻止
		TaskException exception = assertThrows(TaskException.class, () -> {
			taskInvok.invokMethod(maliciousJob);
		});

		assertTrue(exception.getMessage().contains("方法名验证失败") || exception.getMessage().contains("拒绝执行危险方法"));
	}

	@Test
	void testBlockClassLoader() {
		// 测试ClassLoader攻击
		SysJob maliciousJob = new SysJob();
		maliciousJob.setJobType("1");
		maliciousJob.setClassName("java.lang.ClassLoader");
		maliciousJob.setMethodName("loadClass");

		// 验证该恶意任务会被阻止
		TaskException exception = assertThrows(TaskException.class, () -> {
			taskInvok.invokMethod(maliciousJob);
		});

		assertTrue(exception.getMessage().contains("类名验证失败") || exception.getMessage().contains("拒绝执行危险类"));
	}

	@Test
	void testBlockJNDI() {
		// 测试JNDI注入攻击
		SysJob maliciousJob = new SysJob();
		maliciousJob.setJobType("1");
		maliciousJob.setClassName("javax.naming.InitialContext");
		maliciousJob.setMethodName("lookup");

		// 验证该恶意任务会被阻止
		TaskException exception = assertThrows(TaskException.class, () -> {
			taskInvok.invokMethod(maliciousJob);
		});

		assertTrue(exception.getMessage().contains("类名验证失败") || exception.getMessage().contains("拒绝执行危险类"));
	}

}
