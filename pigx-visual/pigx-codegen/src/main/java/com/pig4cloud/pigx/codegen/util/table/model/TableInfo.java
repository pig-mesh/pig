package com.pig4cloud.pigx.codegen.util.table.model;
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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.anyline.metadata.Table;

/**
 * 表信息
 *
 * @author luolin
 * @date 2022-09-18 21:56:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TableInfo extends Table<TableInfo> {

	/**
	 * 建表模式
	 */
	private String model = TableModelEnum.CREATE.name().toLowerCase();

	/**
	 * 表CRUD模式
	 */
	private String crudModel;

}
