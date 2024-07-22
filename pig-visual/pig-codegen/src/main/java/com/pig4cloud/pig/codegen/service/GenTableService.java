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
import org.anyline.metadata.Table;

import java.util.List;

/**
 * 列属性
 *
 * @author pigx code generator
 * @date 2023-02-06 20:34:55
 */
public interface GenTableService extends IService<GenTable> {

	/**
	 * 查询对应数据源的表
	 * @param page 分页信息
	 * @param table 查询条件
	 * @return 表
	 */
	IPage queryTablePage(Page<Table> page, GenTable table);

	/**
	 * 查询表信息（列），然后插入到中间表中
	 * @param dsName 数据源
	 * @param tableName 表名
	 * @return GenTable
	 */
	GenTable queryOrBuildTable(String dsName, String tableName);

	/**
	 * 查询表ddl 语句
	 * @param dsName 数据源名称
	 * @param tableName 表名称
	 * @return ddl 语句
	 * @throws Exception
	 */
	String queryTableDdl(String dsName, String tableName) throws Exception;

	/**
	 * 查询数据源里面的全部表
	 * @param dsName 数据源名称
	 * @return table
	 */
	List<String> queryTableList(String dsName);

	/**
	 * 查询表的全部字段
	 * @param dsName 数据源
	 * @param tableName 表名称
	 * @return column
	 */
	List<String> queryTableColumn(String dsName, String tableName);

}
