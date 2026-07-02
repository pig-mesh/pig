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

package com.pig4cloud.pig.admin.api.feign;

import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.feign.annotation.NoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author lengleng
 * @date 2026-02-10
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteUserService {

	/**
	 * 通过用户名查询用户、角色信息
	 * @param username 用户名
	 * @return R
	 */
	@NoToken
	@GetMapping("/user/info/{username}")
	R<UserInfo> info(@PathVariable String username);

	/**
	 * 通过社交账号或手机号查询用户、角色信息
	 * @param inStr appid@code
	 * @return
	 */
	@NoToken
	@GetMapping("/social/info/{inStr}")
	R<UserInfo> social(@PathVariable String inStr);

	/**
	 * 锁定用户
	 * @param username 用户名
	 * @return
	 */
	@NoToken
	@PutMapping("/user/lock/{username}")
	R<Boolean> lockUser(@PathVariable String username);

}
