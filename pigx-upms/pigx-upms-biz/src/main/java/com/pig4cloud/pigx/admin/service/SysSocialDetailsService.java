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

package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pigx.admin.api.vo.SysSocialDetailsVO;

import java.util.List;

/**
 * 系统社交登录账号表
 *
 * @author lengleng
 * @date 2018-08-16 21:30:41
 */
public interface SysSocialDetailsService extends IService<SysSocialDetails> {

	/**
	 * 绑定社交账号
	 * @param state 类型
	 * @param code code
	 * @return
	 */
	Boolean bindSocial(String state, String code);

	/**
	 * 根据入参查询用户信息
	 * @param inStr
	 * @return
	 */
	UserInfo getUserInfo(String inStr);

    List<SysSocialDetailsVO> selectList();
}
