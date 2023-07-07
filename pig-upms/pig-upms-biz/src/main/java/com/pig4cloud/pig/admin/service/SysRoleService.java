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
import com.pig4cloud.pig.admin.api.entity.SysRole;
import com.pig4cloud.pig.admin.api.vo.RoleExcelVO;
import com.pig4cloud.pig.admin.api.vo.RoleVO;
import com.pig4cloud.pig.common.core.util.R;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
public interface SysRoleService extends IService<SysRole> {

	/**
	 * 通过用户ID，查询角色信息
	 * @param userId
	 * @return
	 */
	List<SysRole> findRolesByUserId(Long userId);

	/**
	 * 根据角色ID 查询角色列表
	 * @param roleIdList 角色ID列表
	 * @param key 缓存key
	 * @return
	 */
	List<SysRole> findRolesByRoleIds(List<Long> roleIdList, String key);

	/**
	 * 通过角色ID，删除角色
	 * @param ids
	 * @return
	 */
	Boolean removeRoleByIds(Long[] ids);

	/**
	 * 根据角色菜单列表
	 * @param roleVo 角色&菜单列表
	 * @return
	 */
	Boolean updateRoleMenus(RoleVO roleVo);

	/**
	 * 导入角色
	 * @param excelVOList 角色列表
	 * @param bindingResult 错误信息列表
	 * @return ok fail
	 */
	R importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult);

	/**
	 * 查询全部的角色
	 * @return list
	 */
	List<RoleExcelVO> listRole();

}
