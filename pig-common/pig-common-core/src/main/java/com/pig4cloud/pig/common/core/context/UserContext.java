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

package com.pig4cloud.pig.common.core.context;

import java.util.List;

/**
 * 当前登录用户的最小上下文，仅暴露通用审计能力所需字段。 安全实现独立于此接口，避免 data 等通用模块反向依赖 security。
 *
 * @author lengleng
 */
public interface UserContext {

	String getUsername();

	Long getDeptId();

	List<Long> getRoleIds();

}
