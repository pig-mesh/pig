/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pigx.common.core.constant;

/**
 * @author lengleng
 * @date 2017-12-18
 */
public interface SecurityConstants {

    /**
     * 启动时是否检查Inner注解安全性
     */
    boolean INNER_CHECK = true;

    /**
     * 刷新
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * 验证码有效期
     */
    int CODE_TIME = 60;

    /**
     * 验证码长度
     */
    String CODE_SIZE = "4";

    /**
     * 角色前缀
     */
    String ROLE = "ROLE_";

    /**
     * 前缀
     */
    String PIGX_PREFIX = "pigx_";

    /**
     * token 相关前缀
     */
    String TOKEN_PREFIX = "token:";

    /**
     * oauth 相关前缀
     */
    String OAUTH_PREFIX = "oauth:";

    /**
     * 授权码模式code key 前缀
     */
    String OAUTH_CODE_PREFIX = "oauth:code:";

    /**
     * 项目的license
     */
    String PIGX_LICENSE = "https://pig4cloud.com";

    /**
     * 内部
     */
    String FROM_IN = "Y";

    /**
     * 标志
     */
    String FROM = "from";

    /**
     * 请求header
     */
    String HEADER_FROM_IN = FROM + "=" + FROM_IN;

    /**
     * OAUTH URL
     */
    String OAUTH_TOKEN_URL = "/oauth2/token";

    /**
     * 移动端授权
     */
    String GRANT_MOBILE = "mobile";

    /**
     * TOC 客户端
     */
    String HEADER_TOC = "CLIENT-TOC";

    /**
     * TOC 客户端
     */
    String HEADER_TOC_YES = "Y";

    /**
     * QQ获取token
     */
    String QQ_AUTHORIZATION_CODE_URL = "https://graph.qq.com/oauth2.0/token?grant_type="
            + "authorization_code&code=%S&client_id=%s&redirect_uri=" + "%s&client_secret=%s";

    /**
     * 微信获取OPENID
     */
    String WX_AUTHORIZATION_CODE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token"
            + "?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 微信小程序OPENID
     */
    String MINI_APP_AUTHORIZATION_CODE_URL = "https://api.weixin.qq.com/sns/jscode2session"
            + "?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    /**
     * 码云获取token
     */
    String GITEE_AUTHORIZATION_CODE_URL = "https://gitee.com/oauth/token?grant_type="
            + "authorization_code&code=%S&client_id=%s&redirect_uri=" + "%s&client_secret=%s";

    /**
     * 开源中国获取token
     */
    String OSC_AUTHORIZATION_CODE_URL = "https://www.oschina.net/action/openapi/token";

    /**
     * QQ获取用户信息
     */
    String QQ_USER_INFO_URL = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    /**
     * 码云获取用户信息
     */
    String GITEE_USER_INFO_URL = "https://gitee.com/api/v5/user?access_token=%s";

    /**
     * 开源中国用户信息
     */
    String OSC_USER_INFO_URL = "https://www.oschina.net/action/openapi/user?access_token=%s&dataType=json";

    /**
     * 钉钉获取 token
     */
    String DING_OLD_GET_TOKEN = "https://oapi.dingtalk.com/gettoken";

    /**
     * 钉钉同步部门列表
     */
    String DING_OLD_DEPT_URL = "https://oapi.dingtalk.com/topapi/v2/department/listsub";

    /**
     * 钉钉部门用户id列表
     */
    String DING_DEPT_USERIDS_URL = "https://oapi.dingtalk.com/topapi/user/listid";

    /**
     * 钉钉用户详情
     */
    String DING_USER_INFO_URL = "https://oapi.dingtalk.com/topapi/v2/user/get";

    /**
     * {bcrypt} 加密的特征码
     */
    String BCRYPT = "{bcrypt}";

    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 客户端编号
     */
    String CLIENT_ID = "client_id";

    /**
     * 客户端唯一令牌
     */
    String CLIENT_RECREATE = "recreate_flag";

    /**
     * 用户ID字段
     */
    String DETAILS_USER_ID = "user_id";

    /**
     * 用户名
     */
    String DETAILS_USERNAME = "username";

    /**
     * 姓名
     */
    String NAME = "name";

    /**
     * 协议字段
     */
    String DETAILS_LICENSE = "license";

    /**
     * 激活字段 兼容外围系统接入
     */
    String ACTIVE = "active";

    /**
     * AES 加密
     */
    String AES = "aes";

    /**
     * 授权码模式confirm
     */
    String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";

    /**
     * {noop} 加密的特征码
     */
    String NOOP = "{noop}";

    /**
     * 短信登录 参数名称
     */
    String SMS_PARAMETER_NAME = "mobile";

    /**
     * 手机号登录
     */
    String APP = "mobile";

    /**
     * 用户信息
     */
    String DETAILS_USER = "user_info";

    /**
     * 不脱敏的权限
     */
    String NO_MASK = "no_mask";

}
