package com.pig4cloud.pigx.pay.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lengleng
 * @date 2019-06-14
 */
@Getter
@AllArgsConstructor
public enum TradeStatusEnum {

    /**
     * 交易完成
     */
    WAIT_BUYER_PAY("WAIT_BUYER_PAY", OrderStatusEnum.INIT.getStatus()),

    /**
     * TRADE_CLOSED
     */
    TRADE_CLOSED("TRADE_CLOSED", OrderStatusEnum.REFUND_SUCCESS.getStatus()),
    /**
     * TRADE_SUCCESS
     */
    TRADE_SUCCESS("TRADE_SUCCESS", OrderStatusEnum.SUCCESS.getStatus()),

    /**
     * 交易关闭
     */
    TRADE_FINISHED("TRADE_FINISHED", OrderStatusEnum.COMPLETE.getStatus()),

    /**
     * 微信支付成功
     */
    SUCCESS("SUCCESS", OrderStatusEnum.SUCCESS.getStatus()),

    /**
     * 微信支付失败
     */
    FAIL("FAIL", OrderStatusEnum.FAIL.getStatus());

    /**
     * 描述
     */
    private String description;

    /**
     * 描述
     */
    private String status;

}
