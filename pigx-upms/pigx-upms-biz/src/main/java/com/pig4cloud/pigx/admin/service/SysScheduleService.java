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

package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysScheduleEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * 日程
 *
 * @author aeizzz
 * @date 2023-03-06 14:26:23
 */
public interface SysScheduleService extends IService<SysScheduleEntity> {

	/**
	 * 根据分页信息和日程实体获取指定范围的日程列表
	 * @param page 分页信息
	 * @param sysSchedule 日程实体
	 * @return 日程列表
	 */
	IPage<SysScheduleEntity> getScheduleByScope(Page page, SysScheduleEntity sysSchedule);

	/**
	 * 根据月份查询日程列表
	 * @return 日程列表
	 */
	List<SysScheduleEntity> selectListByScope(LocalDate startDate, LocalDate endDate);

}
