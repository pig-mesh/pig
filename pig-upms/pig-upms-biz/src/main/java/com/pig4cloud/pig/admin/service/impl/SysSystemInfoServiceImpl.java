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

package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysClarityData;
import com.pig4cloud.pig.admin.handler.ClarityFetchTaskHandler;
import com.pig4cloud.pig.admin.mapper.SysClarityDataMapper;
import com.pig4cloud.pig.admin.service.SysSystemInfoService;
import com.pig4cloud.pig.admin.api.constant.UpmsErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * 系统监控服务实现
 *
 * @author lengleng
 * @date 2026-03-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysSystemInfoServiceImpl extends ServiceImpl<SysClarityDataMapper, SysClarityData>
		implements SysSystemInfoService {

	private final ClarityFetchTaskHandler clarityFetchTask;

	@Override
	public R<Map<String, Object>> getCacheInfo() {
		Properties info = RedisUtils.execute((RedisCallback<Properties>) RedisServerCommands::info);
		Properties commandStats = RedisUtils
			.execute((RedisCallback<Properties>) connection -> connection.serverCommands().info("commandstats"));
		Object dbSize = RedisUtils.execute((RedisCallback<Object>) RedisServerCommands::dbSize);

		if (commandStats == null) {
			return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_SYSTEM_CACHE_FETCH_ERROR));
		}

		Map<String, Object> result = new HashMap<>(3);
		result.put("info", info);
		result.put("dbSize", dbSize);

		List<Map<String, String>> pieList = new ArrayList<>();
		commandStats.stringPropertyNames().forEach(key -> {
			Map<String, String> data = new HashMap<>(2);
			String property = commandStats.getProperty(key);
			data.put("name", StringUtils.removeStart(key, "cmdstat_"));
			data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
			pieList.add(data);
		});

		result.put("commandStats", pieList);
		return R.ok(result);
	}

	@Override
	public R<?> getClarityData(int numOfDays) {
		// 查询今日对应 numOfDays 的缓存数据
		SysClarityData today = baseMapper.selectOne(Wrappers.<SysClarityData>lambdaQuery()
			.eq(SysClarityData::getDataDate, LocalDate.now())
			.eq(SysClarityData::getNumOfDays, numOfDays));

		if (today != null) {
			return R.ok(today);
		}

		// 无数据：触发异步拉取，立即返回 loading 状态
		clarityFetchTask.fetchAndSave(numOfDays);

		Map<String, Object> loading = new HashMap<>(2);
		loading.put("loading", true);
		loading.put("message", "数据获取中，请稍后手动刷新");
		return R.ok(loading);
	}

}
