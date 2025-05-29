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

package com.pig4cloud.pigx.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.app.api.entity.AppPageEntity;
import com.pig4cloud.pigx.app.api.entity.AppRole;
import com.pig4cloud.pigx.app.api.vo.AppRoleExcelVO;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * app角色表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
public interface AppRoleService extends IService<AppRole> {

	/**
	 * 根据用户ID查询角色列表
	 *
	 * @param userId 用户ID
	 * @return 对应用户ID的角色列表
	 */
	List<AppRole> findRolesByUserId(Long userId);

	/**
	 * 删除用户的同时，把role_menu关系删除
	 * @param ids RoleIds
	 */
	Boolean deleteRoleByIds(Long[] ids);

	/**
	 * 导入角色信息方法
	 *
	 * @param excelVOList   角色信息的Excel视图对象列表
	 * @param bindingResult 数据绑定结果，用于校验
	 */
	R importRole(List<AppRoleExcelVO> excelVOList, BindingResult bindingResult);

	/**
	 * 获取列表菜单对象
	 *
	 * @return 列表菜单对象的实例
	 */
	AppPageEntity listMenu();
}
