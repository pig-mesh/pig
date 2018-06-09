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

package com.github.pig.auth.config;

import lombok.Data;

/**
 * @author lengleng
 * @date 2017/10/28
 * social 登录基础配置
 */
@Data
public class SocialPropertiesConfig {
    /**
     * 提供商
     */
    private String providerId;
    /**
     * 应用ID
     */
    private String clientId;
    /**
     * 应用密钥
     */
    private String clientSecret;

}
