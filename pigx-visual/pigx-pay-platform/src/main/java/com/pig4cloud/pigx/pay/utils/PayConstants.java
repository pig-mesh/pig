package com.pig4cloud.pigx.pay.utils;

/**
 * @author lengleng
 * @date 2019-06-14
 * <p>
 * 支付相关的常量
 */
public interface PayConstants {

	/**
	 * 支付宝商户交易编号
	 */
	String OUT_TRADE_NO = "out_trade_no";

	/**
	 * 支付宝浏览器标志
	 */
	String ALIPAY = "Alipay";

	/**
	 * 微信浏览器标志
	 */
	String MICRO_MESSENGER = "MicroMessenger";

	/**
	 * 返回码
	 */
	String RESULT_CODE = "result_code";

	/**
	 * 支付状态（支付宝）
	 */
	String TRADE_STATUS = "trade_status";

	/**
	 * 聚合支付返回 Code
	 */
	String MERGE_CODE = "code";

	/**
	 * 聚合支付订单号
	 */
	String MERGE_OUT_TRADE_NO = "outTradeNo";

	/**
	 * 支付渠道
	 */
	String PAY_CHANNEL = "payChannel";

}
