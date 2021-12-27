package com.pig4cloud.pigx.common.swagger.config;

import com.pig4cloud.pigx.common.swagger.support.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

/**
 * @author lengleng
 * @date 2020/10/2
 * <p>
 * 网关swagger 配置类，仅在webflux 环境生效哦
 */
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GatewaySwaggerAutoConfiguration {

	@Bean
	@Primary
	public SwaggerProvider swaggerProvider(RouteDefinitionRepository repository, SwaggerProperties swaggerProperties,
			DiscoveryClient discoveryClient) {
		return new SwaggerProvider(repository, swaggerProperties, discoveryClient);
	}

	@Bean
	public SwaggerResourceHandler swaggerResourceHandler(SwaggerProvider swaggerProvider) {
		return new SwaggerResourceHandler(swaggerProvider);
	}

	@Bean
	public WebFluxSwaggerConfiguration fluxSwaggerConfiguration() {
		return new WebFluxSwaggerConfiguration();
	}

	@Bean
	public SwaggerSecurityHandler swaggerSecurityHandler(
			ObjectProvider<SecurityConfiguration> securityConfigurationObjectProvider) {
		SecurityConfiguration securityConfiguration = securityConfigurationObjectProvider
				.getIfAvailable(() -> SecurityConfigurationBuilder.builder().build());
		return new SwaggerSecurityHandler(securityConfiguration);
	}

	@Bean
	public SwaggerUiHandler swaggerUiHandler(ObjectProvider<UiConfiguration> uiConfigurationObjectProvider) {
		UiConfiguration uiConfiguration = uiConfigurationObjectProvider
				.getIfAvailable(() -> UiConfigurationBuilder.builder().build());
		return new SwaggerUiHandler(uiConfiguration);
	}

	@Bean
	public RouterFunction swaggerRouterFunction(SwaggerProperties swaggerProperties, SwaggerUiHandler swaggerUiHandler,
			SwaggerSecurityHandler swaggerSecurityHandler, SwaggerResourceHandler swaggerResourceHandler) {
		// 开启swagger 正常执行路由
		if (swaggerProperties.getEnabled()) {
			return RouterFunctions
					.route(RequestPredicates.GET("/swagger-resources").and(RequestPredicates.accept(MediaType.ALL)),
							swaggerResourceHandler)
					.andRoute(RequestPredicates.GET("/swagger-resources/configuration/ui")
							.and(RequestPredicates.accept(MediaType.ALL)), swaggerUiHandler)
					.andRoute(RequestPredicates.GET("/swagger-resources/configuration/security")
							.and(RequestPredicates.accept(MediaType.ALL)), swaggerSecurityHandler);
		}
		else {
			// 关闭时，返回404
			return RouterFunctions
					.route(RequestPredicates.GET("/*/v2/api-docs").and(RequestPredicates.accept(MediaType.ALL)),
							serverRequest -> ServerResponse.notFound().build())
					.andRoute(RequestPredicates.GET("/swagger-ui/**").and(RequestPredicates.accept(MediaType.ALL)),
							serverRequest -> ServerResponse.notFound().build())
					.andRoute(RequestPredicates.GET("/doc.html").and(RequestPredicates.accept(MediaType.ALL)),
							serverRequest -> ServerResponse.notFound().build());
		}
	}

}
