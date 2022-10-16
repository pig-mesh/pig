package com.pig4cloud.pigx.common.api.encrypt.core;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.api.encrypt.annotation.decrypt.ApiDecrypt;
import com.pig4cloud.pigx.common.api.encrypt.bean.CryptoInfoBean;
import com.pig4cloud.pigx.common.api.encrypt.bean.DecryptHttpInputMessage;
import com.pig4cloud.pigx.common.api.encrypt.config.ApiEncryptProperties;
import com.pig4cloud.pigx.common.api.encrypt.exception.DecryptBodyFailException;
import com.pig4cloud.pigx.common.api.encrypt.util.ApiCryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 请求数据的加密信息解密处理<br>
 * 本类只对控制器参数中含有<strong>{@link org.springframework.web.bind.annotation.RequestBody}</strong>
 * 以及package为<strong><code>cn.licoy.encryptbody.annotation.decrypt</code></strong>下的注解有效
 *
 * @author licoy.cn
 * @author L.cm
 * @version 2018/9/7
 * @see RequestBodyAdvice
 */
@Slf4j
@Order(1)
@ControllerAdvice
@RequiredArgsConstructor
@ConditionalOnProperty(value = ApiEncryptProperties.PREFIX + ".enable", havingValue = "true", matchIfMissing = true)
public class ApiDecryptRequestBodyAdvice implements RequestBodyAdvice {

	private final ApiEncryptProperties properties;

	private final ObjectMapper objectMapper;

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return AnnotationUtil.hasAnnotation(methodParameter.getMethod(), ApiDecrypt.class);
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		// 判断 body 是否为空
		InputStream messageBody = inputMessage.getBody();
		if (messageBody.available() <= 0) {
			return inputMessage;
		}

		CryptoInfoBean cryptoInfoBean = ApiCryptoUtil.getDecryptInfo(parameter);
		if (cryptoInfoBean == null) {
			throw new DecryptBodyFailException("获取解密注解配置为空");
		}
		// base64 byte array
		byte[] bodyByteArray = StreamUtils.copyToByteArray(messageBody);
		// body 内容 json key, 默认：data {"data":"base64加密字符串"}
		String bodyJsonKey = properties.getBodyJsonKey();

		byte[] decryptedBody = null;
		if (StrUtil.isBlank(bodyJsonKey)) {
			decryptedBody = ApiCryptoUtil.decryptData(bodyByteArray, cryptoInfoBean);
		}
		else {

			Map<String, Object> data = objectMapper.readValue(bodyByteArray, Map.class);
			String content = (String) data.get(bodyJsonKey);
			if (content != null) {
				decryptedBody = ApiCryptoUtil.decryptData(content.getBytes(StandardCharsets.UTF_8), cryptoInfoBean);
			}
		}
		if (decryptedBody == null) {
			throw new DecryptBodyFailException(
					"Decryption error, " + "please check if the selected source data is encrypted correctly."
							+ " (解密错误，请检查选择的源数据的加密方式是否正确。)");
		}
		InputStream inputStream = new ByteArrayInputStream(decryptedBody);
		return new DecryptHttpInputMessage(inputStream, inputMessage.getHeaders());
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

}
