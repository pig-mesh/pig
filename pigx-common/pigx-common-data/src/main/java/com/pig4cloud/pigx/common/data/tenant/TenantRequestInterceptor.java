package com.pig4cloud.pigx.common.data.tenant;

import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * RestTemplate 请求拦截器，用于传递租户ID
 *
 * @author lengleng
 * @date 2025/06/27
 */
public class TenantRequestInterceptor implements ClientHttpRequestInterceptor {

	/**
	 * 拦截HTTP请求，添加租户ID到请求头
	 *
	 * @param request   HTTP请求对象
	 * @param body      请求体字节数组
	 * @param execution 请求执行器
	 * @return 客户端HTTP响应
	 * @throws IOException 当I/O操作失败时抛出
	 */
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		if (TenantContextHolder.getTenantId() != null) {
			request.getHeaders().set(CommonConstants.TENANT_ID, String.valueOf(TenantContextHolder.getTenantId()));
		}

		return execution.execute(request, body);
	}

}
