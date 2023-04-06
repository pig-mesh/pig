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

package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysAuditLog;

/**
 * 审计记录表
 *
 * @author PIG
 * @date 2023-02-28 20:12:23
 */
public interface SysAuditLogService extends IService<SysAuditLog> {

	/**
	 * 分页查询审计日志（数据权限处理）
	 * @param page 分页条件
	 * @param sysAuditLog 查询条件
	 * @return page
	 */
	Page<SysAuditLog> getAuditsByScope(Page page, SysAuditLog sysAuditLog);

}
