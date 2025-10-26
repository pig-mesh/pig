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

package com.pig4cloud.pig.common.xss;

import com.pig4cloud.pig.common.xss.config.PigXssProperties;
import com.pig4cloud.pig.common.xss.core.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Jackson XSS 自动配置类
 *
 * @author lengleng
 * @date 2025/05/31
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(PigXssProperties.class)
@ConditionalOnProperty(prefix = PigXssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PigXssAutoConfiguration implements WebMvcConfigurer {

	private final PigXssProperties xssProperties;

	/**
	 * 创建XSS清理器Bean
	 * @param properties XSS配置属性
	 * @return XSS清理器实例
	 * @see DefaultXssCleaner
	 */
	@Bean
	@ConditionalOnMissingBean
	public XssCleaner xssCleaner(PigXssProperties properties) {
		return new DefaultXssCleaner(properties);
	}

	/**
	 * 创建FormXssClean实例
	 * @param properties PigXss配置属性
	 * @param xssCleaner XSS清理器
	 * @return FormXssClean实例
	 */
	@Bean
	public FormXssClean formXssClean(PigXssProperties properties, XssCleaner xssCleaner) {
		return new FormXssClean(properties, xssCleaner);
	}

	/**
	 * 创建Jackson2ObjectMapperBuilderCustomizer Bean，用于XSS防护
	 * @param properties XSS配置属性
	 * @param xssCleaner XSS清理器
	 * @return 自定义的Jackson2ObjectMapperBuilder
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer(PigXssProperties properties,
			XssCleaner xssCleaner) {
		return builder -> builder.deserializerByType(String.class, new JacksonXssClean(properties, xssCleaner));
	}

	/**
	 * 添加XSS拦截器
	 * @param registry 拦截器注册器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> patterns = xssProperties.getPathPatterns();
		if (patterns.isEmpty()) {
			patterns.add("/**");
		}
		XssCleanInterceptor interceptor = new XssCleanInterceptor(xssProperties);
		registry.addInterceptor(interceptor)
			.addPathPatterns(patterns)
			.excludePathPatterns(xssProperties.getPathExcludePatterns())
			.order(Ordered.LOWEST_PRECEDENCE);
	}

}
