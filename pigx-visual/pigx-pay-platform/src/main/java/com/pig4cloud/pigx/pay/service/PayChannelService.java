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

package com.pig4cloud.pigx.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.pay.entity.PayChannel;

/**
 * 渠道
 *
 * @author lengleng
 * @date 2019-05-28 23:57:58
 */
public interface PayChannelService extends IService<PayChannel> {

	/**
	 * 新增支付渠道
	 * @param payChannel 支付渠道
	 * @return
	 */
	Boolean saveChannel(PayChannel payChannel);

	/**
	 * 分页查询
	 *
	 * @param page       页
	 * @param payChannel 付费频道
	 * @return {@link Page }<{@link PayChannel }>
	 */
	Page<PayChannel> queryPage(Page page, PayChannel payChannel);

	/**
	 * 获取channel
	 *
	 * @param id id
	 * @return {@link PayChannel }
	 */
	PayChannel getChannel(Long id);
}
