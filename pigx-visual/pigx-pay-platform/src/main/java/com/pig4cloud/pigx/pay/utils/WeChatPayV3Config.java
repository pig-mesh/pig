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
import com.ijpay.core.enums.AuthTypeEnum;
import com.ijpay.core.kit.PayKit;
import com.pig4cloud.pigx.pay.entity.PayChannel;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/**
 * 微信支付 APIV3 渠道配置
 *
 * @author lengleng
 * @date 2026-05-10
 */
@Getter
public class WeChatPayV3Config {

    /**
     * 公众号 AppID。
     */
    private final String appId;

    /**
     * 微信支付商户号（channelMchId）。
     */
    private final String mchId;

    /**
     * APIv3 密钥，32 位字符串，用于回调解密。
     */
    private final String apiV3Key;

    /**
     * 商户 API 证书序列号，用于签名请求头。
     */
    private final String merchantSerialNo;

    /**
     * 商户私钥 PEM 文本，换行符已从 \n 还原为真实换行。
     */
    private final String privateKeyContent;

    /**
     * 微信平台证书 PEM 文本，换行符已从 \n 还原为真实换行。
     */
    private final String platformCertContent;

    /**
     * 解析后的商户私钥对象，用于 APIV3 签名。
     */
    private final PrivateKey privateKey;

    private WeChatPayV3Config(String appId, String mchId, String apiV3Key, String merchantSerialNo,
                              String privateKeyContent, String platformCertContent, PrivateKey privateKey) {
        this.appId = appId;
        this.mchId = mchId;
        this.apiV3Key = apiV3Key;
        this.merchantSerialNo = merchantSerialNo;
        this.privateKeyContent = privateKeyContent;
        this.platformCertContent = platformCertContent;
        this.privateKey = privateKey;
    }

    /**
     * 从渠道配置构建 APIV3 配置对象，并验证必填字段。
     *
     * @param channel 支付渠道配置，不允许为空；param JSON 需含 apiV3Key / merchantSerialNo / privateKey / platformCert
     * @return 解析完成的配置对象
     * @throws IllegalArgumentException 配置缺失或私钥解析失败
     */
    public static WeChatPayV3Config from(PayChannel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("微信公众号支付渠道配置为空");
        }

        JSONObject params = JSONUtil.parseObj(channel.getParam());
        String appId = require(channel.getAppId(), "appId");
        String mchId = require(channel.getChannelMchId(), "channelMchId");
        String apiV3Key = require(params.getStr("apiV3Key"), "param.apiV3Key");
        String merchantSerialNo = require(params.getStr("merchantSerialNo"), "param.merchantSerialNo");
        String privateKeyContent = normalizePem(require(params.getStr("privateKey"), "param.privateKey"));
        String platformCertContent = normalizePem(require(params.getStr("platformCert"), "param.platformCert"));

        try {
            PrivateKey privateKey = PayKit.getPrivateKeyByKeyContent(privateKeyContent, AuthTypeEnum.RSA.getCode());
            return new WeChatPayV3Config(appId, mchId, apiV3Key, merchantSerialNo, privateKeyContent,
                    platformCertContent, privateKey);
        } catch (Exception ex) {
            throw new IllegalArgumentException("微信支付 APIV3 私钥解析失败", ex);
        }
    }

    /**
     * 返回平台证书内容的字节流，用于传入 WxPayKit.verifyNotify 验签。
     *
     * @return 平台证书 UTF-8 字节流，每次调用返回新实例
     */
    public InputStream platformCertInputStream() {
        return new ByteArrayInputStream(platformCertContent.getBytes(StandardCharsets.UTF_8));
    }

    static String normalizePem(String value) {
        return value.replace("\\n", "\n").trim();
    }

    private static String require(String value, String field) {
        if (StrUtil.isBlank(value)) {
            throw new IllegalArgumentException("微信支付 APIV3 配置缺失: " + field);
        }
        return value;
    }

}
