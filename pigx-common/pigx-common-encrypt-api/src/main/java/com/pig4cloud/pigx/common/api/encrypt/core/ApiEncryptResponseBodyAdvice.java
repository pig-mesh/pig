package com.pig4cloud.pigx.common.api.encrypt.core;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.api.encrypt.annotation.encrypt.ApiEncrypt;
import com.pig4cloud.pigx.common.api.encrypt.bean.CryptoInfoBean;
import com.pig4cloud.pigx.common.api.encrypt.config.ApiEncryptProperties;
import com.pig4cloud.pigx.common.api.encrypt.exception.EncryptBodyFailException;
import com.pig4cloud.pigx.common.api.encrypt.util.ApiCryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应数据的加密处理<br>
 * 本类只对控制器参数中含有<strong>{@link org.springframework.web.bind.annotation.ResponseBody}</strong>
 * 或者控制类上含有<strong>{@link org.springframework.web.bind.annotation.RestController}</strong>
 * 以及package为<strong><code>cn.licoy.encryptbody.annotation.encrypt</code></strong>下的注解有效
 *
 * @author licoy.cn
 * @author L.cm
 * @version 2018/9/4
 * @see ResponseBodyAdvice
 */
@Slf4j
@Order(1)
@ControllerAdvice
@RequiredArgsConstructor
@ConditionalOnProperty(value = ApiEncryptProperties.PREFIX + ".enable", havingValue = "true", matchIfMissing = true)
public class ApiEncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	private final ApiEncryptProperties properties;

	private final ObjectMapper objectMapper;

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return AnnotationUtil.hasAnnotation(returnType.getMethod(), ApiEncrypt.class);
	}

	@Nullable
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (body == null) {
			return null;
		}

		CryptoInfoBean cryptoInfoBean = ApiCryptoUtil.getEncryptInfo(returnType);
		if (cryptoInfoBean == null) {
			throw new EncryptBodyFailException();
		}

		byte[] bodyJsonBytes;
		try {
			bodyJsonBytes = objectMapper.writeValueAsBytes(body);
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		// body 内容 json key, 默认：data {"data":"base64加密字符串"}
		String bodyJsonKey = properties.getBodyJsonKey();
		if (StrUtil.isBlank(bodyJsonKey)) {
			return ApiCryptoUtil.encryptData(bodyJsonBytes, cryptoInfoBean);
		}
		Map<String, Object> data = new HashMap<>(2);
		data.put(bodyJsonKey, ApiCryptoUtil.encryptData(bodyJsonBytes, cryptoInfoBean));
		return data;
	}

}
