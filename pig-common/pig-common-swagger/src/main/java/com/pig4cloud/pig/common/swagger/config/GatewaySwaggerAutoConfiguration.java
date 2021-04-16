package com.pig4cloud.pig.common.swagger.config;

import com.pig4cloud.pig.common.swagger.support.SwaggerProperties;
import com.pig4cloud.pig.common.swagger.support.SwaggerResourceHandler;
import com.pig4cloud.pig.common.swagger.support.SwaggerSecurityHandler;
import com.pig4cloud.pig.common.swagger.support.SwaggerUiHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author lengleng
 * @date 2020/10/2
 * <p>
 * 网关swagger 配置类，仅在webflux 环境生效哦
 */
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ComponentScan("com.pig4cloud.pig.common.swagger.support")
public class GatewaySwaggerAutoConfiguration {

	private final SwaggerResourceHandler swaggerResourceHandler;

	private final SwaggerSecurityHandler swaggerSecurityHandler;

	private final SwaggerUiHandler swaggerUiHandler;

	private final SwaggerProperties swaggerProperties;

	@Bean
	public WebFluxSwaggerConfiguration fluxSwaggerConfiguration() {
		return new WebFluxSwaggerConfiguration();
	}

	@Bean
	public RouterFunction swaggerRouterFunction() {
		// 开启swagger 匹配路由
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
			return RouterFunctions.route(
					RequestPredicates.GET("/swagger-ui/**").and(RequestPredicates.accept(MediaType.ALL)),
					serverRequest -> ServerResponse.notFound().build());
		}
	}

}
