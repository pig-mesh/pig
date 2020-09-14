/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.security.component;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import lombok.Cleanup;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author lengleng
 * @date 2020/8/3
 * <p>
 * 重写默认tokenStore 保存 username and token 关系
 */
public class PigRedisTokenStore extends RedisTokenStore {

	private RedisConnectionFactory connectionFactory;

	public PigRedisTokenStore(RedisConnectionFactory connectionFactory) {
		super(connectionFactory);
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 序列化保存认证信息
	 * @param token token 详细信息
	 * @param authentication 认证相关信息
	 */
	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		super.storeAccessToken(token, authentication);
		@Cleanup
		RedisConnection connection = connectionFactory.getConnection();
		// KEY
		byte[] key = StrUtil.bytes(CacheConstants.PROJECT_OAUTH_TOKEN + authentication.getName());
		// value
		byte[] tokenVal = StrUtil.bytes(token.getValue());
		RedisStringCommands stringCommand = connection.stringCommands();
		stringCommand.set(key, tokenVal, Expiration.seconds(token.getExpiresIn()),
				RedisStringCommands.SetOption.SET_IF_ABSENT);
	}

	/**
	 * 删除token
	 * @param accessToken token
	 */
	@Override
	public void removeAccessToken(OAuth2AccessToken accessToken) {
		super.removeAccessToken(accessToken);
		@Cleanup
		RedisConnection connection = connectionFactory.getConnection();
		// KEY
		OAuth2Authentication authentication = readAuthentication(accessToken);
		byte[] key = StrUtil.bytes(CacheConstants.PROJECT_OAUTH_TOKEN + authentication.getName());
		connection.del(key);
	}

}
