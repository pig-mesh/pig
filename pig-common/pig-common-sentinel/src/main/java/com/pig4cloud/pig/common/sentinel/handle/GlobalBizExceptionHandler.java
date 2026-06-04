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

package com.pig4cloud.pig.common.sentinel.handle;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.common.core.util.R;
import feign.FeignException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * <p>
 * 适配 Spring Boot 4.0+/Spring Framework 7.0+，统一拦截 Web、参数校验、Feign、IO 等异常。
 *
 * @author lengleng
 * @date 2026-05-18
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalBizExceptionHandler {

	/**
	 * JSON 序列化工具，用于解析 Feign 透传的远端 R 响应体。
	 */
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 全局兜底异常
	 * @param e 未被更具体处理器捕获的异常
	 * @return 统一失败响应，消息来自异常本地化描述
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleGlobalException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);
		// 业务异常交由 sentinel 记录
		Tracer.trace(e);
		return R.failed(e.getLocalizedMessage());
	}

	/**
	 * 客户端断开导致的异步响应不可用异常，无需返回任何内容
	 * <p>
	 * Spring Boot 3.2+ 在 SSE / 长连接场景客户端主动断开时抛出，需静默处理避免污染日志
	 * @param e 异步请求不可用异常
	 */
	@ExceptionHandler(AsyncRequestNotUsableException.class)
	@ResponseStatus(HttpStatus.OK)
	public void handleAsyncRequestNotUsableException(AsyncRequestNotUsableException e) {
		log.debug("异步请求已不可用 ex={}", e.getMessage());
	}

	/**
	 * 数据库异常
	 * @param exception 数据库调用异常
	 * @return 统一失败响应，不向客户端暴露底层 SQL 细节
	 */
	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleSQLException(SQLException exception) {
		log.error("数据库调用异常 ex={}", exception.getMessage(), exception);
		return R.failed("数据库调用异常，请联系管理员处理");
	}

	/**
	 * Feign 远程调用异常，透传远端 R 响应体
	 * @param e Feign 调用异常
	 * @return 远端 R 响应体或本地统一失败响应
	 */
	@ExceptionHandler(FeignException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleFeignException(FeignException e) {
		log.error("Feign 调用异常 ex={}", e.getMessage(), e);
		Tracer.trace(e);

		if (e.responseBody().isPresent()) {
			try {
				ByteBuffer responseBody = e.responseBody().get();
				byte[] body = new byte[responseBody.remaining()];
				responseBody.get(body);
				return objectMapper.readValue(body, R.class);
			}
			catch (Exception ex) {
				log.warn("Feign 响应体反序列化失败 ex={}", ex.getMessage());
			}
		}
		return R.failed(e.getLocalizedMessage());
	}

	/**
	 * 拒绝授权异常
	 * @param e Spring Security 访问拒绝异常
	 * @return 权限不足的统一失败响应
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public R handleAccessDeniedException(AccessDeniedException e) {
		log.error("拒绝授权异常信息 ex={}", e.getMessage());
		return R.failed("权限不足，不允许访问");
	}

	/**
	 * 表单参数绑定校验异常（@ModelAttribute / 表单提交）
	 * @param exception 表单参数绑定异常
	 * @return 字段校验失败的统一失败响应
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleBindException(BindException exception) {
		log.warn("参数绑定异常 ex={}", exception.getMessage());
		return R.failed(buildFieldErrorMessage(exception.getBindingResult().getFieldErrors()));
	}

	/**
	 * @RequestBody + @Valid 校验异常
	 * @param exception 请求体参数校验异常
	 * @return 字段校验失败的统一失败响应
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		log.warn("请求体参数校验异常 ex={}", exception.getMessage());
		return R.failed(buildFieldErrorMessage(exception.getBindingResult().getFieldErrors()));
	}

	/**
	 * Spring 7.0+ 方法级参数校验异常（@Validated 标注在 Controller 上的 @RequestParam / @PathVariable
	 * 校验）
	 * @param exception 方法参数校验异常
	 * @return 方法参数校验失败的统一失败响应
	 */
	@ExceptionHandler(HandlerMethodValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleHandlerMethodValidationException(HandlerMethodValidationException exception) {
		log.warn("方法参数校验异常 ex={}", exception.getMessage());
		String parameterMessage = exception.getParameterValidationResults()
			.stream()
			.map(ParameterValidationResult::getResolvableErrors)
			.flatMap(List::stream)
			.map(error -> error.getDefaultMessage() == null ? StrUtil.EMPTY : error.getDefaultMessage())
			.collect(Collectors.joining(StrUtil.COMMA));
		String crossParameterMessage = buildResolvableErrorMessage(exception.getCrossParameterValidationResults());
		String message = joinErrorMessages(parameterMessage, crossParameterMessage);
		return R.failed(message);
	}

	/**
	 * @Validated 在 Service 层方法或单参数校验失败
	 * @param exception 约束校验异常
	 * @return 约束校验失败的统一失败响应
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleConstraintViolationException(ConstraintViolationException exception) {
		log.warn("约束校验异常 ex={}", exception.getMessage());
		String message = exception.getConstraintViolations()
			.stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.joining(StrUtil.COMMA));
		return R.failed(message);
	}

	/**
	 * 请求体解析异常（JSON 格式错误 / 类型不匹配）
	 * @param exception 请求体解析异常
	 * @return 请求体格式错误的统一失败响应
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		log.warn("请求体解析异常 ex={}", exception.getMessage());
		return R.failed("请求体格式错误，无法解析");
	}

	/**
	 * 缺少必填请求参数
	 * @param exception 缺少请求参数异常
	 * @return 标明缺失参数名的统一失败响应
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
		log.warn("缺少必填参数 ex={}", exception.getMessage());
		return R.failed(StrUtil.format("缺少必填参数: {}", exception.getParameterName()));
	}

	/**
	 * 请求参数类型转换异常
	 * @param exception 方法参数类型不匹配异常
	 * @return 标明参数名的统一失败响应
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
		log.warn("参数类型不匹配 ex={}", exception.getMessage());
		return R.failed(StrUtil.format("参数 {} 类型不正确", exception.getName()));
	}

	/**
	 * 请求方法不支持
	 * @param exception 请求方法不支持异常
	 * @return 请求方法不支持的统一失败响应
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public R handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		log.warn("请求方法不支持 ex={}", exception.getMessage());
		return R.failed(exception.getMessage());
	}

	/**
	 * Content-Type 不支持
	 * @param exception 媒体类型不支持异常
	 * @return 媒体类型不支持的统一失败响应
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public R handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
		log.warn("媒体类型不支持 ex={}", exception.getMessage());
		return R.failed(exception.getMessage());
	}

	/**
	 * 文件上传超过大小限制
	 * @param exception 文件大小超过限制异常
	 * @return 文件大小超过限制的统一失败响应
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(HttpStatus.CONTENT_TOO_LARGE)
	public R handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
		log.warn("上传文件超过大小限制 ex={}", exception.getMessage());
		return R.failed("上传文件大小超过限制");
	}

	/**
	 * 保持和低版本请求路径不存在的行为一致
	 * <p>
	 * <a href="https://github.com/spring-projects/spring-boot/issues/38733">[Spring Boot
	 * 3.2.0] 404 Not Found behavior #38733</a>
	 * @param exception 静态资源或请求路径不存在异常
	 * @return 404 的统一失败响应
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public R handleNoResourceFoundException(NoResourceFoundException exception) {
		log.debug("请求路径 404 {}", exception.getMessage());
		return R.failed(exception.getMessage());
	}

	/**
	 * 构造字段校验错误信息
	 * @param fieldErrors 字段校验错误列表，空集合返回空字符串
	 * @return 拼接后的字段校验错误信息
	 */
	private String buildFieldErrorMessage(List<FieldError> fieldErrors) {
		if (fieldErrors == null || fieldErrors.isEmpty()) {
			return StrUtil.EMPTY;
		}
		return fieldErrors.stream()
			.map(error -> StrUtil.format("{} {}", error.getField(), error.getDefaultMessage()))
			.collect(Collectors.joining(StrUtil.COMMA));
	}

	/**
	 * 构造 Spring 参数校验错误信息
	 * @param errors 参数校验错误，空集合返回空字符串
	 * @return 拼接后的错误信息
	 */
	private String buildResolvableErrorMessage(List<? extends MessageSourceResolvable> errors) {
		if (errors == null || errors.isEmpty()) {
			return StrUtil.EMPTY;
		}
		return errors.stream()
			.map(error -> error.getDefaultMessage() == null ? StrUtil.EMPTY : error.getDefaultMessage())
			.collect(Collectors.joining(StrUtil.COMMA));
	}

	/**
	 * 拼接非空错误信息片段
	 * @param first 第一段错误信息，可为空
	 * @param second 第二段错误信息，可为空
	 * @return 拼接后的错误信息，全部为空时返回空字符串
	 */
	private String joinErrorMessages(String first, String second) {
		if (StrUtil.isBlank(first)) {
			return StrUtil.blankToDefault(second, StrUtil.EMPTY);
		}
		if (StrUtil.isBlank(second)) {
			return first;
		}
		return StrUtil.join(StrUtil.COMMA, first, second);
	}

}
