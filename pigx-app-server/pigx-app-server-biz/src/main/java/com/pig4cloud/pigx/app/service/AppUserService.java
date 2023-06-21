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

package com.pig4cloud.pigx.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.app.api.dto.AppUserDTO;
import com.pig4cloud.pigx.app.api.dto.AppUserInfo;
import com.pig4cloud.pigx.app.api.entity.AppUser;
import com.pig4cloud.pigx.app.api.vo.AppUserExcelVO;
import com.pig4cloud.pigx.app.api.vo.AppUserVO;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * app用户表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
public interface AppUserService extends IService<AppUser> {

	Boolean updateUser(AppUserDTO appUser);

	Boolean saveUser(AppUserDTO appUser);

	List<AppUserExcelVO> listUser(AppUserDTO appUser);

	IPage getUsersWithRolePage(Page page, AppUserDTO appUserDTO);

	AppUserInfo findUserInfo(AppUser user);

	R updateUserInfo(AppUserDTO userDto);

	AppUserVO selectUserVoById(Long userId);

	Boolean deleteAppUserByIds(Long[] ids);

	R importUser(List<AppUserExcelVO> excelVOList, BindingResult bindingResult);

	R registerAppUser(AppUserDTO appUser);

}
