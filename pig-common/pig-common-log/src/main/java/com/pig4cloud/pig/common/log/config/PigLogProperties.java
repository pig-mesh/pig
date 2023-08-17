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

package com.pig4cloud.pig.common.log.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 日志配置类
 *
 * @author L.cm
 */
@Getter
@Setter
@ConfigurationProperties(PigLogProperties.PREFIX)
public class PigLogProperties {

	public static final String PREFIX = "security.log";

	/**
	 * 开启日志记录
	 */
	private boolean enabled = true;

	/**
	 * 放行字段，password,mobile,idcard,phone
	 */
	@Value("${security.log.exclude-fields:password,mobile,idcard,phone}")
	private List<String> excludeFields;

	/**
	 * 请求报文最大存储长度
	 */
	private Integer maxLength = 2000;

}
