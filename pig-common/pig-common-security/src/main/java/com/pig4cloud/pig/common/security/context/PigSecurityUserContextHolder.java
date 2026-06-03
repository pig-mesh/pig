/*
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pig.common.security.context;

import com.pig4cloud.pig.common.core.context.UserContext;
import com.pig4cloud.pig.common.core.context.UserContextHolder;
import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.util.SecurityUtils;

/**
 * 基于 Spring Security 上下文的 {@link UserContextHolder} 实现。
 *
 * @author lengleng
 */
public class PigSecurityUserContextHolder implements UserContextHolder {

	@Override
	public UserContext get() {
		PigUser user = SecurityUtils.getUser();
		if (user == null) {
			return null;
		}
		return new PigUserContextAdapter(user);
	}

}
