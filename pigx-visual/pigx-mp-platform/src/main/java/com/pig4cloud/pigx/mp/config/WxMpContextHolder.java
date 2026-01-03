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

package com.pig4cloud.pigx.mp.config;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * @author lengleng
 * @date 2019/03/27 微信上下文工具类
 */
@UtilityClass
public class WxMpContextHolder {

	private final ThreadLocal<String> THREAD_LOCAL_APPID = new TransmittableThreadLocal<>();

	/**
	 * TTL 设置appId
	 * @param appId
	 */
	public void setAppId(String appId) {
		THREAD_LOCAL_APPID.set(appId);
	}

	/**
	 * 获取TTL中的appId
	 * @return
	 */
	public String getAppId() {
		return THREAD_LOCAL_APPID.get();
	}

	public void clear() {
		THREAD_LOCAL_APPID.remove();
	}

}
