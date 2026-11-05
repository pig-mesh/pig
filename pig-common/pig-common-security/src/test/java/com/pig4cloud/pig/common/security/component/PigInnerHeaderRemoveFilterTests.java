package com.pig4cloud.pig.common.security.component;

import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PigInnerHeaderRemoveFilterTests {

	@Test
	void rejectsForwardedLoopbackOnExternalInternalRequest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRemoteAddr("203.0.113.10");
		request.addHeader("X-Forwarded-For", "127.0.0.1");
		request.addHeader(SecurityConstants.FROM, SecurityConstants.FROM_IN);
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = mock(FilterChain.class);
		new PigInnerHeaderRemoveFilter().doFilter(request, response, chain);
		assertThat(response.getStatus()).isEqualTo(403);
		verifyNoInteractions(chain);
	}

	@Test
	void allowsLoopbackInternalCallsAndOrdinaryExternalCalls() throws Exception {
		for (boolean internal : new boolean[] { true, false }) {
			MockHttpServletRequest request = new MockHttpServletRequest();
			request.setRemoteAddr(internal ? "127.0.0.1" : "203.0.113.10");
			if (internal)
				request.addHeader(SecurityConstants.FROM, SecurityConstants.FROM_IN);
			MockHttpServletResponse response = new MockHttpServletResponse();
			FilterChain chain = mock(FilterChain.class);
			new PigInnerHeaderRemoveFilter().doFilter(request, response, chain);
			verify(chain).doFilter(request, response);
		}
	}

}
