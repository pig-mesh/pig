/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
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
import com.pig4cloud.pig.admin.api.entity.SysPublicParam;
import com.pig4cloud.pig.admin.mapper.SysClarityDataMapper;
import com.pig4cloud.pig.admin.mapper.SysPublicParamMapper;
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

	private static final String CLARITY_KEY = "CLARITY_API_KEY";

	private final SysClarityDataMapper clarityDataMapper;

	private final SysPublicParamMapper publicParamMapper;

	@Async
	public void fetchAndSave(int numOfDays) {
		Long placeholderId = null;
		try {
			boolean alreadyRunning = clarityDataMapper.exists(Wrappers.<SysClarityData>lambdaQuery()
				.eq(SysClarityData::getDataDate, LocalDate.now())
				.eq(SysClarityData::getNumOfDays, numOfDays)
				.in(SysClarityData::getFetchStatus, ClarityFetchStatus.PENDING, ClarityFetchStatus.SUCCESS));
			if (alreadyRunning) {
				return;
			}

			SysPublicParam config = publicParamMapper.selectOne(
					Wrappers.<SysPublicParam>lambdaQuery().eq(SysPublicParam::getPublicKey, CLARITY_KEY));
			if (config == null || config.getPublicValue() == null) {
				log.warn("[Clarity] 未配置公共参数 CLARITY_API_KEY，插入 FAILED 占位行");
				SysClarityData failed = new SysClarityData();
				failed.setDataDate(LocalDate.now());
				failed.setNumOfDays(numOfDays);
				failed.setFetchStatus(ClarityFetchStatus.FAILED);
				clarityDataMapper.insert(failed);
				return;
			}

			SysClarityData placeholder = new SysClarityData();
			placeholder.setDataDate(LocalDate.now());
			placeholder.setNumOfDays(numOfDays);
			placeholder.setFetchStatus(ClarityFetchStatus.PENDING);
			clarityDataMapper.insert(placeholder);
			placeholderId = placeholder.getId();

			HttpResponse response = HttpRequest.get(CLARITY_API_BASE + numOfDays)
				.header("Authorization", "Bearer " + config.getPublicValue())
				.header("Content-Type", "application/json")
				.execute();

			if (!response.isOk()) {
				log.error("[Clarity] API 调用失败，状态码：{}", response.getStatus());
				updateStatus(placeholderId, ClarityFetchStatus.FAILED);
				return;
			}

			JSONArray body = JSONUtil.parseArray(response.body());
			SysClarityData data = parseResponse(body);
			data.setId(placeholderId);
			data.setFetchStatus(ClarityFetchStatus.SUCCESS);
			clarityDataMapper.updateById(data);
			log.info("[Clarity] 数据拉取成功，日期：{}，numOfDays：{}", LocalDate.now(), numOfDays);
		}
		catch (Exception e) {
			log.error("[Clarity] 数据拉取异常", e);
			if (placeholderId != null) {
				updateStatus(placeholderId, ClarityFetchStatus.FAILED);
			}
		}
	}

	private void updateStatus(Long id, ClarityFetchStatus status) {
		SysClarityData update = new SysClarityData();
		update.setId(id);
		update.setFetchStatus(status);
		clarityDataMapper.updateById(update);
	}

	private SysClarityData parseResponse(JSONArray responseArray) {
		SysClarityData data = new SysClarityData();
		for (Object item : responseArray) {
			if (!(item instanceof JSONObject metric)) continue;
			String metricName = metric.getStr("metricName");
			JSONArray info = metric.getJSONArray("information");
			if (info == null || info.isEmpty()) continue;
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
				case "Device" -> data.setDeviceData(toJsonRanking(info.stream(),
						dev -> dev.getStr("name"), dev -> Convert.toInt(dev.getStr("sessionsCount"), 0)));
				case "PopularPages" -> data.setTopUrls(toJsonRanking(info.stream().limit(5),
						page -> page.getStr("url"), page -> Convert.toInt(page.getStr("visitsCount"), 0)));
				case "ReferrerUrl" -> data.setReferrerUrlData(toJsonRanking(info.stream().limit(5),
						ref -> StrUtil.blankToDefault(ref.getStr("url"), ref.getStr("name", "unknown")),
						ref -> Convert.toInt(ref.getStr("sessionsCount"), 0)));
				case "PageTitle" -> data.setPageTitleData(toJsonRanking(info.stream().limit(5),
						pt -> StrUtil.blankToDefault(pt.getStr("name"), pt.getStr("title", "unknown")),
						pt -> Convert.toInt(pt.getStr("sessionsCount"), 0)));
				case "Browser" -> data.setBrowserData(toJsonRanking(info.stream(),
						br -> StrUtil.blankToDefault(br.getStr("name"), "unknown"),
						br -> Convert.toInt(br.getStr("sessionsCount"), 0)));
				default -> {}
			}
		}
		return data;
	}

	private String toJsonRanking(Stream<?> stream,
			Function<JSONObject, String> nameExtractor,
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
