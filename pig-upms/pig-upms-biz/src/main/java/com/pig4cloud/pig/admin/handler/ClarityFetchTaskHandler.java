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

package com.pig4cloud.pig.admin.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pig.admin.api.constant.ClarityFetchStatus;
import com.pig4cloud.pig.admin.api.entity.SysClarityData;
import com.pig4cloud.pig.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pig.admin.mapper.SysClarityDataMapper;
import com.pig4cloud.pig.admin.mapper.SysSocialDetailsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Clarity API 异步数据拉取任务
 *
 * @author lengleng
 * @date 2026-03-26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClarityFetchTaskHandler {

	private static final String CLARITY_API_BASE = "https://www.clarity.ms/export-data/api/v1/project-live-insights?numOfDays=";

	private static final String CLARITY_TYPE = "clarity";

	private final SysClarityDataMapper clarityDataMapper;

	private final SysSocialDetailsMapper socialDetailsMapper;

	/**
	 * 异步从 Clarity API 拉取数据并存库 策略：先插入占位行（PENDING），调用成功后更新数据（SUCCESS），失败则标记（FAILED）。
	 * 并发时第二个请求查到 PENDING/SUCCESS 即跳过，彻底避免重复调用。
	 * @param numOfDays Clarity API numOfDays 参数（1=24h, 2=48h, 3=72h）
	 */
	@Async
	public void fetchAndSave(int numOfDays) {
		try {
			// 幂等检查：今日已有 PENDING 或 SUCCESS 记录则跳过（FAILED 允许重试）
			boolean alreadyRunning = clarityDataMapper.exists(Wrappers.<SysClarityData>lambdaQuery()
				.eq(SysClarityData::getDataDate, LocalDate.now())
				.eq(SysClarityData::getNumOfDays, numOfDays)
				.in(SysClarityData::getFetchStatus, ClarityFetchStatus.PENDING, ClarityFetchStatus.SUCCESS));
			if (alreadyRunning) {
				log.debug("[Clarity] 今日 numOfDays={} 已有进行中或成功记录，跳过", numOfDays);
				return;
			}

			// 获取 Clarity 配置（type='clarity'）
			SysSocialDetails config = socialDetailsMapper
				.selectOne(Wrappers.<SysSocialDetails>lambdaQuery().eq(SysSocialDetails::getType, CLARITY_TYPE));
			if (config == null) {
				log.warn("[Clarity] 未找到 type='clarity' 的配置，跳过拉取");
				return;
			}

			// 插入占位行，防止并发重复调用
			SysClarityData placeholder = new SysClarityData();
			placeholder.setDataDate(LocalDate.now());
			placeholder.setNumOfDays(numOfDays);
			placeholder.setFetchStatus(ClarityFetchStatus.PENDING);
			clarityDataMapper.insert(placeholder);
			log.info("[Clarity] 占位行已插入，id={}，numOfDays={}", placeholder.getId(), numOfDays);

			// 调用 Clarity API
			HttpResponse response = HttpRequest.get(CLARITY_API_BASE + numOfDays)
				.header("Authorization", "Bearer " + config.getAppSecret())
				.header("Content-Type", "application/json")
				.execute();

			if (!response.isOk()) {
				log.error("[Clarity] API 调用失败，状态码：{}", response.getStatus());
				// 标记失败，下次调度可重试
				updateStatus(placeholder.getId(), ClarityFetchStatus.FAILED);
				return;
			}

			// 解析响应并填充数据
			JSONArray body = JSONUtil.parseArray(response.body());
			SysClarityData data = parseResponse(body);
			data.setId(placeholder.getId());
			data.setFetchStatus(ClarityFetchStatus.SUCCESS);
			clarityDataMapper.updateById(data);
			log.info("[Clarity] 数据拉取并存库成功，日期：{}，numOfDays：{}", LocalDate.now(), numOfDays);
		}
		catch (Exception e) {
			log.error("[Clarity] 数据拉取异常", e);
		}
	}

	private void updateStatus(Long id, ClarityFetchStatus status) {
		SysClarityData update = new SysClarityData();
		update.setId(id);
		update.setFetchStatus(status);
		clarityDataMapper.updateById(update);
	}

	/**
	 * 解析 Clarity API 响应体（JSONArray 结构）到实体 字段映射已通过真实 API 调用确认（2026-03-26） 所有 count 类字段为
	 * String 类型，使用 Convert.toInt 安全转换（null/空串返回 0）
	 */
	private SysClarityData parseResponse(JSONArray responseArray) {
		SysClarityData data = new SysClarityData();

		for (Object item : responseArray) {
			if (!(item instanceof JSONObject metric)) {
				continue;
			}
			String metricName = metric.getStr("metricName");
			JSONArray info = metric.getJSONArray("information");
			if (info == null || info.isEmpty()) {
				continue;
			}
			JSONObject first = info.getJSONObject(0);

			switch (metricName) {
				case "Traffic" -> {
					data.setTotalSessions(Convert.toInt(first.getStr("totalSessionCount"), 0));
					data.setDistinctUsers(Convert.toInt(first.getStr("distinctUserCount"), 0));
					data.setPagesPerSession(first.getBigDecimal("pagesPerSessionPercentage"));
				}
				case "ScrollDepth" -> data.setScrollDepth(first.getBigDecimal("averageScrollDepth"));
				case "DeadClickCount" -> data.setDeadClickRate(first.getBigDecimal("sessionsWithMetricPercentage"));
				case "RageClickCount" -> data.setRageClickRate(first.getBigDecimal("sessionsWithMetricPercentage"));
				case "Device" -> data.setDeviceData(toJsonRanking(info.stream(), dev -> dev.getStr("name"),
						dev -> Convert.toInt(dev.getStr("sessionsCount"), 0)));
				case "PopularPages" -> data.setTopUrls(toJsonRanking(info.stream().limit(5), page -> page.getStr("url"),
						page -> Convert.toInt(page.getStr("visitsCount"), 0)));
				case "ReferrerUrl" -> data.setReferrerUrlData(toJsonRanking(info.stream().limit(5),
						ref -> StrUtil.blankToDefault(ref.getStr("url"), ref.getStr("name", "unknown")),
						ref -> Convert.toInt(ref.getStr("sessionsCount"), 0)));
				case "PageTitle" -> data.setPageTitleData(toJsonRanking(info.stream().limit(5),
						pt -> StrUtil.blankToDefault(pt.getStr("name"), pt.getStr("title", "unknown")),
						pt -> Convert.toInt(pt.getStr("sessionsCount"), 0)));
				case "Browser" -> data.setBrowserData(
						toJsonRanking(info.stream(), br -> StrUtil.blankToDefault(br.getStr("name"), "unknown"),
								br -> Convert.toInt(br.getStr("sessionsCount"), 0)));
				default -> {
				}
			}
		}

		return data;
	}

	/**
	 * 将 JSONArray 流转换为 [{name, value}] 格式的 JSON 字符串
	 */
	private String toJsonRanking(Stream<?> stream, Function<JSONObject, String> nameExtractor,
			Function<JSONObject, Integer> valueExtractor) {
		List<Map<String, Object>> list = new ArrayList<>();
		stream.forEach(item -> {
			if (item instanceof JSONObject obj) {
				list.add(Map.of("name", nameExtractor.apply(obj), "value", valueExtractor.apply(obj)));
			}
		});
		return JSONUtil.toJsonStr(list);
	}

}
