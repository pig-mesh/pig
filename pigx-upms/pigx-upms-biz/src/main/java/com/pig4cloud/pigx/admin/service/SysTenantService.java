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

package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.dto.SysTenantUserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.entity.SysTenant;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;
import java.util.Map;

/**
 * 租户管理
 *
 * @author lengleng
 * @date 2019-05-15 15:55:41
 */
public interface SysTenantService extends IService<SysTenant> {

	/**
	 * 获取正常的租户列表
	 * @return 正常的租户列表
	 */
	List<SysTenant> getNormalTenant();

	/**
	 * 保存租户信息
	 * @param sysTenant 租户信息对象
	 * @return 保存是否成功
	 */
	Boolean saveTenant(SysTenant sysTenant);

	/**
	 * 修改租户信息
	 * @param sysTenant 租户信息对象
	 * @return 修改是否成功
	 */
	Boolean updateTenant(SysTenant sysTenant);

	/**
	 * 获取用户所属租户列表
	 *
	 * @return 用户所属租户列表
	 */
	List<SysTenant> getUserTenant();

	/**
	 * 获取用户租户分页信息
	 * @param page 分页参数
	 * @param userDTO 用户信息
	 * @return 用户租户分页结果
	 */
	Page getUserTenantPage(Page page, UserDTO userDTO);

	/**
	 * 移除租户用户
	 * @param tenantUserDTO 租户用户信息
	 * @return 是否移除成功
	 */
	Boolean removeTenantUser(SysTenantUserDTO tenantUserDTO);

	/**
	 * 根据用户信息查询租户用户列表
	 * @param userDTO 用户信息传输对象
	 * @return 租户用户列表
	 */
	List<SysUser> listTenantUser(UserDTO userDTO);

	/**
	 * 保存租户用户信息
	 * @param tenantUserDTO 租户用户信息DTO
	 * @return 保存是否成功
	 */
	Boolean saveTenantUser(SysTenantUserDTO tenantUserDTO);

	/**
	 * 获取租户角色列表
	 * @param userDTO 用户信息
	 * @return 租户角色列表
	 */
	Map<String, Object> listTenantOrg(UserDTO userDTO);

	/**
	 * 移除租户
	 * @param ids 租户ID列表
	 * @return 是否移除成功
	 */
	Boolean removeTenant(Long[] ids);

	/**
	 * 更新用户租户信息
	 * @param userDto 用户数据传输对象
	 * @return 更新结果
	 */
	R updateUserTenant(UserDTO userDto);

	/**
	 * 获取或更新当前用户的租户信息
	 * <p>
	 * 如果当前租户已过期或无效，则自动切换到用户的其他有效租户
	 * @return 当前有效的租户对象，如果无有效租户则返回null
	 */
	SysTenant getOrUpdateTenant();

}
