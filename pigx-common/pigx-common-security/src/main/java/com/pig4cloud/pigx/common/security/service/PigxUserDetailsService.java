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
package com.pig4cloud.pigx.common.security.service;

import cn.hutool.core.util.ArrayUtil;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.UserTypeEnum;
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
public interface PigxUserDetailsService extends UserDetailsService, Ordered {

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
				.orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
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
        if (ArrayUtil.isNotEmpty(info.getRoles())) {
            // 获取角色
            Arrays.stream(info.getRoles()).forEach(roleId -> dbAuthsSet.add(SecurityConstants.ROLE + roleId));
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

        }
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUser user = info.getSysUser();

        // 构造security用户
        return new PigxUser(user.getUserId(), user.getUsername(), user.getDeptId(), user.getPhone(), user.getAvatar(),
                user.getNickname(), user.getName(), user.getEmail(), user.getTenantId(),
                SecurityConstants.BCRYPT + user.getPassword()
                , true, true, UserTypeEnum.TOB.getStatus()
                , !CommonConstants.STATUS_LOCK.equals(user.getPasswordExpireFlag()) // 密码过期判断
                , !CommonConstants.STATUS_LOCK.equals(user.getLockFlag()), authorities);
    }

    /**
     * 通过用户实体查询
     *
     * @param pigxUser user
     * @return
     */
    default UserDetails loadUserByUser(PigxUser pigxUser) {
        return this.loadUserByUsername(pigxUser.getUsername());
    }

}
