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

package com.pig4cloud.pig.common.security.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lengleng
 * @date 2019/2/1
 * 认证授权相关工具类
 */
@Slf4j
public class AuthUtils {
	private static final String BASIC_ = "Basic ";

	/**
	 * 从header 请求中的clientId/clientsecect
	 *
	 * @param header header中的参数
	 * @throws RuntimeException if the Basic header is not present or is not valid
	 *                          Base64
	 */
	public static String[] extractAndDecodeHeader(String header)
		throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(
				"Failed to decode basic authentication token");
		}

		String token = new String(decoded, CharsetUtil.UTF_8);

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new RuntimeException("Invalid basic authentication token");
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
	public static String[] extractAndDecodeHeader(HttpServletRequest request)
		throws IOException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith(BASIC_)) {
			throw new RuntimeException("请求头中client信息为空");
		}

		return extractAndDecodeHeader(header);
	}
}
