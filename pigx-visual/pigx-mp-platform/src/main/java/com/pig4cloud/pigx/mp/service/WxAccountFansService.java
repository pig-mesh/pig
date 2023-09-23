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
package com.pig4cloud.pigx.mp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.mp.entity.WxAccountFans;

/**
 * 微信公众号粉丝
 *
 * @author lengleng
 * @date 2019-03-26 22:08:08
 */
public interface WxAccountFansService extends IService<WxAccountFans> {

	/**
	 * 同步指定公众号粉丝
	 * @param appId
	 * @return
	 */
	void syncAccountFans(String appId);

	/**
	 * 分页查询粉丝
	 * @param page 粉丝
	 * @param wxAccountFans 查询条件
	 * @return
	 */
	IPage getFansWithTagPage(Page page, WxAccountFans wxAccountFans);

	/**
	 * 更新粉丝信息
	 * @param wxAccountFans 信息
	 * @return
	 */
	Boolean updateFans(WxAccountFans wxAccountFans);

	Boolean unblack(Long[] ids, String appId);

	Boolean black(Long[] ids, String appId);

}
