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

package com.pig4cloud.pigx.common.api.encrypt.config;

import com.pig4cloud.pigx.common.api.encrypt.core.ApiDecryptParamResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * api 签名自动配置
 *
 * @author L.cm
 */
@RequiredArgsConstructor
@ConditionalOnProperty(value = ApiEncryptProperties.PREFIX + ".enable", havingValue = "true", matchIfMissing = true)
public class ApiEncryptParamConfiguration implements WebMvcConfigurer {

	private final ApiEncryptProperties apiEncryptProperties;

	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new ApiDecryptParamResolver(apiEncryptProperties));
	}

}
