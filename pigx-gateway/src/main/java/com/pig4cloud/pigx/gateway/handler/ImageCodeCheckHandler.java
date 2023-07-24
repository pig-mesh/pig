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

package com.pig4cloud.pigx.gateway.handler;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author lengleng
 * @date 2020/5/19 验证码生成逻辑处理类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ImageCodeCheckHandler implements HandlerFunction<ServerResponse> {

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public Mono<ServerResponse> handle(ServerRequest request) {
		CaptchaVO vo = new CaptchaVO();
		vo.setPointJson(request.queryParam("pointJson").get());
		vo.setToken(request.queryParam("token").get());
		vo.setCaptchaType(CommonConstants.IMAGE_CODE_TYPE);

		CaptchaService captchaService = SpringContextHolder.getBean(CaptchaService.class);
		ResponseModel responseModel = captchaService.check(vo);

		return ServerResponse.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(objectMapper.writeValueAsString(R.ok(responseModel))));
	}

}
