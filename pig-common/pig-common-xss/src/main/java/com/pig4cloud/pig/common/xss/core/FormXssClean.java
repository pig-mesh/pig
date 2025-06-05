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

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.xss.config.PigXssProperties;
import com.pig4cloud.pig.common.xss.utils.XssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * 表单XSS清理控制器
 * <p>
 * 用于处理前端表单提交的XSS清理工作
 *
 * @author lengleng
 * @date 2025/05/31
 */
@ControllerAdvice
@ConditionalOnProperty(prefix = PigXssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class FormXssClean {

	private final PigXssProperties properties;

	private final XssCleaner xssCleaner;

	/**
	 * 初始化数据绑定器，注册自定义属性编辑器
	 * @param binder 数据绑定器
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// 处理前端传来的表单字符串
		binder.registerCustomEditor(String.class, new StringPropertiesEditor(xssCleaner, properties));
	}

	/**
	 * 字符串属性编辑器，用于处理XSS防护的字符串属性编辑
	 *
	 * @author lengleng
	 * @date 2025/05/31
	 */
	@Slf4j
	@RequiredArgsConstructor
	public static class StringPropertiesEditor extends PropertyEditorSupport {

		private final XssCleaner xssCleaner;

		private final PigXssProperties properties;

		/**
		 * 获取属性值的文本表示
		 * @return 属性值的字符串表示，如果值为null则返回空字符串
		 */
		@Override
		public String getAsText() {
			Object value = getValue();
			return value != null ? value.toString() : StrUtil.EMPTY;
		}

		/**
		 * 设置文本值，根据XSS防护状态进行相应处理
		 * @param text 要设置的文本
		 * @throws IllegalArgumentException 如果参数不合法时抛出异常
		 */
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (text == null) {
				setValue(null);
			}
			else if (XssHolder.isEnabled()) {
				String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
				setValue(value);
				log.debug("Request parameter value:{} cleaned up by mica-xss, current value is:{}.", text, value);
			}
			else {
				setValue(XssUtil.trim(text, properties.isTrimText()));
			}
		}

	}

}
