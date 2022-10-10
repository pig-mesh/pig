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

import com.pig4cloud.pig.common.xss.utils.XssUtil;
import org.jsoup.Jsoup;

/**
 * xss 清理器
 *
 * @author L.cm
 */
public interface XssCleaner {

	/**
	 * 清理 html
	 * @param html html
	 * @return 清理后的数据
	 */
	default String clean(String html) {
		return clean(html, XssType.FORM);
	}

	/**
	 * 清理 html
	 * @param html html
	 * @param type XssType
	 * @return 清理后的数据
	 */
	String clean(String html, XssType type);

	/**
	 * 判断输入是否安全
	 * @param html html
	 * @return 是否安全
	 */
	default boolean isValid(String html) {
		return Jsoup.isValid(html, XssUtil.WHITE_LIST);
	}

}
