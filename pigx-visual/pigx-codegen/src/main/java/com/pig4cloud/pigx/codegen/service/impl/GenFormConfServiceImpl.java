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
package com.pig4cloud.pigx.codegen.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.codegen.entity.GenFormConf;
import com.pig4cloud.pigx.codegen.mapper.GenFormConfMapper;
import com.pig4cloud.pigx.codegen.service.GenFormConfService;
import com.pig4cloud.pigx.codegen.util.VFormConfigConsts;
import com.pig4cloud.pigx.codegen.util.VFormTypeEnum;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 表单管理
 *
 * @author lengleng
 * @date 2019-08-12 15:55:35
 */
@Service
public class GenFormConfServiceImpl extends ServiceImpl<GenFormConfMapper, GenFormConf> implements GenFormConfService {

	/**
	 * 表单解析方法
	 * @param formInfo json
	 * @return 字段列表
	 */
	@Override
	public Map<String, List<JSONObject>> parse(String formInfo) {
		JSONObject jsonObject = JSONUtil.parseObj(formInfo);
		Map<String, List<JSONObject>> resultMap = new LinkedHashMap<>();
		parseForm(jsonObject, resultMap);
		return resultMap;
	}

	/**
	 * 解析 form config
	 * @param json 表单配置
	 * @param resultMap 返回结果
	 */
	private void parseForm(JSONObject json, Map<String, List<JSONObject>> resultMap) {
		JSONArray widgetList = json.getJSONArray(VFormConfigConsts.widgetList);
		String groupKey = json.getStr(VFormConfigConsts.groupKey, RandomUtil.randomString(5));
		widgetList.stream().map(o -> (JSONObject) o).forEach(jsonObject -> {
			String type = jsonObject.getStr(VFormConfigConsts.type);
			if (VFormTypeEnum.GRID.getType().equals(type)) {
				parseGrid(jsonObject, resultMap);
			}
			else if (VFormTypeEnum.GRID_COL.getType().equals(type)) {
				parseForm(jsonObject, resultMap);
			}
			else {
				int span = Optional.ofNullable(json.getJSONObject(VFormConfigConsts.options))
					.map(options -> options.getInt(VFormConfigConsts.span, 24))
					.orElse(24);

				jsonObject.set(VFormConfigConsts.span, span);

				List<JSONObject> jsonObjects = resultMap.computeIfAbsent(groupKey, k -> new ArrayList<>());
				jsonObjects.add(jsonObject);
			}
		});
	}

	/**
	 * 解析 grid 容器
	 * @param jsonObject 入参
	 * @param resultMap 结果
	 */
	private void parseGrid(JSONObject jsonObject, Map<String, List<JSONObject>> resultMap) {
		JSONArray cols = jsonObject.getJSONArray(VFormConfigConsts.cols);
		String key = jsonObject.getStr(VFormConfigConsts.key);
		cols.stream().map(o -> (JSONObject) o).forEach(col -> {
			String colType = col.getStr(VFormConfigConsts.type);
			if (VFormTypeEnum.GRID_COL.getType().equals(colType)) {
				col.set(VFormConfigConsts.groupKey, key);
				parseForm(col, resultMap);
			}

			if (VFormTypeEnum.GRID.getType().equals(colType)) {
				parseGrid(col, resultMap);
			}
		});
	}

}
