/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pig.common.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author Hccake
 * @date 2025-12-12
 * <p>
 * 国际化配置 - 支持多 JAR 包扫描
 */
@Slf4j
@Configuration
public class MessageSourceConfiguration {

	/**
	 * 创建并配置国际化消息源Bean
	 *
	 * @return 配置好的消息源实例，支持通配符资源加载
	 */
	@Bean
	public MessageSource pigMessageSource() {
		WildcardReloadableResourceBundleMessageSource messageSource = new WildcardReloadableResourceBundleMessageSource();
		// 设置基础包名，支持通配符扫描多个 JAR
		messageSource.setBasename("i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(3600);
		messageSource.setDefaultLocale(Locale.CHINA);
		return messageSource;
	}

	/**
	 * 支持 classpath*: 通配符的 MessageSource 实现 能够扫描所有 JAR 包中的 i18n 资源文件
	 */
	@Slf4j
	static class WildcardReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {
		private static final String PROPERTIES_SUFFIX = ".properties";

		private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		private static final Pattern LOCALE_PROPERTIES_FILE_NAME_PATTERN = Pattern.compile(".*_([a-z]{2}(_[A-Z]{2})?(_[A-Z]+)?)\\.properties$");

		public WildcardReloadableResourceBundleMessageSource() {
			super.setResourceLoader(this.resolver);
		}

		/**
		 * 计算给定bundle基名和Locale的所有文件名。会计算指定Locale、系统Locale（如果适用）和默认文件的文件名。
		 * 当基名包含通配符时，会移除基名并添加通配符匹配到的资源文件路径。
		 *
		 * @param basename bundle的基名
		 * @param locale   区域设置
		 * @return 要检查的文件名列表
		 * @see #setFallbackToSystemLocale
		 * @see #calculateFilenamesForLocale
		 */
		@Override
		protected List<String> calculateAllFilenames(String basename, Locale locale) {
			// 父类默认的方法会将 basename 也放入 filenames 列表
			List<String> filenames = super.calculateAllFilenames(basename, locale);
			// 当 basename 有匹配符时，从 filenames 中移除，否则扫描文件将抛出 Illegal char <*> 的异常
			if (containsWildcard(basename)) {
				filenames.remove(basename);
				try {
					Resource[] resources = getResources(basename);
					for (Resource resource : resources) {
						String resourceUriStr = resource.getURI().toString();
						// 根据通配符匹配到的多个 basename 对应的文件添加到文件列表末尾，作为兜底匹配
						if (!LOCALE_PROPERTIES_FILE_NAME_PATTERN.matcher(resourceUriStr).matches()) {
							String sourcePath = resourceUriStr.replace(PROPERTIES_SUFFIX, "");
							filenames.add(sourcePath);
						}
					}
				} catch (IOException ex) {
					log.error("读取国际化信息文件异常", ex);
				}
			}
			return filenames;
		}

		/**
		 * 计算指定基名和区域设置的资源文件名列表
		 *
		 * @param basename 资源文件基名
		 * @param locale   区域设置
		 * @return 存在的资源文件名列表（不包含.properties后缀）
		 */
		@Override
		protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
			// 资源文件名
			List<String> fileNames = new ArrayList<>();
			// 获取到待匹配的国际化信息文件名集合
			List<String> matchFilenames = super.calculateFilenamesForLocale(basename, locale);
			for (String matchFilename : matchFilenames) {
				try {
					Resource[] resources = getResources(matchFilename);
					for (Resource resource : resources) {
						if (resource.exists()) {
							String sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
							fileNames.add(sourcePath);
						}
					}
				} catch (IOException ex) {
					log.error("读取国际化信息文件异常", ex);
				}
			}
			return fileNames;
		}

		/**
		 * 根据资源路径模式获取资源数组
		 *
		 * @param resourceLocationPattern 资源路径模式（支持用"."表示文件层级）
		 * @return 匹配的资源数组
		 * @throws IOException 获取资源过程中发生IO异常时抛出
		 */
		private Resource[] getResources(String resourceLocationPattern) throws IOException {
			// 支持用 . 表示文件层级
			resourceLocationPattern = resourceLocationPattern.replace(".", "/");
			return this.resolver.getResources("classpath*:" + resourceLocationPattern + PROPERTIES_SUFFIX);
		}

		/**
		 * 检查字符串是否包含通配符
		 *
		 * @param str 要检查的字符串
		 * @return 如果字符串包含通配符返回true，否则返回false
		 */
		private boolean containsWildcard(String str) {
			if (str.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
				str = str.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length());
			}
			return str.contains("*");
		}
	}

}
