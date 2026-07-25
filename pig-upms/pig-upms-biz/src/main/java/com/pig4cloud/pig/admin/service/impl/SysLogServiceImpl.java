/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.dto.SysLogDTO;
import com.pig4cloud.pig.admin.api.entity.SysLog;
import com.pig4cloud.pig.admin.api.vo.PreLogVO;
import com.pig4cloud.pig.admin.mapper.SysLogMapper;
import com.pig4cloud.pig.admin.service.SysLogService;
import com.pig4cloud.pig.common.log.util.LogTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2017-11-20
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

	/**
	 * 批量插入前端错误日志
	 * @param preLogVoList 日志信息
	 * @return true/false
	 */
	@Override
	public Boolean saveBatchLogs(List<PreLogVO> preLogVoList) {
		List<SysLog> sysLogs = preLogVoList.stream().map(pre -> {
			SysLog log = new SysLog();
			log.setLogType(LogTypeEnum.ERROR.getType());
			log.setTitle(pre.getInfo());
			log.setException(pre.getStack());
			log.setParams(pre.getMessage());
			log.setCreateTime(LocalDateTime.now());
			log.setRequestUri(pre.getUrl());
			log.setCreateBy(pre.getUser());
			return log;
		}).toList();
		return this.saveBatch(sysLogs);
	}

	@Override
	public Page getLogByPage(Page page, SysLogDTO sysLog) {

		LambdaQueryWrapper<SysLog> wrapper = Wrappers.lambdaQuery();
		if (StrUtil.isNotBlank(sysLog.getLogType())) {
			wrapper.eq(SysLog::getLogType, sysLog.getLogType());
		}

		if (ArrayUtil.isNotEmpty(sysLog.getCreateTime())) {
			wrapper.ge(SysLog::getCreateTime, sysLog.getCreateTime()[0])
				.le(SysLog::getCreateTime, sysLog.getCreateTime()[1]);
		}

		if (StrUtil.isNotBlank(sysLog.getTitle())) {
			wrapper.like(SysLog::getTitle, sysLog.getTitle());
		}

		wrapper.eq(StrUtil.isNotBlank(sysLog.getServiceId()), SysLog::getServiceId, sysLog.getServiceId());
		return baseMapper.selectPage(page, wrapper);
	}

	/**
	 * 插入日志
	 * @param sysLog 日志对象
	 * @return true/false
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveLog(SysLogDTO sysLog) {
		SysLog log = new SysLog();
		BeanUtils.copyProperties(sysLog, log, "createTime");
		baseMapper.insert(log);
		return Boolean.TRUE;
	}

	/**
	 * sum 函数计算三十天内的数据
	 * @return list map
	 */
	@Override
	public List<Map<String, Object>> getLogSum() {
		List<Map<String, Object>> logSumList = baseMapper.selectLogSum(LocalDateTime.now().minusDays(30));
		Map<String, Map<String, Object>> resultMap = new TreeMap<>();

		for (Map<String, Object> row : logSumList) {
			String createTime = formatCreateTime(row.get(SysLog.Fields.createTime));
			Map<String, Object> logSum = resultMap.computeIfAbsent(createTime, key -> {
				Map<String, Object> item = new LinkedHashMap<>();
				item.put(SysLog.Fields.createTime, key);
				return item;
			});
			logSum.put(row.get(SysLog.Fields.logType).toString(), ((Number) row.get("logCount")).intValue());
		}

		return new ArrayList<>(resultMap.values());
	}

	private String formatCreateTime(Object createTime) {
		if (createTime == null) {
			return StrUtil.EMPTY;
		}
		if (createTime instanceof Date date) {
			return DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
		}
		if (createTime instanceof TemporalAccessor temporalAccessor) {
			return TemporalAccessorUtil.format(temporalAccessor, DatePattern.NORM_DATE_PATTERN);
		}
		return createTime.toString();
	}

}
