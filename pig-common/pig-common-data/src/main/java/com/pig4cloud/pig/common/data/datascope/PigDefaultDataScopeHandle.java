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

package com.pig4cloud.pig.common.data.datascope;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.entity.SysRole;
import com.pig4cloud.pig.admin.api.feign.RemoteDataScopeService;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.constant.enums.UserTypeEnum;
import com.pig4cloud.pig.common.core.util.RetOps;
import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author lengleng
 * @date 2019-09-07
 * <p>
 * 默认data scope 判断处理器
 */

/**
 * @author lengleng
 * @date 2019-09-07
 * <p>
 * 默认data scope 判断处理器
 */
@RequiredArgsConstructor
public class PigDefaultDataScopeHandle implements DataScopeHandle {

    private final RemoteDataScopeService dataScopeService;

    @Override
    public Boolean calcScope(DataScope dataScope) {
        // 对于TOC客户端，不进行数据权限校验
        PigUser user = SecurityUtils.getUser();
        if (UserTypeEnum.TOC.getStatus().equals(user.getUserType())) {
            return true;
        }

        // 业务代码里的规则，覆盖计算规则
        if (StrUtil.isNotBlank(dataScope.getUsername()) || CollUtil.isNotEmpty(dataScope.getDeptList())) {
            return false;
        }

        // 获取用户角色ID列表
        List<String> roleIdList = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith(SecurityConstants.ROLE))
                .map(authority -> authority.split(StrUtil.UNDERLINE)[1])
                .toList();
        if (CollUtil.isEmpty(roleIdList)) {
            return false;
        }

        // 获取角色列表
        List<SysRole> roleList = RetOps.of(dataScopeService.getRoleList(roleIdList))
                .getData()
                .orElseGet(Collections::emptyList);
        if (CollUtil.isEmpty(roleList)) {
            return false;
        }

        // 处理数据权限
        return processDataScope(user, dataScope, roleList);
    }

    /**
     * 处理数据权限
     */
    private boolean processDataScope(PigUser user, DataScope dataScope, List<SysRole> roleList) {
        List<Long> deptList = dataScope.getDeptList();

        for (SysRole role : roleList) {
            Integer dsType = role.getDsType();

            // 处理不同数据权限类型
            switch (Objects.requireNonNull(DataScopeTypeEnum.getByType(dsType))) {
                case ALL:
                    return true;
                case CUSTOM:
                    handleCustomScope(role, deptList);
                    break;
                case OWN_CHILD_LEVEL:
                    handleOwnChildLevelScope(user, deptList);
                    break;
                case OWN_LEVEL:
                    handleOwnLevelScope(user, deptList);
                    break;
                case SELF_LEVEL:
                    handleSelfLevelScope(user, dataScope);
                    break;
                default:
                    break;
            }
        }

        return false;
    }

    /**
     * 处理自定义数据权限
     */
    private void handleCustomScope(SysRole role, List<Long> deptList) {
        if (StrUtil.isNotBlank(role.getDsScope())) {
            deptList.addAll(Arrays.stream(role.getDsScope().split(StrUtil.COMMA)).map(Long::parseLong).toList());
        }
    }

    /**
     * 处理本级及下级数据权限
     */
    private void handleOwnChildLevelScope(PigUser user, List<Long> deptList) {
        List<Long> descendantDeptIds = RetOps.of(dataScopeService.getDescendantList(user.getDeptId()))
                .getData()
                .orElseGet(Collections::emptyList)
                .stream()
                .map(SysDept::getDeptId)
                .toList();
        deptList.addAll(descendantDeptIds);
    }

    /**
     * 处理本级数据权限
     */
    private void handleOwnLevelScope(PigUser user, List<Long> deptList) {
        deptList.add(user.getDeptId());
    }

    /**
     * 处理个人数据权限
     */
    private void handleSelfLevelScope(PigUser user, DataScope dataScope) {
        dataScope.setUsername(user.getUsername());
    }

}
