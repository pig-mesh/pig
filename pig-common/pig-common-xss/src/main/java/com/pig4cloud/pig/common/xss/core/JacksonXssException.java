/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.xss.core;

import lombok.Getter;

import java.io.IOException;
import java.io.Serial;

/**
 * Jackson XSS 异常类，用于处理 JSON 序列化/反序列化过程中的 XSS 安全问题
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Getter
public class JacksonXssException extends IOException implements XssException {

	@Serial
	private static final long serialVersionUID = 1L;

	private final String input;

	/**
	 * 构造JacksonXssException异常
	 * @param input 引发异常的输入内容
	 * @param message 异常信息
	 */
	public JacksonXssException(String input, String message) {
		super(message);
		this.input = input;
	}

}
