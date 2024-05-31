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

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.constant.ScheduleStateEnum;
import com.pig4cloud.pigx.admin.api.entity.SysScheduleEntity;
import com.pig4cloud.pigx.admin.mapper.SysScheduleMapper;
import com.pig4cloud.pigx.admin.service.SysScheduleService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

	/**
	 * 根据范围获取计划列表
	 * @param page 分页对象
	 * @param sysSchedule 计划实体对象
	 * @return 分页对象
	 */
	@Override
	public IPage<SysScheduleEntity> getScheduleByScope(Page page, SysScheduleEntity sysSchedule) {
		LambdaQueryWrapper<SysScheduleEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StrUtil.isNotBlank(sysSchedule.getTitle()), SysScheduleEntity::getTitle, sysSchedule.getTitle());
		wrapper.like(StrUtil.isNotBlank(sysSchedule.getScheduleType()), SysScheduleEntity::getScheduleType, sysSchedule.getScheduleType());
		wrapper.eq(Objects.nonNull(sysSchedule.getScheduleDate()), SysScheduleEntity::getScheduleDate, sysSchedule.getScheduleDate());
		DataScope dataScope = new DataScope();
		dataScope.setUsername(SecurityUtils.getUser().getUsername());
		return baseMapper.selectPageByScope(page, wrapper, dataScope);
	}

	/**
	 * 根据月份查询日程列表
	 * @param startDate 本周开始日期
	 * @param endDate 本周结束日期
	 * @return 日程列表
	 */
	@Override
	public List<SysScheduleEntity> selectListByScope(LocalDate startDate, LocalDate endDate) {
		DateTime beginOfWeek = DateUtil.beginOfWeek(DateUtil.date());
		DateTime endOfWeek = DateUtil.endOfWeek(DateUtil.date());
		LambdaQueryWrapper<SysScheduleEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.between(SysScheduleEntity::getScheduleDate, Objects.isNull(startDate) ? beginOfWeek : startDate,
				Objects.isNull(endDate) ? endOfWeek : endDate);
		wrapper.ne(SysScheduleEntity::getScheduleState, ScheduleStateEnum.END.getCode());
		DataScope dataScope = new DataScope();
		dataScope.setUsername(SecurityUtils.getUser().getUsername());
		return baseMapper.selectListByScope(wrapper, dataScope);
	}

}
