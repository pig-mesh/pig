package com.pig4cloud.pigx.ureport.config;

import com.bstek.ureport.UReportPropertyPlaceholderConfigurer;
import com.bstek.ureport.provider.report.ReportProvider;
import com.pig4cloud.pigx.common.file.core.FileProperties;
import com.pig4cloud.pigx.common.file.core.FileTemplate;
import com.pig4cloud.pigx.common.file.oss.service.OssTemplate;
import com.pig4cloud.pigx.ureport.processor.UReportPropertyPlaceholderConfigurerPlus;
import com.pig4cloud.pigx.ureport.provider.DfsReportProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * @author lengleng
 * @date 2020/7/25
 * <p>
 * 增强默认Ureport 行为
 */
@ConditionalOnClass(OssTemplate.class)
@Configuration(proxyBeanMethods = false)
public class UreportExtConfig {

	@Bean
	public ReportProvider dfsReportProvider(FileTemplate template, FileProperties properties) {
		return new DfsReportProvider(template, properties);
	}

	@Bean
	public UReportPropertyPlaceholderConfigurer uReportPropertyPlaceholderConfigurerPlus() {
		return new UReportPropertyPlaceholderConfigurerPlus();
	}

	@Bean
	@Primary
	public LocaleResolver localeResolver() {

		AcceptHeaderLocaleResolver lr = new AcceptHeaderLocaleResolver();

		// 设置默认区域,

		lr.setDefaultLocale(Locale.CHINA);

		return lr;

	}

}
