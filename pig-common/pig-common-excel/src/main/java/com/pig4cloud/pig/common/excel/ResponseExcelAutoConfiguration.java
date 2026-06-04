package com.pig4cloud.pig.common.excel;

import com.pig4cloud.pig.common.excel.aop.DynamicNameAspect;
import com.pig4cloud.pig.common.excel.aop.RequestExcelArgumentResolver;
import com.pig4cloud.pig.common.excel.aop.ResponseExcelReturnValueHandler;
import com.pig4cloud.pig.common.excel.config.ExcelConfigProperties;
import com.pig4cloud.pig.common.excel.processor.NameProcessor;
import com.pig4cloud.pig.common.excel.processor.NameSpelExpressionProcessor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2020/3/29
 * <p>
 * 配置初始化
 */
@AutoConfiguration
@Import(ExcelHandlerConfiguration.class)
@EnableConfigurationProperties(ExcelConfigProperties.class)
public class ResponseExcelAutoConfiguration {

	/**
	 * SPEL 解析处理器
	 * @return NameProcessor excel名称解析器
	 */
	@Bean
	@ConditionalOnMissingBean
	public NameProcessor nameProcessor() {
		return new NameSpelExpressionProcessor();
	}

	/**
	 * Excel名称解析处理切面
	 * @param nameProcessor SPEL 解析处理器
	 * @return DynamicNameAspect
	 */
	@Bean
	@ConditionalOnMissingBean
	public DynamicNameAspect dynamicNameAspect(NameProcessor nameProcessor) {
		return new DynamicNameAspect(nameProcessor);
	}

	/**
	 * 注册 Excel 请求参数解析器。
	 */
	@Bean
	public WebMvcConfigurer excelWebMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
				resolvers.add(new RequestExcelArgumentResolver());
			}
		};
	}

	/**
	 * 将 Excel 返回值处理器放到 Spring MVC 处理链最前面，确保 @RestController 的
	 * @param requestMappingHandlerAdapter Spring MVC 方法适配器
	 * @param responseExcelReturnValueHandler Excel 返回值处理器
	 * @return 容器初始化完成后的处理器注册回调
	 * @ResponseBody 处理器不会先消费 @ResponseExcel 返回值。
	 */
	@Bean
	public SmartInitializingSingleton excelReturnValueHandlerInitializer(
			RequestMappingHandlerAdapter requestMappingHandlerAdapter,
			ResponseExcelReturnValueHandler responseExcelReturnValueHandler) {
		return () -> {
			List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter
				.getReturnValueHandlers();
			if (returnValueHandlers == null || returnValueHandlers.contains(responseExcelReturnValueHandler)) {
				return;
			}
			List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>(returnValueHandlers.size() + 1);
			newHandlers.add(responseExcelReturnValueHandler);
			newHandlers.addAll(returnValueHandlers);
			requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
		};
	}

}
