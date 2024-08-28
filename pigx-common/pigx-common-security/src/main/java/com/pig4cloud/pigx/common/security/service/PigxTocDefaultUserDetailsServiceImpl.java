/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pigx.common.security.service;

import cn.hutool.core.util.ArrayUtil;
import com.pig4cloud.pigx.app.api.dto.AppUserInfo;
import com.pig4cloud.pigx.app.api.entity.AppUser;
import com.pig4cloud.pigx.app.api.feign.RemoteAppUserService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.UserTypeEnum;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.RetOps;
import com.pig4cloud.pigx.common.core.util.WebUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;

import java.util.*;

/**
 * 用户详细信息
 *
 * @author lengleng hccake
 */
@Slf4j
@RequiredArgsConstructor
public class PigxTocDefaultUserDetailsServiceImpl implements PigxUserDetailsService {

    private final CacheManager cacheManager;

    private final Optional<RemoteAppUserService> remoteAppUserServiceOptional;

    /**
     * 用户密码登录
     *
     * @param username 用户密码登录
     * @return
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS_MINI);
        if (Objects.nonNull(cache) && Objects.nonNull(cache.get(username))) {
            AppUserInfo appUserInfo = cache.get(username, AppUserInfo.class);
            return getUserDetailsAppUser(R.ok(appUserInfo));
        }

        R<AppUserInfo> result = remoteAppUserServiceOptional.get().info(username);
        Optional<AppUserInfo> userInfoOptional = RetOps.of(result).getData();
        userInfoOptional.ifPresent(userInfo -> Objects.requireNonNull(cache).put(username, userInfo));
        return getUserDetailsAppUser(result);
    }

    @Override
    public UserDetails loadUserByUser(PigxUser pigxUser) {
        return pigxUser;
    }

    UserDetails getUserDetailsAppUser(R<AppUserInfo> result) {
        // @formatter:off
		return RetOps.of(result)
				.assertSuccess(r -> new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "用户信息获取失败",null)))
				.getData()
				.map(this::convertUserDetailsAppUser)
				.orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
		// @formatter:on
    }

    /**
     * UserInfo 转 UserDetails
     *
     * @param info
     * @return 返回UserDetails对象
     */
    UserDetails convertUserDetailsAppUser(AppUserInfo info) {
        Set<String> dbAuthsSet = new HashSet<>();
        if (ArrayUtil.isNotEmpty(info.getRoles())) {
            // 获取角色
            Arrays.stream(info.getRoles()).forEach(roleId -> dbAuthsSet.add(SecurityConstants.ROLE + roleId));
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

        }
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(dbAuthsSet.toArray(new String[0]));
        AppUser user = info.getAppUser();
        // 构造security用户

        return new PigxUser(user.getUserId(), user.getUsername(), null, user.getPhone(), user.getAvatar(),
                user.getNickname(), user.getName(), user.getEmail(), user.getTenantId(),
                SecurityConstants.BCRYPT + user.getPassword(), true, true, UserTypeEnum.TOC.getStatus(), true,
                !CommonConstants.STATUS_LOCK.equals(user.getLockFlag()), authorities);
    }

    @Override
    public int getOrder() {
        return 10;
    }

    /**
     * 支持所有的 mobile 类型
     *
     * @param clientId  目标客户端
     * @param grantType 授权类型
     * @return true/false
     */
    @Override
    public boolean support(String clientId, String grantType) {
        if (Objects.isNull(WebUtils.getRequest())) {
            return false;
        }
        String header = WebUtils.getRequest().getHeader(SecurityConstants.HEADER_TOC);
        return SecurityConstants.HEADER_TOC_YES.equals(header);
    }

}
