package com.pig4cloud.pigx.pay.handler.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.pig4cloud.pigx.pay.entity.PayChannel;
import com.pig4cloud.pigx.pay.entity.PayRefundOrder;
import com.pig4cloud.pigx.pay.entity.PayTradeOrder;
import com.pig4cloud.pigx.pay.handler.PayOrderRefundHandler;
import com.pig4cloud.pigx.pay.mapper.PayChannelMapper;
import com.pig4cloud.pigx.pay.mapper.PayRefundOrderMapper;
import com.pig4cloud.pigx.pay.mapper.PayTradeOrderMapper;
import com.pig4cloud.pigx.pay.utils.OrderStatusEnum;
import com.pig4cloud.pigx.pay.utils.PayChannelNameEnum;
import com.pig4cloud.pigx.pay.utils.TradeStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * yungous 退款时限
 *
 * @author lengleng
 * @date 2023/3/1
 */
@Service("ALIPAY_REFUND")
@RequiredArgsConstructor
public class AlipayPayOrderRefundHandler implements PayOrderRefundHandler {

	private final PayRefundOrderMapper refundOrderMapper;

	private final PayChannelMapper channelMapper;

	private final PayTradeOrderMapper tradeOrderMapper;

	private final HttpServletRequest request;

	@Override
	public PayChannel preparePayParams() {
		PayChannel channel = channelMapper.selectOne(
				Wrappers.<PayChannel>lambdaQuery().eq(PayChannel::getChannelId, PayChannelNameEnum.ALIPAY_WAP.name()));

		if (channel == null) {
			throw new IllegalArgumentException("支付宝网页支付渠道配置为空");
		}

		JSONObject params = JSONUtil.parseObj(channel.getParam());
		AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
			.setAppId(channel.getAppId())
			.setPrivateKey(params.getStr("privateKey"))
			.setCharset(CharsetUtil.UTF_8)
			.setAliPayPublicKey(params.getStr("publicKey"))
			.setServiceUrl(params.getStr("serviceUrl"))
			.setSignType("RSA2")
			.build();
		AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
		return channel;
	}

	/**
	 * 创建商品退款订单
	 * @param refundOrder 退款订单
	 * @param tradeOrder 交易订单
	 * @return
	 */
	@Override
	public PayRefundOrder createPayRefundOrder(PayRefundOrder refundOrder, PayTradeOrder tradeOrder) {
		refundOrder.setPayOrderId(tradeOrder.getOrderId());
		refundOrder.setChannelOrderNo(tradeOrder.getChannelOrderNo());
		refundOrder.setChannelId(PayChannelNameEnum.ALIPAY_WAP.getName());
		refundOrder.setChannelMchId(AliPayApiConfigKit.getAliPayApiConfig().getAppId());
		refundOrder.setClientIp(JakartaServletUtil.getClientIP(request));
		refundOrder.setPayAmount(tradeOrder.getAmount());
		refundOrderMapper.insert(refundOrder);
		return refundOrder;
	}

	/**
	 * 调起渠道退款
	 * @param refundOrder 商品退款订单
	 * @param tradeOrder 交易订单
	 * @return obj
	 */
	@Override
	@SneakyThrows
	public Object refund(PayRefundOrder refundOrder, PayTradeOrder tradeOrder) {
		AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
		refundModel.setOutTradeNo(tradeOrder.getOrderId().toString());
		refundModel.setTradeNo(tradeOrder.getChannelOrderNo());
		refundModel.setRefundAmount(NumberUtil.div(refundOrder.getRefundAmount().toString(), "100", 2).toString());
		refundModel.setRefundReason(refundOrder.getRemark());
		return AliPayApi.tradeRefundToResponse(refundModel);
	}

	/**
	 * 更新订单信息
	 * @param refundOrder 退款订单
	 * @param tradeOrder 交易订单
	 */
	@Override
	public void updateOrder(Object obj, PayRefundOrder refundOrder, PayTradeOrder tradeOrder) {
		AlipayTradeRefundResponse refundResponse = (AlipayTradeRefundResponse) obj;
		refundOrder.setMchRefundNo(refundResponse.getTradeNo());
		// 更新退款单状态成功
		refundOrder.setStatus(Integer.parseInt(TradeStatusEnum.TRADE_SUCCESS.getStatus()));
		refundOrder.setRefundSuccTime(LocalDateTime.now());
		refundOrderMapper.updateById(refundOrder);
		// 更新原订单为退款成功状态
		tradeOrder.setPaySuccTime(LocalDateTime.now());
		tradeOrder.setStatus(OrderStatusEnum.REFUND_SUCCESS.getStatus());
		tradeOrderMapper.updateById(tradeOrder);
	}

	/**
	 * 调用入口
	 * @param refundOrder 退款订单
	 * @return
	 */
	@Override
	public Object handle(PayRefundOrder refundOrder) {
		// 准备支付宝相关参数
		preparePayParams();
		// 根据订单ID查询交易订单
		PayTradeOrder payTradeOrder = tradeOrderMapper.selectOne(
				Wrappers.<PayTradeOrder>lambdaQuery().eq(PayTradeOrder::getOrderId, refundOrder.getPayOrderId()));
		// 创建退款单
		createPayRefundOrder(refundOrder, payTradeOrder);
		// 拉起支付宝退款
		Object refund = refund(refundOrder, payTradeOrder);
		// 更新库表状态
		updateOrder(refund, refundOrder, payTradeOrder);
		return null;
	}

}
