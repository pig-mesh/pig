/*
 *
 *  *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pig4cloud.pig.common.mybatis.resolver;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Mybatis Plus Order By SQL注入问题解决类
 *
 * @author lengleng
 * @date 2019-06-24
 */
public class SqlFilterArgumentResolver implements HandlerMethodArgumentResolver {

	/**
	 * 判断Controller方法参数是否为Page类型
	 * @param parameter 方法参数
	 * @return 如果参数类型是Page则返回true，否则返回false
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Page.class);
	}

	/**
	 * 解析分页参数并构建Page对象
	 * @param parameter 方法参数信息
	 * @param mavContainer 模型和视图容器
	 * @param webRequest web请求对象
	 * @param binderFactory 数据绑定工厂
	 * @return 包含分页和排序信息的Page对象
	 * @throws NumberFormatException 当分页参数转换失败时抛出
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		String[] ascs = request.getParameterValues("ascs");
		String[] descs = request.getParameterValues("descs");
		String current = request.getParameter("current");
		String size = request.getParameter("size");

		Page<?> page = new Page<>();
		if (StrUtil.isNotBlank(current)) {
			page.setCurrent(Convert.toLong(current, 0L));
		}

		if (StrUtil.isNotBlank(size)) {
			page.setSize(Convert.toLong(size, 10L));
		}

		List<OrderItem> orderItemList = new ArrayList<>();
		Optional.ofNullable(ascs)
			.ifPresent(s -> orderItemList
				.addAll(Arrays.stream(s).filter(asc -> !SqlInjectionUtils.check(asc)).map(OrderItem::asc).toList()));
		Optional.ofNullable(descs)
			.ifPresent(s -> orderItemList
				.addAll(Arrays.stream(s).filter(desc -> !SqlInjectionUtils.check(desc)).map(OrderItem::desc).toList()));
		page.addOrder(orderItemList);

		return page;
	}

}
