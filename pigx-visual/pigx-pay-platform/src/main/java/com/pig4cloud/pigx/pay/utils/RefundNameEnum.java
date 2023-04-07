package com.pig4cloud.pigx.pay.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lengleng
 * @date 2019-05-30
 * <p>
 * 支付渠道名称
 */
@AllArgsConstructor
public enum RefundNameEnum {

	/**
	 * 支付宝wap支付
	 */
	ALIPAY("ALIPAY_REFUND", "支付宝退款"),

	/**
	 * 微信H5支付
	 */
	WEIXIN("WEIXIN_REFUND", "微信退款");

	/**
	 * 名称
	 */
	@Getter
	private String name;

	/**
	 * 描述
	 */
	private String description;

}
