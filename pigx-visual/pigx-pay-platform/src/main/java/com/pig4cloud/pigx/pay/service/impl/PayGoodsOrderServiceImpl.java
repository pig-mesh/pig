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
package com.pig4cloud.pigx.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.pay.entity.PayGoodsOrder;
import com.pig4cloud.pigx.pay.handler.PayOrderHandler;
import com.pig4cloud.pigx.pay.mapper.PayGoodsOrderMapper;
import com.pig4cloud.pigx.pay.service.PayGoodsOrderService;
import com.pig4cloud.pigx.pay.utils.PayChannelNameEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品
 *
 * @author lengleng
 * @date 2019-05-28 23:58:27
 */
@Slf4j
@Service
@AllArgsConstructor
public class PayGoodsOrderServiceImpl extends ServiceImpl<PayGoodsOrderMapper, PayGoodsOrder>
		implements PayGoodsOrderService {

	private final Map<String, PayOrderHandler> orderHandlerMap;

	private final HttpServletRequest request;

	/**
	 * 下单购买
	 * @param goodsOrder
	 * @param isMerge
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> buy(PayGoodsOrder goodsOrder, boolean isMerge) {
		// 是否聚合支付
		String ua = isMerge ? "MERGE_PAY" : request.getHeader(HttpHeaders.USER_AGENT);

		Enum channel = PayChannelNameEnum.getChannel(ua);
		PayOrderHandler orderHandler = orderHandlerMap.get(channel.name());
		goodsOrder.setGoodsName("测试产品");
		goodsOrder.setGoodsId("10001");
		Object params = orderHandler.handle(goodsOrder);

		Map<String, Object> result = new HashMap<>(4);
		result.put("channel", channel.name());
		result.put("goods", goodsOrder);
		result.put("params", params);
		return result;
	}

}
