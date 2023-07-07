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

package com.pig4cloud.pig.codegen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.codegen.entity.GenFieldType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 列属性
 *
 * @author pigx code generator
 * @date 2023-02-06 20:16:01
 */
@Mapper
public interface GenFieldTypeMapper extends BaseMapper<GenFieldType> {

	/**
	 * 根据tableId，获取包列表
	 * @param dsName 数据源名称
	 * @param tableName 表名称
	 * @return 返回包列表
	 */
	Set<String> getPackageByTableId(@Param("dsName") String dsName, @Param("tableName") String tableName);

}
