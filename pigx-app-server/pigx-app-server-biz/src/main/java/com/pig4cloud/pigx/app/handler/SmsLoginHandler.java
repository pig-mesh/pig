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

package com.pig4cloud.pigx.app.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.app.api.dto.AppUserInfo;
import com.pig4cloud.pigx.app.api.entity.AppUser;
import com.pig4cloud.pigx.app.service.AppUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2018/11/18
 */
@Slf4j
@Component("APP-SMS")
@AllArgsConstructor
public class SmsLoginHandler extends AbstractLoginHandler {

	private final AppUserService appUserService;

	/**
	 * 验证码登录传入为手机号 不用不处理
	 * @param mobile
	 * @return
	 */
	@Override
	public String identify(String mobile) {
		return mobile;
	}

	/**
	 * 通过mobile 获取用户信息
	 * @param identify
	 * @return
	 */
	@Override
	public AppUserInfo info(String identify) {
		AppUser user = appUserService.getOne(Wrappers.<AppUser>query().lambda().eq(AppUser::getPhone, identify));

		if (user == null) {
			log.info("手机号未注册:{}", identify);
			return createAndSaveAppUserInfo(identify);
		}
		return appUserService.findUserInfo(user);
	}

	/**
	 * 创建并插入用户
	 * @param identify
	 * @return
	 */
	private AppUserInfo createAndSaveAppUserInfo(String phone) {
		AppUser appUser = new AppUser();
		appUser.setUsername(phone);
		appUser.setPhone(phone);
		appUser.setCreateBy(phone);
		appUser.setUpdateBy(phone);
		appUserService.saveOrUpdate(appUser, Wrappers.<AppUser>lambdaQuery().eq(AppUser::getPhone, phone));

		AppUserInfo appUserDTO = new AppUserInfo();
		appUserDTO.setAppUser(appUser);
		return appUserDTO;
	}

	/**
	 * 绑定逻辑
	 * @param user 用户实体
	 * @param identify 渠道返回唯一标识
	 * @return
	 */
	@Override
	public Boolean bind(AppUser user, String identify) {
		user.setPhone(identify);
		appUserService.updateById(user);
		return true;
	}

}
