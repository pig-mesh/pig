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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * xss 处理拦截器
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class XssCleanInterceptor implements AsyncHandlerInterceptor {

	/**
	 * XSS防护配置属性
	 */
	private final PigXssProperties xssProperties;

	/**
	 * 预处理XSS过滤
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param handler 处理器对象
	 * @return 总是返回true
	 * @throws Exception 处理过程中可能抛出的异常
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1. 非控制器请求直接跳出
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		// 2. 没有开启
		if (!xssProperties.isEnabled()) {
			return true;
		}
		// 3. 处理 XssIgnore 注解
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		XssCleanIgnore xssCleanIgnore = AnnotationUtils.getAnnotation(handlerMethod.getMethod(), XssCleanIgnore.class);
		if (xssCleanIgnore == null) {
			XssHolder.setEnable();
		}
		else if (ArrayUtil.isNotEmpty(xssCleanIgnore.value())) {
			XssHolder.setEnable();
			XssHolder.setXssCleanIgnore(xssCleanIgnore);
		}
		return true;
	}

	/**
	 * 请求完成后清理XSS过滤器持有的线程局部变量
	 * @param request HTTP请求对象
	 * @param response HTTP响应对象
	 * @param handler 处理请求的处理器
	 * @param ex 处理过程中抛出的异常
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		XssHolder.remove();
	}

	/**
	 * 在并发处理开始后清除XSSHolder中的内容
	 * @param request HTTP请求对象
	 * @param response HTTP响应对象
	 * @param handler 处理器对象
	 * @throws Exception 可能抛出的异常
	 */
	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		XssHolder.remove();
	}

}
