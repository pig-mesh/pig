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

import cn.hutool.core.util.ArrayUtil;
import com.pig4cloud.pig.common.xss.config.PigXssProperties;
import com.pig4cloud.pig.common.xss.utils.XssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

/**
 * Jackson XSS 处理类，用于清理JSON数据中的XSS风险内容
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Slf4j
@RequiredArgsConstructor
public class JacksonXssClean extends XssCleanDeserializerBase {

	private final PigXssProperties properties;

	private final XssCleaner xssCleaner;

	/**
	 * 清理文本内容，根据XSS防护设置进行处理
	 * @param name 属性名称
	 * @param text 待清理的文本
	 * @return 清理后的文本
	 * @throws IOException 如果清理过程中发生IO异常
	 */
	@Override
	public String clean(String name, String text) throws IOException {
		if (XssHolder.isEnabled() && Objects.isNull(XssHolder.getXssCleanIgnore())) {
			String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
			log.debug("Json property value:{} cleaned up by mica-xss, current value is:{}.", text, value);
			return value;
		}
		else if (XssHolder.isEnabled() && Objects.nonNull(XssHolder.getXssCleanIgnore())) {
			XssCleanIgnore xssCleanIgnore = XssHolder.getXssCleanIgnore();
			if (ArrayUtil.contains(xssCleanIgnore.value(), name)) {
				return XssUtil.trim(text, properties.isTrimText());
			}

			String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
			log.debug("Json property value:{} cleaned up by mica-xss, current value is:{}.", text, value);
			return value;
		}
		else {
			return XssUtil.trim(text, properties.isTrimText());
		}
	}

}
