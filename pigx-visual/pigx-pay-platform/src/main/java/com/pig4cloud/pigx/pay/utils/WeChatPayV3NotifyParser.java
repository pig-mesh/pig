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

package com.pig4cloud.pigx.pay.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 微信支付 APIV3 回调报文适配器
 *
 * @author lengleng
 * @date 2026-05-10
 */
public final class WeChatPayV3NotifyParser {

    private WeChatPayV3NotifyParser() {
    }

    /**
     * 将微信 APIV3 回调解密后的 JSON 转换为旧版回调处理器兼容的 Map。
     * <p>trade_state 为 SUCCESS 时 result_code=SUCCESS，否则为 FAIL。
     *
     * @param decryptData WxPayKit.verifyNotify 解密后的 JSON 字符串，不允许为空
     * @return 包含 out_trade_no / result_code / openid / total_fee 等字段的参数 Map
     */
    public static Map<String, String> parse(String decryptData) {
        JSONObject data = JSONUtil.parseObj(decryptData);
        Map<String, String> params = new LinkedHashMap<>();
        String tradeState = data.getStr("trade_state");

        params.put(PayConstants.OUT_TRADE_NO, data.getStr("out_trade_no"));
        params.put("transaction_id", data.getStr("transaction_id"));
        params.put("attach", data.getStr("attach"));
        params.put("trade_state", tradeState);
        params.put("trade_state_desc", data.getStr("trade_state_desc"));
        params.put("success_time", data.getStr("success_time"));
        params.put(PayConstants.RESULT_CODE, StrUtil.equals("SUCCESS", tradeState) ? "SUCCESS" : "FAIL");
        params.put("err_code", tradeState);
        params.put("err_code_des", data.getStr("trade_state_desc"));

        JSONObject payer = data.getJSONObject("payer");
        if (payer != null) {
            params.put("openid", payer.getStr("openid"));
        }

        JSONObject amount = data.getJSONObject("amount");
        if (amount != null) {
            params.put("total_fee", amount.getStr("total"));
            params.put("payer_total", amount.getStr("payer_total"));
            params.put("currency", amount.getStr("currency"));
            params.put("payer_currency", amount.getStr("payer_currency"));
        }

        return params;
    }

}
