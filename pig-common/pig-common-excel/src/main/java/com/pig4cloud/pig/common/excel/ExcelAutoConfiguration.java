package com.pig4cloud.pig.common.excel;

import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.excel.provider.RemoteDictApiService;
import com.pig4cloud.pig.common.excel.provider.RemoteDictDataProvider;
import com.pig4cloud.plugin.excel.handler.DictDataProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

/**
 * excel 自动装配类
 *
 * @author lengleng
 * @date 2024/9/1
 */
@AutoConfiguration
public class ExcelAutoConfiguration {

	/**
	 * REST 客户端构建器（支持负载均衡）
	 * @return {@link RestClient.Builder }
	 */
	@Bean
	@LoadBalanced
	@ConditionalOnProperty(value = "spring.cloud.nacos.discovery.enabled", havingValue = "true", matchIfMissing = true)
	RestClient.Builder restClientBuilder() {
		return RestClient.builder();
	}

	/**
	 * 远程 dict API 服务
	 * @return {@link RemoteDictApiService }
	 */
	@Bean
	@ConditionalOnMissingBean
	public RemoteDictApiService remoteDictApiService(Optional<RestClient.Builder> restClientBuilderOptional) {
		RestClient client = restClientBuilderOptional.orElseGet(RestClient::builder)
			.baseUrl(getBaseUrl())
			.defaultHeader(SecurityConstants.FROM, SecurityConstants.FROM_IN)
			.build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(client)).build();
		return factory.createClient(RemoteDictApiService.class);
	}

	/**
	 * dict 数据提供程序
	 * @param remoteDictApiService 远程 dict API 服务
	 * @return {@link DictDataProvider }
	 */
	@Bean
	@ConditionalOnMissingBean
	public DictDataProvider dictDataProvider(RemoteDictApiService remoteDictApiService) {
		return new RemoteDictDataProvider(remoteDictApiService);
	}

	/**
	 * 获取Base URL
	 * @return {@link String }
	 */
	private String getBaseUrl() {
		// 根据当前架构模式，组装URL
		if (SpringContextHolder.isMicro()) {
			return String.format("http://%s", ServiceNameConstants.UPMS_SERVICE);
		}
		else {
			return String.format("http://%s", SpringContextHolder.getEnvironment()
				.resolvePlaceholders("127.0.0.1:${server.port}${server.servlet.context-path}"));
		}
	}

}
