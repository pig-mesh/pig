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

package com.pig4cloud.pigx.pay.handler.impl;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.Payer;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import com.pig4cloud.pigx.common.core.util.WebUtils;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.pay.entity.PayChannel;
import com.pig4cloud.pigx.pay.entity.PayGoodsOrder;
import com.pig4cloud.pigx.pay.entity.PayTradeOrder;
import com.pig4cloud.pigx.pay.mapper.PayChannelMapper;
import com.pig4cloud.pigx.pay.mapper.PayGoodsOrderMapper;
import com.pig4cloud.pigx.pay.mapper.PayTradeOrderMapper;
import com.pig4cloud.pigx.pay.utils.ChannelPayApiConfigKit;
import com.pig4cloud.pigx.pay.utils.OrderStatusEnum;
import com.pig4cloud.pigx.pay.utils.PayChannelNameEnum;
import com.pig4cloud.pigx.pay.utils.WeChatPayV3Config;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lengleng
 * @date 2019-05-31
 * <p>
 * 微信公众号支付
 */
@Slf4j
@Service("WEIXIN_MP")
@RequiredArgsConstructor
public class WeChatMpPayOrderHandler extends AbstractPayOrderHandler {

	private final PayTradeOrderMapper tradeOrderMapper;

	private final PayGoodsOrderMapper goodsOrderMapper;

	private final PayChannelMapper channelMapper;

	private final HttpServletRequest request;

	/**
	 * 准备支付参数
	 * @return PayChannel
	 */
	@Override
	public PayChannel preparePayParams() {
		PayChannel channel = channelMapper.selectOne(
				Wrappers.<PayChannel>lambdaQuery().eq(PayChannel::getChannelId, PayChannelNameEnum.WEIXIN_MP.name()));

		if (channel == null) {
			throw new IllegalArgumentException("微信公众号支付渠道配置为空");
		}

        // 提前验证 APIV3 配置完整性，避免在写入订单后才抛出配置异常
        WeChatPayV3Config.from(channel);
		return channel;
	}

	/**
	 * 创建交易订单
	 * @param goodsOrder
	 * @return
	 */
	@Override
	public PayTradeOrder createTradeOrder(PayGoodsOrder goodsOrder) {
		PayTradeOrder tradeOrder = new PayTradeOrder();
		tradeOrder.setOrderId(goodsOrder.getPayOrderId());
		tradeOrder.setAmount(goodsOrder.getAmount());
		tradeOrder.setChannelId(PayChannelNameEnum.WEIXIN_MP.getName());
        tradeOrder.setChannelMchId(ChannelPayApiConfigKit.get().getChannelMchId());
		tradeOrder.setClientIp(JakartaServletUtil.getClientIP(request));
		tradeOrder.setCurrency("CNY");
		tradeOrder.setStatus(OrderStatusEnum.INIT.getStatus());
		tradeOrder.setBody(goodsOrder.getGoodsName());
		tradeOrderMapper.insert(tradeOrder);
		return tradeOrder;
	}

	/**
	 * 调起渠道支付
	 * @param goodsOrder 商品订单
	 * @param tradeOrder 交易订单
	 */
	@Override
	public Object pay(PayGoodsOrder goodsOrder, PayTradeOrder tradeOrder) {
        WeChatPayV3Config config = WeChatPayV3Config.from(ChannelPayApiConfigKit.get());
        String notifyUrl = String.format("%s/api/%s/notify/wx/callbak", ChannelPayApiConfigKit.get().getNotifyUrl(),
                WebUtils.isMicro() ? "pay" : "admin");
        UnifiedOrderModel orderModel = UnifiedOrderModel.builder()
                .appid(config.getAppId())
                .mchid(config.getMchId())
                .description(goodsOrder.getGoodsName())
			.attach(TenantContextHolder.getTenantId().toString())
			.out_trade_no(String.valueOf(tradeOrder.getOrderId()))
                .notify_url(notifyUrl)
                .amount(Amount.builder().total(Integer.parseInt(goodsOrder.getAmount())).currency("CNY").build())
                .payer(Payer.builder().openid(goodsOrder.getUserId()).build())
                .build();

        try {
            IJPayHttpResponse response = WxPayApi.v3(RequestMethodEnum.POST, WxDomainEnum.CHINA.getDomain(),
                    BasePayApiEnum.JS_API_PAY.getUrl(), config.getMchId(), config.getMerchantSerialNo(), null,
                    config.getPrivateKey(), JSONUtil.toJsonStr(orderModel));
            log.info("微信 APIV3 JSAPI 下单返回: status={}, body={}", response.getStatus(), response.getBody());
            if (response.getStatus() < 200 || response.getStatus() >= 300) {
                throw new IllegalStateException("微信支付 APIV3 下单失败: " + response.getBody());
            }

            JSONObject result = JSONUtil.parseObj(response.getBody());
            String prepayId = result.getStr("prepay_id");
            if (prepayId == null) {
                throw new IllegalStateException("微信支付 APIV3 下单未返回 prepay_id");
            }
            return WxPayKit.jsApiCreateSign(config.getAppId(), prepayId, config.getPrivateKey());
        } catch (Exception ex) {
            throw new IllegalStateException("微信支付 APIV3 下单失败", ex);
        }
	}

	/**
	 * 更新订单信息
	 * @param goodsOrder 商品订单
	 * @param tradeOrder 交易订单
	 */
	@Override
	public void updateOrder(PayGoodsOrder goodsOrder, PayTradeOrder tradeOrder) {
		tradeOrderMapper.updateById(tradeOrder);
		goodsOrderMapper.updateById(goodsOrder);
	}

}
