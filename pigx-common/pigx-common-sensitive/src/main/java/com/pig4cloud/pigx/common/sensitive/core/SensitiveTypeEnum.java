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

package com.pig4cloud.pigx.common.sensitive.core;

import cn.hutool.core.util.DesensitizedUtil;
import com.pig4cloud.pigx.common.sensitive.annotation.Sensitive;
import com.pig4cloud.pigx.common.sensitive.util.DesensitizedUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 敏感信息枚举类
 *
 * @author mayee
 * @version v1.0
 **/
@RequiredArgsConstructor
public enum SensitiveTypeEnum {

    /**
     * 自定义
     */
    CUSTOMER((sensitive, input) -> DesensitizedUtils.desValue(input, sensitive.prefixNoMaskLen(), sensitive.suffixNoMaskLen(), sensitive.maskStr())),
    /**
     * 用户名, 刘*华, 徐*
     */
    CHINESE_NAME((sensitive, input) -> DesensitizedUtils.chineseName(input)),
    /**
     * 身份证号, 110110********1234
     */
    ID_CARD((sensitive, input) -> DesensitizedUtils.idCardNum(input)),
    /**
     * 座机号, ****1234
     */
    FIXED_PHONE((sensitive, input) -> DesensitizedUtils.fixedPhone(input)),
    /**
     * 手机号, 176****1234
     */
    MOBILE_PHONE((sensitive, input) -> DesensitizedUtils.mobilePhone(input)),
    /**
     * 地址, 北京********
     */
    ADDRESS((sensitive, input) -> DesensitizedUtils.address(input)),
    /**
     * 电子邮件, s*****o@xx.com
     */
    EMAIL((sensitive, input) -> DesensitizedUtils.email(input)),
    /**
     * 银行卡, 622202************1234
     */
    BANK_CARD((sensitive, input) -> DesensitizedUtils.bankCard(input)),
    /**
     * 密码, 永远是 ******, 与长度无关
     */
    PASSWORD((sensitive, input) -> DesensitizedUtils.password(input)),
    /**
     * 密钥, 【密钥】密钥除第一位其他都是***, 与长度无关
     */
    KEY((sensitive, input) -> DesensitizedUtils.key(input)),
    /**
     * IPV4 类型 113.123.198.176 主机部分打码 113.123.*.123
     */
    IPV4((sensitive, input) -> DesensitizedUtils.ipv4(input)),

    /**
     * 中国大陆车牌，包含普通车辆、新能源车辆
     */
    CAR_LICENSE((sensitive, input) -> DesensitizedUtil.carLicense(input));


    /**
     * 获取脱敏策略
     */
    @Getter
    private final SensitiveStrategyFunction<Sensitive, String, String> strategy;

}
