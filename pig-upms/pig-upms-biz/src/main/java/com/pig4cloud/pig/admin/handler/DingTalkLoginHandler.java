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

package com.pig4cloud.pig.admin.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.mapper.SysSocialDetailsMapper;
import com.pig4cloud.pig.admin.service.SysUserService;
import com.pig4cloud.pig.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 钉钉登录
 *
 * @author lengleng
 * @date 2023-10-14
 */
@Slf4j
@Component("DINGTALK")
@RequiredArgsConstructor
public class DingTalkLoginHandler extends AbstractLoginHandler {

	private final SysUserService sysUserService;

	private final SysSocialDetailsMapper sysSocialDetailsMapper;

	@Override
	public String identify(String code) {
		SysSocialDetails condition = new SysSocialDetails();
		condition.setType(LoginTypeEnum.DINGTALK.getType());
		SysSocialDetails socialDetails = sysSocialDetailsMapper.selectOne(new QueryWrapper<>(condition));

		String accessTokenResult = HttpUtil.post("https://api.dingtalk.com/v1.0/oauth2/userAccessToken",
				JSONUtil.createObj()
					.set("clientId", socialDetails.getAppId())
					.set("clientSecret", socialDetails.getAppSecret())
					.set("grantType", "authorization_code")
					.set("code", code)
					.toString());
		log.debug("获取钉钉Token响应报文：{}", accessTokenResult);

		String accessToken = JSONUtil.parseObj(accessTokenResult).getStr("accessToken");
		String userResult = HttpRequest.get("https://api.dingtalk.com/v1.0/contact/users/me")
			.header("x-acs-dingtalk-access-token", accessToken)
			.execute()
			.body();
		log.debug("获取钉钉UserId响应报文:{}", userResult);

		return JSONUtil.parseObj(userResult).getStr("openId");
	}

	@Override
	public UserInfo info(String openId) {
		if (StrUtil.isBlank(openId)) {
			log.warn("钉钉openId为空，无法获取用户信息");
			return null;
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setWxDingUserid(openId);

		R<UserInfo> userInfoR = sysUserService.getUserInfo(userDTO);
		if (userInfoR.getData() == null) {
			log.info("钉钉不存在用户:{}", openId);
			return null;
		}

		return userInfoR.getData();
	}

	@Override
	public Boolean bind(SysUser user, String identify) {
		user.setWxDingUserid(identify);
		sysUserService.updateById(user);
		return true;
	}

}
