/*
 *    Copyright (c) 2018-2026, 云集汇通 All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the yunjihuitong.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 */

package com.pig4cloud.pigx.mp.config;

import com.pig4cloud.pigx.common.data.cache.RedisUtils;
import me.chanjar.weixin.common.enums.TicketType;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

/**
 * @author lengleng
 * @date 2019/03/26
 * <p>
 * 微信SDK redis扩展
 */
public class WxMpInRedisConfigStorage extends WxMpDefaultConfigImpl {

	private final static String ACCESS_TOKEN_KEY = "wechat:access_token_";

	private final static String JSAPI_TICKET_KEY = "wechat:jsapi_ticket_";

	private final static String CARDAPI_TICKET_KEY = "wechat:cardapi_ticket_";

	private String accessTokenKey;

	private String jsapiTicketKey;

	private String cardapiTicketKey;

    public WxMpInRedisConfigStorage() {
	}

	/**
	 * 每个公众号生成独有的存储key
	 * @param appId
	 */
	@Override
	public void setAppId(String appId) {
		super.setAppId(appId);
		this.accessTokenKey = ACCESS_TOKEN_KEY.concat(appId);
		this.jsapiTicketKey = JSAPI_TICKET_KEY.concat(appId);
		this.cardapiTicketKey = CARDAPI_TICKET_KEY.concat(appId);
	}

	@Override
	public String getAccessToken() {
        return RedisUtils.get(this.accessTokenKey);
	}

	@Override
	public boolean isAccessTokenExpired() {
        return RedisUtils.getExpire(accessTokenKey) < 2L;
	}

	@Override
	public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        RedisUtils.set(accessTokenKey, accessToken, expiresInSeconds - 200);
	}

	@Override
	public void expireAccessToken() {
        RedisUtils.expire(this.accessTokenKey, 0);
	}

	@Override
	public String getJsapiTicket() {
        return RedisUtils.get(this.jsapiTicketKey);
	}

	@Override
	public String getCardApiTicket() {
        return RedisUtils.get(cardapiTicketKey);
	}

	@Override
	public String getTicket(TicketType type) {
        return RedisUtils.get(getTicketRedisKey(type));
	}

	@Override
	public boolean isTicketExpired(TicketType type) {
        return RedisUtils.getExpire(this.getTicketRedisKey(type)) < 2;
	}

	@Override
	public synchronized void updateTicket(TicketType type, String jsapiTicket, int expiresInSeconds) {
        RedisUtils.set(getTicketRedisKey(type), jsapiTicket, expiresInSeconds - 200);
	}

	@Override
	public void expireTicket(TicketType type) {
        RedisUtils.expire(getTicketRedisKey(type), 0);
	}

	private String getTicketRedisKey(TicketType type) {
		return String.format("wechat:ticket:key:%s:%s", this.appId, type.getCode());
	}

}
