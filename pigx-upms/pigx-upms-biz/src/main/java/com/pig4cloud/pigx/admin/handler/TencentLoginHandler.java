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

package com.pig4cloud.pigx.admin.handler;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.mapper.SysSocialDetailsMapper;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.LoginTypeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2018/11/18
 */
@Slf4j
@Component("QQ")
@AllArgsConstructor
public class TencentLoginHandler extends AbstractLoginHandler {

	private final SysUserService sysUserService;

	private final SysSocialDetailsMapper sysSocialDetailsMapper;

	/**
	 * QQ登录传入code
	 * <p>
	 * 通过code 调用qq 获取唯一标识
	 * @param code
	 * @return
	 */
	@Override
	public String identify(String code) {
		SysSocialDetails condition = new SysSocialDetails();
		condition.setType(LoginTypeEnum.QQ.getType());
		SysSocialDetails socialDetails = sysSocialDetailsMapper.selectOne(new QueryWrapper<>(condition));

		String url = String.format(SecurityConstants.QQ_AUTHORIZATION_CODE_URL, socialDetails.getAppId(),
				socialDetails.getAppSecret(), code);
		String result = HttpUtil.get(url);
		log.debug("QQ响应报文:{}", result);

		String accessToken = JSONUtil.parseObj(result).getStr("access_token");
		String userUrl = String.format(SecurityConstants.QQ_USER_INFO_URL, accessToken);
		String resp = HttpUtil.get(userUrl);
		log.debug("QQ获取个人信息返回报文{}", resp);

		JSONObject userInfo = JSONUtil.parseObj(resp);
		// QQ唯一标识
		String openid = userInfo.getStr("openid");
		return openid;
	}

	/**
	 * openId 获取用户信息
	 * @param openId
	 * @return
	 */
	@Override
	public UserInfo info(String openId) {
		SysUser user = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getQqOpenid, openId));

		if (user == null) {
			log.info("QQ未绑定:{}", openId);
			return null;
		}
		return sysUserService.findUserInfo(user);
	}

	/**
	 * 绑定逻辑
	 * @param user 用户实体
	 * @param identify 渠道返回唯一标识
	 * @return
	 */
	@Override
	public Boolean bind(SysUser user, String identify) {
		user.setQqOpenid(identify);
		sysUserService.updateById(user);
		return true;
	}

}
