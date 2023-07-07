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

package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenTable;

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
	 * @return 默认配置信息
	 */
	Map<String, Object> getGeneratorConfig();

	/**
	 * 分页查询表格列表
	 * @param page 分页对象
	 * @param table 查询条件
	 * @return 表格列表分页结果
	 */
	IPage list(Page<GenTable> page, GenTable table);

	/**
	 * 根据数据源名称和表名查询或构建表格
	 * @param dsName 数据源名称
	 * @param tableName 表名
	 * @return 查询到的表格信息
	 */
	GenTable queryOrBuildTable(String dsName, String tableName);

	/**
	 * 查询指定数据源下的所有表格
	 * @param dsName 数据源名称
	 * @return 所有表格的列表
	 */
	List<Map<String, Object>> queryDsAllTable(String dsName);

	/**
	 * 查询指定数据源和表名的列信息
	 * @param dsName 数据源名称
	 * @param tableName 表名
	 * @return 列信息列表
	 */
	List<Map<String, String>> queryColumn(String dsName, String tableName);

}
