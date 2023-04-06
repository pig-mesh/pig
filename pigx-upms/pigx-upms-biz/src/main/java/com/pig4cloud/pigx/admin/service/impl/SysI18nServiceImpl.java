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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysI18nEntity;
import com.pig4cloud.pigx.admin.mapper.SysI18nMapper;
import com.pig4cloud.pigx.admin.service.SysI18nService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统表-国际化
 *
 * @author PIG
 * @date 2023-02-14 09:07:01
 */
@Service
public class SysI18nServiceImpl extends ServiceImpl<SysI18nMapper, SysI18nEntity> implements SysI18nService {

	/**
	 * 生成前段需要的i18n 格式内容
	 * @return
	 */
	@Override
	@Cacheable(value = CacheConstants.I18N_DETAILS)
	public Map listMap() {
		List<SysI18nEntity> sysI18nEntities = baseMapper.selectList(null);
		HashMap<String, List<Map<String, String>>> stringListHashMap = new HashMap<>();
		List<Map<String, String>> zhCh = new ArrayList<>();
		List<Map<String, String>> en = new ArrayList<>();
		sysI18nEntities.forEach(item -> {
			HashMap<String, String> zhChMap = new HashMap<>();
			HashMap<String, String> enMap = new HashMap<>();
			zhChMap.put(item.getName(), item.getZhCn());
			enMap.put(item.getName(), item.getEn());
			zhCh.add(zhChMap);
			en.add(enMap);
		});
		stringListHashMap.put("zh-cn", zhCh);
		stringListHashMap.put("en", en);
		return stringListHashMap;
	}

	/**
	 * 同步数据
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.I18N_DETAILS, allEntries = true)
	public R syncI18nCache() {
		return R.ok();
	}

}
