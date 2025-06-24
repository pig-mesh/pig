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
package com.pig4cloud.pigx.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.app.api.entity.AppRole;
import com.pig4cloud.pigx.app.api.vo.AppRoleExcelVO;
import com.pig4cloud.pigx.app.mapper.AppRoleMapper;
import com.pig4cloud.pigx.app.service.AppRoleService;
import com.pig4cloud.pigx.common.core.exception.ErrorCodes;
import com.pig4cloud.pigx.common.core.util.MsgUtils;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.vo.ErrorMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * app角色表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@Service
@AllArgsConstructor
public class AppRoleServiceImpl extends ServiceImpl<AppRoleMapper, AppRole> implements AppRoleService {

	@Override
	public List<AppRole> findRolesByUserId(Long userId) {
		return baseMapper.listRolesByUserId(userId);
	}

	/**
	 * 删除用户的同时，把role_menu关系删除
	 * @param ids roleIds
	 */
	@Override
	public Boolean deleteRoleByIds(Long[] ids) {
		this.removeBatchByIds(CollUtil.toList(ids));
		return Boolean.TRUE;
	}

	/**
	 * 导入角色
	 * @param excelVOList
	 * @param bindingResult
	 * @return
	 */
	@Override
	public R importRole(List<AppRoleExcelVO> excelVOList, BindingResult bindingResult) {
		// 通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

		// 个性化校验逻辑
		List<AppRole> roleList = this.list();

		// 执行数据插入操作 组装 RoleDto
		for (AppRoleExcelVO excel : excelVOList) {
			Set<String> errorMsg = new HashSet<>();
			// 检验角色名称或者角色编码是否存在
			boolean existRole = roleList.stream()
				.anyMatch(appRole -> excel.getRoleName().equals(appRole.getRoleName())
						|| excel.getRoleCode().equals(appRole.getRoleCode()));

			if (existRole) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_ROLE_NAMEORCODE_EXISTING, excel.getRoleName(),
						excel.getRoleCode()));
			}

			// 数据合法情况
			if (CollUtil.isEmpty(errorMsg)) {
				insertExcelRole(excel);
			}
			else {
				// 数据不合法情况
				errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
			}
		}
		if (CollUtil.isNotEmpty(errorMessageList)) {
			return R.failed(errorMessageList);
		}
		return R.ok();

	}

	private void insertExcelRole(AppRoleExcelVO excel) {
		AppRole appRole = new AppRole();
		appRole.setRoleName(excel.getRoleName());
		appRole.setRoleDesc(excel.getRoleDesc());
		appRole.setRoleCode(excel.getRoleCode());
		this.save(appRole);
	}

}
