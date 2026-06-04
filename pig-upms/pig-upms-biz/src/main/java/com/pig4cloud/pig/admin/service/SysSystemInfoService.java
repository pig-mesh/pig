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

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysClarityData;
import com.pig4cloud.pig.common.core.util.R;

import java.util.Map;

/**
 * 系统监控服务
 *
 * @author lengleng
 * @date 2026-03-26
 */
public interface SysSystemInfoService extends IService<SysClarityData> {

	/**
	 * 获取 Redis 缓存监控信息
	 * @return R
	 */
	R<Map<String, Object>> getCacheInfo();

	/**
	 * 获取今日 Clarity 数据 查库优先：有数据直接返回；无数据触发异步拉取并返回 loading 状态
	 * @param numOfDays Clarity API numOfDays 参数（1=24h, 2=48h, 3=72h）
	 * @return R
	 */
	R<?> getClarityData(int numOfDays);

}
