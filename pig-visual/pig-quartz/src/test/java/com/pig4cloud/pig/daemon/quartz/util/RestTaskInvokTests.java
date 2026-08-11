package com.pig4cloud.pig.daemon.quartz.util;

import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.exception.TaskException;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestTaskInvokTests {

	private final RestTaskUrlValidator validator = mock(RestTaskUrlValidator.class);

	private final RestTaskInvok invoker = new RestTaskInvok(validator);

	@Test
	void rejectsUrlBeforeSendingRequest() {
		SysJob sysJob = SysJob.builder().jobId(1L).executePath("http://127.0.0.1/internal").build();
		when(validator.isAllowed(sysJob.getExecutePath())).thenReturn(false);

		assertThatThrownBy(() -> invoker.invokMethod(sysJob)).isInstanceOf(TaskException.class)
			.hasMessage("定时任务REST地址未通过白名单校验");
		verify(validator).isAllowed(sysJob.getExecutePath());
	}

	@Test
	void doesNotFollowRedirects() throws Exception {
		AtomicInteger redirectHits = new AtomicInteger();
		AtomicInteger targetHits = new AtomicInteger();
		HttpServer targetServer = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
		targetServer.createContext("/target", exchange -> {
			targetHits.incrementAndGet();
			exchange.sendResponseHeaders(200, -1);
			exchange.close();
		});
		HttpServer redirectServer = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
		redirectServer.createContext("/redirect", exchange -> {
			redirectHits.incrementAndGet();
			exchange.getResponseHeaders()
				.add("Location", "http://127.0.0.1:" + targetServer.getAddress().getPort() + "/target");
			exchange.sendResponseHeaders(302, -1);
			exchange.close();
		});

		targetServer.start();
		redirectServer.start();
		try {
			String executePath = "http://127.0.0.1:" + redirectServer.getAddress().getPort() + "/redirect";
			SysJob sysJob = SysJob.builder().jobId(2L).executePath(executePath).build();
			when(validator.isAllowed(executePath)).thenReturn(true);

			invoker.invokMethod(sysJob);

			assertThat(redirectHits).hasValue(1);
			assertThat(targetHits).hasValue(0);
		}
		finally {
			redirectServer.stop(0);
			targetServer.stop(0);
		}
	}

}
