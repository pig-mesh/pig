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
package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysAuditLog;
import com.pig4cloud.pigx.admin.mapper.SysAuditLogMapper;
import com.pig4cloud.pigx.admin.service.SysAuditLogService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

/**
 * 审计记录表
 *
 * @author PIG
 * @date 2023-02-28 20:12:23
 */
@Service
public class SysAuditLogServiceImpl extends ServiceImpl<SysAuditLogMapper, SysAuditLog> implements SysAuditLogService {

	/**
	 * 分页查询审计日志（数据权限处理）
	 * @param page 分页条件
	 * @param sysAuditLog 查询条件
	 * @return page
	 */
	@Override
	public Page<SysAuditLog> getAuditsByScope(Page page, SysAuditLog sysAuditLog) {
		LambdaQueryWrapper<SysAuditLog> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StrUtil.isNotBlank(sysAuditLog.getAuditName()), SysAuditLog::getAuditName,
				sysAuditLog.getAuditName());
		wrapper.like(StrUtil.isNotBlank(sysAuditLog.getAuditField()), SysAuditLog::getAuditField,
				sysAuditLog.getAuditField());
		wrapper.like(StrUtil.isNotBlank(sysAuditLog.getCreateBy()), SysAuditLog::getCreateBy,
				sysAuditLog.getCreateBy());

		// 数据权限限制，只查询本人的审计日志
		DataScope dataScope = new DataScope();
		dataScope.setUsername(SecurityUtils.getUser().getUsername());
		return baseMapper.selectPageByScope(page, wrapper, dataScope);
	}

}
