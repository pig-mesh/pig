/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 */

package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.constant.ClarityFetchStatus;
import com.pig4cloud.pig.admin.api.entity.SysClarityData;
import com.pig4cloud.pig.admin.handler.ClarityFetchTaskHandler;
import com.pig4cloud.pig.admin.mapper.SysClarityDataMapper;
import com.pig4cloud.pig.admin.service.SysSystemInfoService;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
	public R<?> getClarityData(int numOfDays) {
		// 只返回成功的缓存行
		SysClarityData today = baseMapper.selectOne(Wrappers.<SysClarityData>lambdaQuery()
			.eq(SysClarityData::getDataDate, LocalDate.now())
			.eq(SysClarityData::getNumOfDays, numOfDays)
			.eq(SysClarityData::getFetchStatus, ClarityFetchStatus.SUCCESS));

		if (today != null) {
			return R.ok(today);
		}

		// 检查是否有 FAILED 行（可重试）或 PENDING 行（等待中）
		SysClarityData existing = baseMapper.selectOne(Wrappers.<SysClarityData>lambdaQuery()
			.eq(SysClarityData::getDataDate, LocalDate.now())
			.eq(SysClarityData::getNumOfDays, numOfDays)
			.in(SysClarityData::getFetchStatus, ClarityFetchStatus.PENDING, ClarityFetchStatus.FAILED));

		if (existing != null && existing.getFetchStatus() == ClarityFetchStatus.FAILED) {
			// FAILED 行：删除后重试
			baseMapper.deleteById(existing.getId());
		}

		if (existing == null || existing.getFetchStatus() == ClarityFetchStatus.FAILED) {
			clarityFetchTask.fetchAndSave(numOfDays);
		}

		Map<String, Object> loading = new HashMap<>(2);
		loading.put("loading", true);
		loading.put("message", "数据获取中，请稍后刷新");
		return R.ok(loading);
	}

}
