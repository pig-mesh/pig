package com.pig4cloud.pigx.codegen.util.table;
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

import com.pig4cloud.pigx.codegen.util.table.enums.TableModelEnum;
import com.pig4cloud.pigx.codegen.util.table.model.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anyline.metadata.Table;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.springframework.stereotype.Component;

/**
 * 创建表核心逻辑类
 *
 * @author luolin
 * @date 2022-09-18 21:56:11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTableHandler {

	/**
	 * 运行表单主键ID
	 */
	private final static String RUN_FORM_COLUMN_NAME = "run_form_id";

	/**
	 * 自动创建表
	 * @param tableInfo 表信息
	 */
	public Boolean createTable(TableInfo tableInfo) {
		if (tableInfo.getColumns() == null || tableInfo.getColumns().isEmpty()) {
			throw new RuntimeException("自动创建表[" + tableInfo.getName() + "]中至少需要一个字段");
		}
		if (!TableModelEnum.CREATE.name().toLowerCase().equals(tableInfo.getModel())) {
			throw new RuntimeException("自动创建表[" + tableInfo.getModel() + "]模式的处理未找到,请检查");
		}
		try {
			AnylineService service = ServiceProxy.service();
			// 如果已存在，删除重键
			Table table = service.metadata().table(tableInfo.getName(), false);
			if (null != table)
				service.ddl().drop(table);
			// 执行建表SQL
			service.ddl().create(tableInfo);
			log.info("自动创建表处理完成!");
		}
		catch (Exception e) {
			throw new RuntimeException("自动创建表异常", e);
		}
		return Boolean.TRUE;
	}

}
