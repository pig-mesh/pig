/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.exception.TaskException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 定时任务rest反射实现
 *
 * @author 郑健楠
 */
@Slf4j
@AllArgsConstructor
@Component("restTaskInvok")
public class RestTaskInvok implements ITaskInvok {

	private final RestTaskUrlValidator restTaskUrlValidator;

	@Override
	public void invokMethod(SysJob sysJob) throws TaskException {
		String executePath = sysJob.getExecutePath();
		if (!restTaskUrlValidator.isAllowed(executePath)) {
			log.warn("定时任务REST地址未通过白名单校验，任务ID：{}", sysJob.getJobId());
			throw new TaskException("定时任务REST地址未通过白名单校验");
		}

		try {
			HttpRequest request = HttpUtil.createGet(executePath).setMaxRedirectCount(0);
			try (HttpResponse ignored = request.execute()) {
				// REST 任务仅关注请求是否成功发出，响应内容无需保留
			}
		}
		catch (Exception e) {
			log.error("定时任务restTaskInvok异常，任务ID：{}", sysJob.getJobId(), e);
			throw new TaskException("定时任务restTaskInvok业务执行失败，任务ID：" + sysJob.getJobId());
		}
	}

}
