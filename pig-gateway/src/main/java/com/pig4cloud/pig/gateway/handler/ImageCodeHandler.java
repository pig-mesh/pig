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

package com.pig4cloud.pig.gateway.handler;

import com.pig4cloud.captcha.ArithmeticCaptcha;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author lengleng
 * @date 2018/7/5 验证码生成逻辑处理类
 */
@Slf4j
@RequiredArgsConstructor
public class ImageCodeHandler implements HandlerFunction<ServerResponse> {

	private static final Integer DEFAULT_IMAGE_WIDTH = 100;

	private static final Integer DEFAULT_IMAGE_HEIGHT = 40;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public Mono<ServerResponse> handle(ServerRequest serverRequest) {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);

		String result = captcha.text();

		// 保存验证码信息
		Optional<String> randomStr = serverRequest.queryParam("randomStr");
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		randomStr.ifPresent(s -> redisTemplate.opsForValue().set(CacheConstants.DEFAULT_CODE_KEY + s, result,
				SecurityConstants.CODE_TIME, TimeUnit.SECONDS));

		// 转换流信息写出
		FastByteArrayOutputStream os = new FastByteArrayOutputStream();
		captcha.out(os);

		return ServerResponse.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG)
				.body(BodyInserters.fromResource(new ByteArrayResource(os.toByteArray())));
	}

}
