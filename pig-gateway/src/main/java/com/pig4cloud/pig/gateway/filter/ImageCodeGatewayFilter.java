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

package com.pig4cloud.pig.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.core.exception.CheckedException;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import com.pig4cloud.pig.common.core.config.FilterIgnorePropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lengleng
 * @date 2019/2/1
 * 验证码处理
 */
@Slf4j
@Component
public class ImageCodeGatewayFilter extends AbstractGatewayFilterFactory {
	public static final String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";
	public static final String OAUTH_TOKEN_URL = "/oauth/token";
	private static final String BASIC_ = "Basic ";
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

	/**
	 * 从header 请求中的clientId/clientsecect
	 *
	 * @param header header中的参数
	 * @throws CheckedException if the Basic header is not present or is not valid
	 *                          Base64
	 */
	public static String[] extractAndDecodeHeader(String header)
		throws IOException, CheckedException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new CheckedException(
				"Failed to decode basic authentication token");
		}

		String token = new String(decoded, CharsetUtil.UTF_8);

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new CheckedException("Invalid basic authentication token");
		}
		return new String[]{token.substring(0, delim), token.substring(delim + 1)};
	}

	/**
	 * *从header 请求中的clientId/clientsecect
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String[] extractAndDecodeHeader(ServerHttpRequest request)
		throws IOException, CheckedException {
		String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith(BASIC_)) {
			throw new CheckedException("请求头中client信息为空");
		}

		return extractAndDecodeHeader(header);
	}

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			// 不是登录请求，直接向下执行
			if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), OAUTH_TOKEN_URL)) {
				return chain.filter(exchange);
			}

			// 终端设置不校验， 直接向下执行(1. 从请求参数中获取 2.从header取)
			String clientId = request.getQueryParams().getFirst("client_id");
			if (StrUtil.isNotBlank(clientId)) {
				if (filterIgnorePropertiesConfig.getClients().contains(clientId)) {
					return chain.filter(exchange);
				}
			}
			try {
				String[] clientInfos = extractAndDecodeHeader(request);
				if (filterIgnorePropertiesConfig.getClients().contains(clientInfos[0])) {
					return chain.filter(exchange);
				}
			} catch (Exception e) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
				return response.setComplete();
			}

			//校验验证码合法性
			try {
				checkCode(request);
			} catch (ValidateCodeException e) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
				return response.setComplete();
			}

			return chain.filter(exchange);
		};
	}

	/**
	 * 检查code
	 *
	 * @param request
	 * @throws ValidateCodeException 校验异常
	 */
	private void checkCode(ServerHttpRequest request) throws ValidateCodeException {
		String code = request.getQueryParams().getFirst("code");

		if (StrUtil.isBlank(code)) {
			throw new ValidateCodeException();
		}

		String randomStr = request.getQueryParams().getFirst("randomStr");
		if (StrUtil.isBlank(randomStr)) {
			throw new ValidateCodeException();
		}

		String key = DEFAULT_CODE_KEY + randomStr;
		if (!redisTemplate.hasKey(key)) {
			throw new ValidateCodeException();
		}

		Object codeObj = redisTemplate.opsForValue().get(key);

		if (codeObj == null) {
			throw new ValidateCodeException();
		}

		String saveCode = codeObj.toString();
		if (StrUtil.isBlank(saveCode)) {
			redisTemplate.delete(key);
			throw new ValidateCodeException();
		}

		if (!StrUtil.equals(saveCode, code)) {
			redisTemplate.delete(key);
			throw new ValidateCodeException();
		}

		redisTemplate.delete(key);
	}
}
