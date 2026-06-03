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
package com.pig4cloud.pig.common.security.service;

import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.entity.SysPost;
import com.pig4cloud.pig.admin.api.entity.SysRole;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.constant.enums.UserTypeEnum;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

/**
 * @author lengleng
 * @date 2018/8/15
 */
public interface PigUserDetailsService extends UserDetailsService, Ordered {

    /**
     * Notfound 用户错误代码
     */
    String NOTFOUND_USER_ERROR_CODE = "UserDetailsService.notFound";

    /**
     * 是否支持此客户端校验
     *
     * @param clientId  请求客户端
     * @param grantType 授权类型
     * @return true/false
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    /**
     * 排序值 默认取最大的
     *
     * @return 排序值
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 获取用户详细信息
     *
     * @param userInfoOptional 用户信息：可选
     * @return {@link UserDetails }
     */
    default UserDetails getUserDetails(Optional<UserInfo> userInfoOptional) {
        // @formatter:off
		return  userInfoOptional
				.map(this::convertUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException(MsgUtils.getSecurityMessage(NOTFOUND_USER_ERROR_CODE)));
		// @formatter:on
    }

    /**
     * UserInfo 转 UserDetails
     *
     * @param info
     * @return 返回UserDetails对象
     */
    default UserDetails convertUserDetails(UserInfo info) {
        Set<String> dbAuthsSet = new HashSet<>();

        // 维护角色列表
        info.getRoleList().forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role.getRoleId()));

        // 维护权限列表
        dbAuthsSet.addAll(info.getPermissions());
        Collection<GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(dbAuthsSet.toArray(new String[0]));

        return new PigUser(info.getUserId(), info.getUsername(),
                info.getRoleList().stream().map(SysRole::getRoleId).toList(),
                info.getDeptId(),
                info.getDeptList().stream().map(SysDept::getDeptId).toList(),
                info.getPostList().stream().map(SysPost::getPostId).toList(), info.getPhone(), info.getAvatar(),
                info.getNickname(), info.getName(), info.getEmail(),
                SecurityConstants.BCRYPT + info.getPassword(), true, true, UserTypeEnum.TOB.getStatus(),
                !CommonConstants.STATUS_LOCK.equals(info.getPasswordExpireFlag()) // 密码过期判断
                , Objects.isNull(info.getPasswordModifyTime()) ? info.getCreateTime() : info.getPasswordModifyTime(), !CommonConstants.STATUS_LOCK.equals(info.getLockFlag()), authorities);
    }

    /**
     * 通过用户实体查询
     *
     * @param pigUser user
     * @return
     */
    default UserDetails loadUserByUser(PigUser pigUser) {
        return this.loadUserByUsername(pigUser.getUsername());
    }

}
