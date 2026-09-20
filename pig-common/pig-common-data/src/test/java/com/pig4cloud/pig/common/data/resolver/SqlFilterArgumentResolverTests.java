package com.pig4cloud.pig.common.data.resolver;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.assertj.core.api.Assertions.assertThat;

class SqlFilterArgumentResolverTests {

	@ParameterizedTest
	@ValueSource(strings = { "ascs", "descs" })
	void preservesColumnsDirectionsAndPagination(String direction) {
		Page<?> page = resolve(direction, "user_id,u.create_time,case_number");
		assertThat(page.orders()).extracting(OrderItem::getColumn)
			.containsExactly("user_id", "u.create_time", "case_number");
		assertThat(page.orders()).allMatch(order -> order.isAsc() == "ascs".equals(direction));
		assertThat(page.getCurrent()).isEqualTo(2);
		assertThat(page.getSize()).isEqualTo(10);
	}

	@ParameterizedTest
	@ValueSource(strings = { "case(1)when(1)then(user_id)else(phone)end",
			"case user_id when 1 then user_id else phone end", "CASE\nWHEN user_id IS NULL THEN phone ELSE user_id END",
			"hex(password)", "user_id+1", "user_id/**/", "user_id desc", "`password`", "1", "u..user_id" })
	void filtersExpressionsInBothSortDirections(String expression) {
		for (String direction : new String[] { "ascs", "descs" }) {
			Page<?> page = resolve(direction, "user_id," + expression + ",u.create_time");
			assertThat(page.orders()).extracting(OrderItem::getColumn).containsExactly("user_id", "u.create_time");
		}
	}

	@ParameterizedTest
	@ValueSource(strings = { "", ",,", "case(1)when(1)then(user_id)else(phone)end" })
	void omitsOrdersWhenNoValidColumnsRemain(String columns) {
		assertThat(resolve("ascs", columns).orders()).isEmpty();
		assertThat(resolve("descs", columns).orders()).isEmpty();
	}

	@Test
	void acceptsMissingSortParameters() {
		Page<?> page = (Page<?>) new SqlFilterArgumentResolver().resolveArgument(null, null,
				new ServletWebRequest(new MockHttpServletRequest()), null);
		assertThat(page.orders()).isEmpty();
	}

	private Page<?> resolve(String direction, String columns) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter(direction, columns);
		request.setParameter("current", "2");
		request.setParameter("size", "10");
		return (Page<?>) new SqlFilterArgumentResolver().resolveArgument(null, null, new ServletWebRequest(request),
				null);
	}

}
