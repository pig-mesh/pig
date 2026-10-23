package com.pig4cloud.pig.auth.config;

import com.pig4cloud.pig.common.security.component.PigResourceServerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.SecurityFilterChain;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityFilterChainOrderTests {

	@Test
	void authorizationEndpointsAreMatchedBeforeTheCatchAllResourceChain() {
		assertThat(chainOrder(AuthorizationServerConfiguration.class))
			.isLessThan(chainOrder(PigResourceServerConfiguration.class));
	}

	private int chainOrder(Class<?> configuration) {
		Method method = Arrays.stream(configuration.getDeclaredMethods())
			.filter(candidate -> SecurityFilterChain.class.isAssignableFrom(candidate.getReturnType()))
			.findFirst()
			.orElseThrow();
		return method.getAnnotation(Order.class).value();
	}

}
