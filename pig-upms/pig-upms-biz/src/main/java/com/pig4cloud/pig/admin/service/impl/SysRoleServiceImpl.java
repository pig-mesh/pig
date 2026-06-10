/*
 *
 *      Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.constant.UpmsErrorCodes;
import com.pig4cloud.pig.admin.api.entity.SysRole;
import com.pig4cloud.pig.admin.api.entity.SysRoleMenu;
import com.pig4cloud.pig.admin.api.vo.RoleExcelVO;
import com.pig4cloud.pig.admin.api.vo.RoleMenuVO;
import com.pig4cloud.pig.admin.mapper.SysRoleMapper;
import com.pig4cloud.pig.admin.service.SysRoleMenuService;
import com.pig4cloud.pig.admin.service.SysRoleService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.excel.vo.ErrorMessage;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	private SysRoleMenuService roleMenuService;

	/**
	 * 通过用户ID查询角色信息
	 * @param userId 用户ID
	 * @return 角色信息列表，无角色时返回空列表
	 */
	@Override
	public List<SysRole> findRolesByUserId(Long userId) {
		return baseMapper.listRolesByUserId(userId);
	}

	/**
	 * 根据角色ID列表查询角色，结果按缓存key缓存，空结果不缓存
	 * @param roleIdList 角色ID列表
	 * @param key 缓存key
	 * @return 匹配的角色列表，无匹配时返回空列表
	 */
	@Override
	@Cacheable(value = CacheConstants.ROLE_DETAILS, key = "#key", unless = "#result.isEmpty()")
	public List<SysRole> findRolesByRoleIds(List<Long> roleIdList, String key) {
		return baseMapper.selectByIds(roleIdList);
	}

	/**
	 * 通过角色ID批量删除角色，并先删除关联的角色菜单
	 * @param ids 角色ID数组
	 * @return 删除成功返回 true
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeRoleByIds(Long[] ids) {
		roleMenuService
			.remove(Wrappers.<SysRoleMenu>update().lambda().in(SysRoleMenu::getRoleId, CollUtil.toList(ids)));
		return this.removeBatchByIds(CollUtil.toList(ids));
	}

	/**
	 * 更新角色的菜单授权
	 * @param roleVo 角色及其菜单ID列表
	 * @return 更新成功返回 true
	 */
	@Override
	public Boolean updateRoleMenus(RoleMenuVO roleVo) {
		return roleMenuService.saveRoleMenus(roleVo.getRoleId(), roleVo.getMenuIds());
	}

	/**
	 * 导入角色，按角色名称或角色编码去重，重复数据不入库并收集错误信息
	 * @param excelVOList 待导入的角色列表
	 * @param bindingResult 通用校验结果，其 target 持有错误信息列表
	 * @return 全部导入成功返回 {@link R#ok()}；存在校验失败时返回携带错误信息列表的 {@link R#failed(Object)}
	 */
	@Override
	public R importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult) {
		// 通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

		// 个性化校验逻辑
		List<SysRole> roleList = this.list();

		// 执行数据插入操作 组装 RoleDto
		for (RoleExcelVO excel : excelVOList) {
			Set<String> errorMsg = new HashSet<>();
			// 检验角色名称或者角色编码是否存在
			boolean existRole = roleList.stream()
				.anyMatch(sysRole -> excel.getRoleName().equals(sysRole.getRoleName())
						|| excel.getRoleCode().equals(sysRole.getRoleCode()));

			if (existRole) {
				errorMsg.add(MsgUtils.getMessage(UpmsErrorCodes.SYS_ROLE_NAMEORCODE_EXISTING, excel.getRoleName(),
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

	/**
	 * 查询角色并转换为导出对象
	 * @param sysRole 查询条件
	 * @param ids 指定导出的角色ID数组，为空时按查询条件导出全部
	 * @return 角色导出对象列表
	 */
	@Override
	public List<RoleExcelVO> listRole(SysRole sysRole, Long[] ids) {
		List<SysRole> roleList = this.list(
				Wrappers.lambdaQuery(sysRole).in(ArrayUtil.isNotEmpty(ids), SysRole::getRoleId, CollUtil.toList(ids)));
		// 转换成execl 对象输出
		return roleList.stream().map(role -> {
			RoleExcelVO roleExcelVO = new RoleExcelVO();
			BeanUtil.copyProperties(role, roleExcelVO);
			return roleExcelVO;
		}).toList();
	}

	/**
	 * 插入excel Role
	 */
	private void insertExcelRole(RoleExcelVO excel) {
		SysRole sysRole = new SysRole();
		sysRole.setRoleName(excel.getRoleName());
		sysRole.setRoleDesc(excel.getRoleDesc());
		sysRole.setRoleCode(excel.getRoleCode());
		this.save(sysRole);
	}

}
