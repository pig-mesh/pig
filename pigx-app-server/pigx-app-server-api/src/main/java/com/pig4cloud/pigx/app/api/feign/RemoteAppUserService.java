/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
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

package com.pig4cloud.pigx.app.api.feign;

import com.pig4cloud.pigx.app.api.dto.AppUserInfo;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.feign.annotation.NoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lengleng
 * @date 2018/6/22
 */
@FeignClient(contextId = "remoteAppUserService", value = ServiceNameConstants.APP_SERVER)
public interface RemoteAppUserService {

	/**
	 * 通过用户名查询用户、角色信息
	 * @param username 用户名
	 * @param from 调用标志
	 * @return R
	 */
	@NoToken
	@GetMapping("/appuser/info/{username}")
	R<AppUserInfo> info(@PathVariable("username") String username);

	/**
	 * 通过社交账号或手机号查询用户、角色信息
	 * @param inStr appid@code
	 * @param from 调用标志
	 * @return
	 */
	@NoToken
	@GetMapping("/appsocial/info/{inStr}")
	R<AppUserInfo> social(@PathVariable("inStr") String inStr);


}
