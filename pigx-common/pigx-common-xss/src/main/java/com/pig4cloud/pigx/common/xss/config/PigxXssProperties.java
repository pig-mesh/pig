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

package com.pig4cloud.pigx.common.xss.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Xss配置类
 *
 * @author L.cm
 */
@Getter
@Setter
@ConfigurationProperties(PigxXssProperties.PREFIX)
public class PigxXssProperties {

	public static final String PREFIX = "security.xss";

	/**
	 * 开启xss,默认关闭： 建议生产环境开启
	 */
	private boolean enabled = false;

	/**
	 * 全局：对文件进行首尾 trim
	 */
	private boolean trimText = true;

	/**
	 * 模式：clear 清理（默认），escape 转义
	 */
	private Mode mode = Mode.clear;

	/**
	 * [clear 专用] prettyPrint，默认关闭： 保留换行
	 */
	private boolean prettyPrint = false;

	/**
	 * [clear 专用] 使用转义，默认关闭
	 */
	private boolean enableEscape = false;

	/**
	 * 拦截的路由，默认为空
	 */
	private List<String> pathPatterns = new ArrayList<>();

	/**
	 * 放行的路由，默认为空
	 */
	private List<String> pathExcludePatterns = new ArrayList<>();

	public enum Mode {

		/**
		 * 清理
		 */
		clear,
		/**
		 * 转义
		 */
		escape;

	}

}
