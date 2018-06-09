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

package com.github.pig.common.util.template;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lengleng
 * @date 2018/1/15
 * 短信消息模板
 */
@Data
public class MobileMsgTemplate implements Serializable {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 组装后的模板内容JSON字符串
     */
    private String context;
    /**
     * 短信通道
     */
    private String channel;
    /**
     * 短信类型(验证码或者通知短信)
     * 暂时不用，留着后面存数据库备用吧
     */
    private String type;
    /**
     * 短信签名
     */
    private String signName;
    /**
     * 短信模板
     */
    private String template;

    public MobileMsgTemplate(String mobile, String context, String channel, String signName, String template){
        this.mobile = mobile;
        this.context = context;
        this.channel = channel;
        this.signName = signName;
        this.template = template;
    }
}
