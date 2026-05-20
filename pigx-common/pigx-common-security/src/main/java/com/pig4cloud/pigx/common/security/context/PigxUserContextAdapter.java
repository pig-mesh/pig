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

package com.pig4cloud.pigx.common.security.context;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.context.UserContext;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Objects;

/**
 * 将 {@link PigxUser} 适配为通用 {@link UserContext}，
 * 从 authorities 中按 {@link SecurityConstants#ROLE} 前缀解析 roleIds，
 * 与改造前 {@code PigxDefaultDataScopeHandle} 的行为保持一致。
 *
 * @author lengleng
 */
public class PigxUserContextAdapter implements UserContext {

	private final PigxUser delegate;

	public PigxUserContextAdapter(PigxUser delegate) {
		this.delegate = delegate;
	}

	@Override
	public String getUsername() {
		return delegate.getUsername();
	}

	@Override
	public Long getDeptId() {
		return delegate.getDeptId();
	}

	@Override
	public List<Long> getRoleIds() {
		return delegate.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.filter(authority -> StrUtil.startWith(authority, SecurityConstants.ROLE))
				.map(authority -> Long.parseLong(Objects.requireNonNull(StrUtil.removePrefix(authority, SecurityConstants.ROLE))))
				.toList();
	}

}
