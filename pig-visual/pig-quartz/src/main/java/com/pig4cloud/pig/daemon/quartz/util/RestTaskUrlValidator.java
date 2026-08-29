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

package com.pig4cloud.pig.daemon.quartz.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.pig4cloud.pig.daemon.quartz.config.QuartzProtectionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Locale;
import java.util.Set;

/**
 * REST 定时任务 URL 白名单校验器。
 */
@Component
@RequiredArgsConstructor
public class RestTaskUrlValidator {

	private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");

	private final QuartzProtectionProperties properties;

	/**
	 * 校验任务地址是否命中 URL 源白名单。
	 * @param executePath REST 任务地址
	 * @return 是否允许请求
	 */
	public boolean isAllowed(String executePath) {
		URI target = parseHttpUri(executePath, false);
		if (target == null || CollUtil.isEmpty(properties.getRestTaskUrlWhitelist())) {
			return false;
		}

		return properties.getRestTaskUrlWhitelist()
			.stream()
			.map(item -> parseHttpUri(item, true))
			.anyMatch(allowed -> sameOrigin(target, allowed));
	}

	private URI parseHttpUri(String value, boolean originOnly) {
		if (StrUtil.isBlank(value)) {
			return null;
		}

		try {
			URI uri = URLUtil.toURI(value).normalize();
			String scheme = uri.getScheme();
			if (StrUtil.isBlank(scheme) || !ALLOWED_SCHEMES.contains(scheme.toLowerCase(Locale.ROOT))
					|| StrUtil.isBlank(uri.getHost()) || uri.getUserInfo() != null) {
				return null;
			}
			if (originOnly && (StrUtil.isNotBlank(uri.getQuery()) || StrUtil.isNotBlank(uri.getFragment())
					|| !(StrUtil.isBlank(uri.getPath()) || StrUtil.equals(uri.getPath(), "/")))) {
				return null;
			}
			return uri;
		}
		catch (RuntimeException ex) {
			return null;
		}
	}

	private boolean sameOrigin(URI target, URI allowed) {
		return allowed != null && target.getScheme().equalsIgnoreCase(allowed.getScheme())
				&& normalizeHost(target.getHost()).equals(normalizeHost(allowed.getHost()))
				&& effectivePort(target) == effectivePort(allowed);
	}

	private String normalizeHost(String host) {
		return host.toLowerCase(Locale.ROOT);
	}

	private int effectivePort(URI uri) {
		if (uri.getPort() >= 0) {
			return uri.getPort();
		}
		return "https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
	}

}
