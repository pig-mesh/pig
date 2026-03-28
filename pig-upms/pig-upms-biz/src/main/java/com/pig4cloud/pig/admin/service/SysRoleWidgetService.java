/*
 *
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
 *
 */

package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysRoleWidget;

/**
 * 角色首页组件配置 Service
 *
 * @author lengleng
 */
public interface SysRoleWidgetService extends IService<SysRoleWidget> {

	/**
	 * 根据角色ID查询widget配置
	 * @param roleId 角色ID
	 * @return widget配置，无配置时返回 null
	 */
	SysRoleWidget getByRoleId(Long roleId);

	/**
	 * 保存或更新角色widget配置
	 * @param sysRoleWidget widget配置
	 * @return 是否成功
	 */
	boolean saveOrUpdateByRoleId(SysRoleWidget sysRoleWidget);

}
