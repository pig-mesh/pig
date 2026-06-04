package com.pig4cloud.pig.codegen.service.impl;
/*
 *      Copyright (c) 2018-2025, luolin All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: luolin (766488893@qq.com)
 */

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.entity.GenCreateTable;
import com.pig4cloud.pig.codegen.mapper.GenCreateTableMapper;
import com.pig4cloud.pig.codegen.service.GenCreateTableService;
import com.pig4cloud.pig.codegen.util.table.CreateTableHandler;
import com.pig4cloud.pig.codegen.util.table.model.TableInfo;
import com.pig4cloud.pig.codegen.util.vo.GenCreateTableColumnVO;
import com.pig4cloud.pig.codegen.util.vo.GenCreateTableVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anyline.metadata.Column;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 自动创建表管理
 *
 * @author luolin
 * @date 2022-09-23 21:56:11
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CreateTableServiceImpl extends ServiceImpl<GenCreateTableMapper, GenCreateTable>
		implements GenCreateTableService {

	private final CreateTableHandler createTableHandler;

	@Override
	public Boolean createTable(GenCreateTableVO createTableVO) {
		TableInfo tableInfo = new TableInfo();
		tableInfo.setName(createTableVO.getTableName());
		LinkedHashMap<String, Column> columns = new LinkedHashMap<>();
		List<GenCreateTableColumnVO> columnInfoList = createTableVO.getColumnInfo();
		if (columnInfoList != null) {
			for (GenCreateTableColumnVO columnInfo : columnInfoList) {
				Column column = buildColumn(columnInfo);
				columns.put(column.getName(), column);
			}
		}
		tableInfo.setColumns(columns);
		tableInfo.setComment(createTableVO.getComments());
		return createTableHandler.createTable(createTableVO.getDsName(), tableInfo);
	}

	private Column buildColumn(GenCreateTableColumnVO columnInfo) {
		Column column = new Column();
		column.setName(columnInfo.getName());
		column.setType(columnInfo.getType());
		column.setComment(columnInfo.getComment());
		column.setDefaultValue(columnInfo.getDefaultValue());

		if (columnInfo.getLength() != null) {
			column.setLength(columnInfo.getLength());
		}

		if (columnInfo.getPrecision() != null) {
			column.setPrecision(columnInfo.getPrecision());
		}

		if (columnInfo.getScale() != null) {
			column.setScale(columnInfo.getScale());
		}

		if (columnInfo.getNullable() != null) {
			column.setNullable(columnInfo.getNullable());
		}

		if (columnInfo.getPrimary() != null) {
			column.setPrimary(columnInfo.getPrimary());
		}

		if (columnInfo.getAutoIncrement() != null) {
			column.setAutoIncrement(columnInfo.getAutoIncrement());
		}
		return column;
	}

}
