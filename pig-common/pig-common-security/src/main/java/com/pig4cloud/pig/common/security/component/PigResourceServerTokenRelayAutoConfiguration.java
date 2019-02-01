/*
 *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.security.component;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;

import java.lang.annotation.*;

/**
 * @author lengleng
 * @date 2019/2/1
 * 注入AccessTokenContextRelay 解决feign 传递token 为空问题
 */
@Configuration
@AutoConfigureAfter(OAuth2AutoConfiguration.class)
@ConditionalOnWebApplication
@ConditionalOnProperty("security.oauth2.client.client-id")
public class PigResourceServerTokenRelayAutoConfiguration {

	@Bean
	public AccessTokenContextRelay accessTokenContextRelay(OAuth2ClientContext context) {
		return new AccessTokenContextRelay(context);
	}

	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Conditional(OAuth2OnClientInResourceServerCondition.class)
	@interface ConditionalOnOAuth2ClientInResourceServer {

	}

	private static class OAuth2OnClientInResourceServerCondition
		extends AllNestedConditions {

		public OAuth2OnClientInResourceServerCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnBean(ResourceServerConfiguration.class)
		static class Server {
		}

		@ConditionalOnBean(OAuth2ClientConfiguration.class)
		static class Client {
		}

	}
}
