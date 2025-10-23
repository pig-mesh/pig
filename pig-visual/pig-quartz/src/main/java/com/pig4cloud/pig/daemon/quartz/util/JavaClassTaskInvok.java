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
import com.pig4cloud.pig.daemon.quartz.constants.PigQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 基于Java反射实现的定时任务调用类
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Component("javaClassTaskInvok")
@Slf4j
public class JavaClassTaskInvok implements ITaskInvok {

	/**
	 * 调用定时任务方法
	 * @param sysJob 定时任务信息
	 * @throws TaskException 执行任务过程中出现异常时抛出
	 */
	@Override
	public void invokMethod(SysJob sysJob) throws TaskException {
		// 安全验证：检查类名和方法名是否合法
		if (!ClassNameValidator.isValidClassName(sysJob.getClassName())) {
			log.error("定时任务类名验证失败，类名包含危险特征或在黑名单中：{}", sysJob.getClassName());
			throw new TaskException("定时任务类名验证失败，拒绝执行危险类：" + sysJob.getClassName());
		}

		if (!ClassNameValidator.isValidMethodName(sysJob.getMethodName())) {
			log.error("定时任务方法名验证失败，方法名包含危险特征：{}", sysJob.getMethodName());
			throw new TaskException("定时任务方法名验证失败，拒绝执行危险方法：" + sysJob.getMethodName());
		}

		Object obj;
		Class clazz;
		Method method;
		Object returnValue;
		try {
			if (StrUtil.isNotEmpty(sysJob.getMethodParamsValue())) {
				clazz = Class.forName(sysJob.getClassName());
				obj = clazz.getDeclaredConstructor().newInstance();
				method = clazz.getDeclaredMethod(sysJob.getMethodName(), String.class);
				returnValue = method.invoke(obj, sysJob.getMethodParamsValue());
			}
			else {
				clazz = Class.forName(sysJob.getClassName());
				obj = clazz.getDeclaredConstructor().newInstance();
				method = clazz.getDeclaredMethod(sysJob.getMethodName());
				returnValue = method.invoke(obj);
			}
			if (StrUtil.isEmpty(returnValue.toString())
					|| PigQuartzEnum.JOB_LOG_STATUS_FAIL.getType().equals(returnValue.toString())) {
				log.error("定时任务javaClassTaskInvok异常,执行任务：{}", sysJob.getClassName());
				throw new TaskException("定时任务javaClassTaskInvok业务执行失败,任务：" + sysJob.getClassName());
			}
		}
		catch (ClassNotFoundException e) {
			log.error("定时任务java反射类没有找到,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射类没有找到,执行任务：" + sysJob.getClassName());
		}
		catch (IllegalAccessException e) {
			log.error("定时任务java反射类异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射类异常,执行任务：" + sysJob.getClassName());
		}
		catch (InstantiationException e) {
			log.error("定时任务java反射类异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射类异常,执行任务：" + sysJob.getClassName());
		}
		catch (NoSuchMethodException e) {
			log.error("定时任务java反射执行方法名异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射执行方法名异常,执行任务：" + sysJob.getClassName());
		}
		catch (InvocationTargetException e) {
			log.error("定时任务java反射执行异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射执行异常,执行任务：" + sysJob.getClassName());
		}

	}

}
