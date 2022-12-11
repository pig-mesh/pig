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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.mapper.SysSocialDetailsMapper;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.constant.enums.LoginTypeEnum;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas30ProxyTicketValidator;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2018/11/18
 */
@Slf4j
@Component("CAS")
@AllArgsConstructor
public class CasLoginHandler extends AbstractLoginHandler {

	private final SysSocialDetailsMapper sysSocialDetailsMapper;

	private final SysUserService sysUserService;

	/**
	 * cas 回到的ticket
	 * <p>
	 * 通过ticket 调用CAS获取唯一标识
	 * @param ticket
	 * @return
	 */
	@Override
	@SneakyThrows
	public String identify(String ticket) {
		SysSocialDetails condition = new SysSocialDetails();
		condition.setType(LoginTypeEnum.CAS.getType());
		SysSocialDetails socialDetails = sysSocialDetailsMapper.selectOne(new QueryWrapper<>(condition));
		// remark 字段填写 CAS 服务器的URL
		Cas30ProxyTicketValidator cas30ProxyTicketValidator = new Cas30ProxyTicketValidator(socialDetails.getRemark());
		Assertion validate = cas30ProxyTicketValidator.validate(ticket, socialDetails.getRedirectUrl());
		return validate.getPrincipal().getName();
	}

	/**
	 * username 获取用户信息
	 * @param username
	 * @return
	 */
	@Override
	public UserInfo info(String username) {
		SysUser user = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));

		if (user == null) {
			log.info("CAS 不存在用户:{}", username);
			return null;
		}
		return sysUserService.findUserInfo(user);
	}

}
