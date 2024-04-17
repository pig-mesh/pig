/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.core.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Request包装类：允许 body 重复读取
 *
 * @author Hccake
 */
@Slf4j
public class RepeatBodyRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] bodyByteArray;

	private final Map<String, String[]> parameterMap;

	public RepeatBodyRequestWrapper(HttpServletRequest request) {
		super(request);
		this.bodyByteArray = getByteBody(request);
		this.parameterMap = super.getParameterMap();
	}

	@Override
	public BufferedReader getReader() {
		return ObjectUtils.isEmpty(this.bodyByteArray) ? null
				: new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.bodyByteArray);
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				// doNoting
			}

			@Override
			public int read() {
				return byteArrayInputStream.read();
			}
		};
	}

	private static byte[] getByteBody(HttpServletRequest request) {
		byte[] body = new byte[0];
		try {
			body = StreamUtils.copyToByteArray(request.getInputStream());
		}
		catch (IOException e) {
			log.error("解析流中数据异常", e);
		}
		return body;
	}

	/**
	 * 重写 getParameterMap() 方法 解决 undertow 中流被读取后，会进行标记，从而导致无法正确获取 body 中的表单数据的问题
	 * @return Map<String, String [ ]> parameterMap
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		return this.parameterMap;
	}

}
