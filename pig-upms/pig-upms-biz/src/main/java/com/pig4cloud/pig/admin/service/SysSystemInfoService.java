/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 */

package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysClarityData;
import com.pig4cloud.pig.common.core.util.R;

/**
 * 系统监控服务
 *
 * @author lengleng
 * @date 2026-03-26
 */
public interface SysSystemInfoService extends IService<SysClarityData> {

	/**
	 * 获取今日 Clarity 数据（查库优先，无数据触发异步拉取）
	 * @param numOfDays Clarity API numOfDays 参数（1=24h, 2=48h, 3=72h）
	 */
	R<?> getClarityData(int numOfDays);

}
