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

package com.pig4cloud.pigx.common.gray.support;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * @author lengleng
 * @date 2020-10-16
 * <p>
 * 灰度版本号传递工具 ,在非web 调用feign 传递之前手动setVersion
 */
@UtilityClass
public class NonWebVersionContextHolder {

	private final ThreadLocal<String> THREAD_LOCAL_VERSION = new TransmittableThreadLocal<>();

	/**
	 * TTL 设置版本号<br/>
	 * @param version 版本号
	 */
	public void setVersion(String version) {
		THREAD_LOCAL_VERSION.set(version);
	}

	/**
	 * 获取TTL中的版本号
	 * @return 版本 || null
	 */
	public String getVersion() {
		return THREAD_LOCAL_VERSION.get();
	}

	public void clear() {
		THREAD_LOCAL_VERSION.remove();
	}

}
