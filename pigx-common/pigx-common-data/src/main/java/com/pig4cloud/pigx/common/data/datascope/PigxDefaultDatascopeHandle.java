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

package com.pig4cloud.pigx.common.data.datascope;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.entity.SysDeptRelation;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteDataScopeService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2019-09-07
 * <p>
 * 默认data scope 判断处理器
 */
public class PigxDefaultDatascopeHandle implements DataScopeHandle {

	@Autowired
	private RemoteDataScopeService dataScopeService;

	/**
	 * 计算用户数据权限
	 * @param deptList
	 * @return
	 */
	@Override
	public Boolean calcScope(List<Long> deptList) {
		PigxUser user = SecurityUtils.getUser();
		List<String> roleIdList = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.filter(authority -> authority.startsWith(SecurityConstants.ROLE))
				.map(authority -> authority.split(StrUtil.UNDERLINE)[1]).collect(Collectors.toList());
		// 当前用户的角色为空
		if (CollectionUtil.isEmpty(roleIdList)) {
			return false;
		}
		SysRole role = dataScopeService.getRoleList(roleIdList).getData().stream()
				.min(Comparator.comparingInt(SysRole::getDsType)).orElse(null);
		// 角色有可能已经删除了
		if (role == null) {
			return false;
		}
		Integer dsType = role.getDsType();
		// 查询全部
		if (DataScopeTypeEnum.ALL.getType() == dsType) {
			return true;
		}
		// 自定义
		if (DataScopeTypeEnum.CUSTOM.getType() == dsType && StrUtil.isNotBlank(role.getDsScope())) {
			String dsScope = role.getDsScope();
			deptList.addAll(
					Arrays.stream(dsScope.split(StrUtil.COMMA)).map(Long::parseLong).collect(Collectors.toList()));
		}
		// 查询本级及其下级
		if (DataScopeTypeEnum.OWN_CHILD_LEVEL.getType() == dsType) {
			List<Long> deptIdList = dataScopeService.getDescendantList(user.getDeptId()).getData().stream()
					.map(SysDeptRelation::getDescendant).collect(Collectors.toList());
			deptList.addAll(deptIdList);
		}
		// 只查询本级
		if (DataScopeTypeEnum.OWN_LEVEL.getType() == dsType) {
			deptList.add(user.getDeptId());
		}
		return false;
	}

}
