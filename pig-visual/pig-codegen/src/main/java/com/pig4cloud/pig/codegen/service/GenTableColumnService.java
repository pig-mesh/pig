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

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenTableColumnEntity;

import java.util.List;

/**
 * 列属性
 *
 * @author pigx code generator
 * @date 2023-02-06 20:16:01
 */
public interface GenTableColumnService extends IService<GenTableColumnEntity> {

	void initFieldList(List<GenTableColumnEntity> tableFieldList);

	void updateTableField(String dsName, String tableName, List<GenTableColumnEntity> tableFieldList);

}
