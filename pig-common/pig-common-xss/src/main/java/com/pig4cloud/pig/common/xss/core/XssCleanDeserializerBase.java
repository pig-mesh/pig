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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.IOException;

/**
 * Jackson XSS 处理基类
 *
 * @author lengleng
 * @date 2025/05/31
 */
public abstract class XssCleanDeserializerBase extends JsonDeserializer<String> {

	/**
	 * 反序列化方法，用于处理JSON字符串的反序列化并进行XSS清洗
	 * @param p JSON解析器
	 * @param ctx 反序列化上下文
	 * @return 经过XSS清洗后的字符串，如果输入为null则返回null
	 * @throws IOException 当反序列化过程中发生I/O错误时抛出
	 * @throws MismatchedInputException 当当前JSON令牌不是VALUE_STRING时抛出
	 */
	@Override
	public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
		JsonToken jsonToken = p.getCurrentToken();
		if (JsonToken.VALUE_STRING != jsonToken) {
			throw MismatchedInputException.from(p, String.class,
					"mica-xss: can't deserialize value of type java.lang.String from " + jsonToken);
		}
		// 解析字符串
		String text = p.getValueAsString();
		if (text == null) {
			return null;
		}

		// xss 配置
		return this.clean(p.getParsingContext().getCurrentName(), text);
	}

	/**
	 * 清理文本中的XSS攻击内容
	 * @param name 文本名称标识
	 * @param text 待清理的文本内容
	 * @return 清理后的安全文本
	 * @throws IOException 清理过程中发生IO异常时抛出
	 */
	public abstract String clean(String name, String text) throws IOException;

}
