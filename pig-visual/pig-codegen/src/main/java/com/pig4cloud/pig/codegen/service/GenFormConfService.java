/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pig.codegen.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenFormConf;

import java.util.List;
import java.util.Map;

import java.util.Map;

/**
 * 表单管理
 *
 * @author lengleng
 * @date 2019-08-12 15:55:35
 */
public interface GenFormConfService extends IService<GenFormConf> {

	/**
<<<<<<< HEAD:pig-visual/pig-codegen/src/main/java/com/pig4cloud/pig/codegen/service/GenFormConfService.java
	 * 解析 form json
	 * @param formInfo json
	 * @return 字段
	 */
	Map<String, List<JSONObject>> parse(String formInfo);
=======
	 * 获取 Redis 缓存监控信息
	 * @return R
	 */
	R<Map<String, Object>> getCacheInfo();

	/**
	 * 获取今日 Clarity 数据
	 * 查库优先：有数据直接返回；无数据触发异步拉取并返回 loading 状态
	 * @param numOfDays Clarity API numOfDays 参数（1=24h, 2=48h, 3=72h）
	 * @return R
	 */
	R<?> getClarityData(int numOfDays);
>>>>>>> 152957533 (feat(clarity): 增强 Clarity 监控数据，支持多维度分析与时间范围选择):pigx-upms/pigx-upms-biz/src/main/java/com/pig4cloud/pigx/admin/service/SysSystemInfoService.java

}
