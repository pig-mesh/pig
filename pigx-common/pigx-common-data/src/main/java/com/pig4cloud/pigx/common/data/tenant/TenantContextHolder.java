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

package com.pig4cloud.pigx.common.data.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * @author lengleng
 * @date 2018/10/4 租户工具类
 */
@UtilityClass
public class TenantContextHolder {

	private final ThreadLocal<Long> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

	private final ThreadLocal<Boolean> THREAD_LOCAL_TENANT_SKIP_FLAG = new TransmittableThreadLocal<>();

	/**
	 * TTL 设置租户ID<br/>
	 * <b>谨慎使用此方法,避免嵌套调用。尽量使用 {@code TenantBroker} </b>
	 * @param tenantId
	 * @see TenantBroker
	 */
	public void setTenantId(Long tenantId) {
		THREAD_LOCAL_TENANT.set(tenantId);
	}

	/**
	 * 设置是否过滤的标识
	 */
	public void setTenantSkip() {
		THREAD_LOCAL_TENANT_SKIP_FLAG.set(Boolean.TRUE);
	}

	/**
	 * 获取TTL中的租户ID
	 * @return
	 */
	public Long getTenantId() {
		return THREAD_LOCAL_TENANT.get();
	}

	/**
	 * 获取是否跳过租户过滤的标识
	 * @return
	 */
	public Boolean getTenantSkip() {
		return THREAD_LOCAL_TENANT_SKIP_FLAG.get() != null && THREAD_LOCAL_TENANT_SKIP_FLAG.get();
	}

	public void clear() {
		THREAD_LOCAL_TENANT.remove();
		THREAD_LOCAL_TENANT_SKIP_FLAG.remove();
	}

}
