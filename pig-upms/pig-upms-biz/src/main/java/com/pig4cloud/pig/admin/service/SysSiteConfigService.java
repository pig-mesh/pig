/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
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

package com.pig4cloud.pig.admin.service;

import com.pig4cloud.pig.admin.dto.SiteConfigDTO;

import java.util.Map;

/**
 * 站点配置服务
 *
 * @author lengleng
 * @date 2026-03-27
 */
public interface SysSiteConfigService {

	/**
	 * 获取聚合配置（i18n + 站点配置）
	 * @return Map
	 */
	Map<String, Object> getAggregatedConfig();

	/**
	 * 获取站点配置
	 * @return SiteConfigDTO
	 */
	SiteConfigDTO getSiteConfig();

	/**
	 * 更新站点配置
	 * @param dto 站点配置DTO
	 */
	void updateSiteConfig(SiteConfigDTO dto);

	/**
	 * 刷新站点配置缓存（清空 Redis 中的 site_config_details）
	 */
	void refreshCache();

}
