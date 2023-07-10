package com.pig4cloud.pigx.pay.handler.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import com.yungouos.pay.merge.MergePay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author lengleng
 * @date 2021/1/4
 * <p>
 * https://open.pay.yungouos.com/#/api/api/pay/merge/nativePay 服务商聚合支付模式
 */
@Slf4j
@Service("MERGE_PAY")
@RequiredArgsConstructor
public class YungouosMergePayOrderHandler extends AbstractPayOrderHandler {

	private final PayTradeOrderMapper tradeOrderMapper;

	private final PayGoodsOrderMapper goodsOrderMapper;

	private final PayChannelMapper channelMapper;

	private final HttpServletRequest request;

	/**
	 * 准备支付参数
	 * @return
	 */
	@Override
	public PayChannel preparePayParams() {
		PayChannel channel = channelMapper.selectOne(
				Wrappers.<PayChannel>lambdaQuery().eq(PayChannel::getChannelId, PayChannelNameEnum.MERGE_PAY.name()));

		if (channel == null) {
			throw new IllegalArgumentException("聚合支付渠道配置为空");
		}

		return channel;
	}

	/**
	 * 创建交易订单
	 * @param goodsOrder 商品订单
	 * @return
	 */
	@Override
	public PayTradeOrder createTradeOrder(PayGoodsOrder goodsOrder) {
		PayTradeOrder tradeOrder = new PayTradeOrder();
		tradeOrder.setOrderId(goodsOrder.getPayOrderId());
		tradeOrder.setAmount(goodsOrder.getAmount());
		tradeOrder.setChannelId(PayChannelNameEnum.MERGE_PAY.getName());
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
	 * @return obj
	 */
	@Override
	public Object pay(PayGoodsOrder goodsOrder, PayTradeOrder tradeOrder) {
		PayChannel channel = ChannelPayApiConfigKit.get();

		String money = NumberUtil.div(tradeOrder.getAmount(), "100", 2).toString();

		return MergePay.nativePay(String.valueOf(tradeOrder.getOrderId()), money, channel.getChannelMchId(),
				tradeOrder.getBody(), "1", TenantContextHolder.getTenantId().toString(),
				ChannelPayApiConfigKit.get().getNotifyUrl() + "/admin/notify/merge/callbak",
				ChannelPayApiConfigKit.get().getReturnUrl(), "", "", "", channel.getParam());
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
