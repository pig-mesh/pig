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

package com.pig4cloud.pig.common.security.util;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.service.PigUser;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {

	/**
	 * 获取用户
	 */
	public PigUser getUser() {
		Object loginId = StpUtil.getLoginId();
		UserDTO userInfo = new UserDTO();
		userInfo.setUsername(loginId.toString());
		R<UserInfo> info = SpringUtil.getBean(RemoteUserService.class).info(userInfo);

		UserInfo data = info.getData();
		PigUser pigUser = new PigUser();
		pigUser.setUsername(loginId.toString());
		pigUser.setId(data.getSysUser().getUserId());
		pigUser.setDeptId(data.getSysUser().getDeptId());
		return pigUser;
	}

	/**
	 * 获取用户角色信息
	 * @return 角色集合
	 */
	public List<Long> getRoles() {
		Object loginId = StpUtil.getLoginId();
		UserDTO userInfo = new UserDTO();
		userInfo.setUsername(loginId.toString());
		R<UserInfo> info = SpringUtil.getBean(RemoteUserService.class).info(userInfo);
		return CollUtil.newArrayList(info.getData().getRoles());
	}

}
