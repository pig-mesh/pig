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

import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.enums.UserTypeEnum;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.RetOps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;
import java.util.Optional;

/**
 * 用户详细信息 default
 *
 * @author lengleng
 */
@Slf4j
@Primary
@RequiredArgsConstructor
public class PigxDefaultUserDetailsServiceImpl implements PigxUserDetailsService {

    private final RemoteUserService remoteUserService;

    private final CacheManager cacheManager;

    /**
     * 用户密码登录
     *
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (Objects.nonNull(cache) && Objects.nonNull(cache.get(username))) {
            return getUserDetails(Optional.ofNullable(cache.get(username, UserInfo.class)));
        }

        R<UserInfo> result = remoteUserService.info(username);
        Optional<UserInfo> userInfoOptional = RetOps.of(result).getData();
        userInfoOptional.ifPresent(userInfo -> Objects.requireNonNull(cache).put(username, userInfo));
        return getUserDetails(userInfoOptional);
    }

    /**
     * 通过用户实体查询
     *
     * @param pigxUser user
     * @return
     */
    @Override
    public UserDetails loadUserByUser(PigxUser pigxUser) {
        // 避免 C端用户通过接口调用B端接口的安全问题
        if (UserTypeEnum.TOB.getStatus().equals(pigxUser.getUserType())) {
            return loadUserByUsername(pigxUser.getUsername());
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

}
