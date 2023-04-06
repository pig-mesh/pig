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
package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysScheduleEntity;
import com.pig4cloud.pigx.admin.mapper.SysScheduleMapper;
import com.pig4cloud.pigx.admin.service.SysScheduleService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;

/**
 * 日程
 *
 * @author aeizzz
 * @date 2023-03-06 14:26:23
 */
@Service
public class SysScheduleServiceImpl extends ServiceImpl<SysScheduleMapper, SysScheduleEntity>
		implements SysScheduleService {

	@Override
	public IPage<SysScheduleEntity> getScheduleByScope(Page page, SysScheduleEntity sysSchedule) {
		LambdaQueryWrapper<SysScheduleEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StrUtil.isNotBlank(sysSchedule.getTitle()), SysScheduleEntity::getTitle, sysSchedule.getTitle());
		wrapper.like(StrUtil.isNotBlank(sysSchedule.getType()), SysScheduleEntity::getType, sysSchedule.getType());
		wrapper.eq(Objects.nonNull(sysSchedule.getDate()), SysScheduleEntity::getDate, sysSchedule.getDate());
		DataScope dataScope = new DataScope();
		dataScope.setUsername(SecurityUtils.getUser().getUsername());
		return baseMapper.selectPageByScope(page, wrapper, dataScope);
	}

	@Override
	public List<SysScheduleEntity> selectListByScope(String month) {
		LocalDate parse = LocalDate.parse(month, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate firstDay = parse.with(TemporalAdjusters.firstDayOfMonth()); // 获取当前月的第一天
		LocalDate lastDay = parse.with(TemporalAdjusters.lastDayOfMonth());
		LambdaQueryWrapper<SysScheduleEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.between(SysScheduleEntity::getDate, firstDay, lastDay);
		DataScope dataScope = new DataScope();
		dataScope.setUsername(SecurityUtils.getUser().getUsername());
		return baseMapper.selectListByScope(wrapper, dataScope);
	}

}
