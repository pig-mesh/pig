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

package com.pig4cloud.pigx.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.codegen.entity.GenTable;

import java.util.List;
import java.util.Map;

/**
 * 列属性
 *
 * @author pigx code generator
 * @date 2023-02-06 20:34:55
 */
public interface GenTableService extends IService<GenTable> {

	/**
	 * 获取默认配置信息
	 * @param path 配置文件路径
	 * @return
	 */
	Map<String, Object> getGeneratorConfig();

	IPage list(Page<GenTable> page, GenTable table);

	GenTable queryOrBuildTable(String dsName, String tableName);

	List<Map<String, Object>> queryDsAllTable(String dsName);

	List<Map<String, String>> queryColumn(String dsName, String tableName);

}
