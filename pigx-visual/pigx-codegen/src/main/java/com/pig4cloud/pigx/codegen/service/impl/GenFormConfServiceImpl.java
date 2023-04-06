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

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.codegen.entity.GenFormConf;
import com.pig4cloud.pigx.codegen.mapper.GenFormConfMapper;
import com.pig4cloud.pigx.codegen.service.GenFormConfService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	public List<JSONObject> parse(String formInfo) {
		JSONObject jsonObject = JSONUtil.parseObj(formInfo);
		List<JSONObject> objectList = new ArrayList<>();
		// 使用 Lambda 表达式简化代码
		Optional.ofNullable(jsonObject.getJSONArray("widgetList"))//
				.ifPresent(widgetList -> widgetList.forEach(obj -> parseWidgetList((JSONObject) obj, objectList)));
		return objectList;
	}

	/**
	 * 解析部件列表
	 * @param jsonObject 部件 json 对象
	 * @param objectList 对象列表
	 */
	private void parseWidgetList(JSONObject jsonObject, List<JSONObject> objectList) {
		Optional.ofNullable(jsonObject.getJSONArray("cols"))//
				.filter(cols -> !cols.isEmpty())//
				.ifPresent(cols -> cols.forEach(colObj -> parseWidgetList((JSONObject) colObj, objectList)));
		objectList.add(jsonObject);
	}

}
