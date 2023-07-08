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
package com.pig4cloud.pig.codegen.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.entity.GenFieldType;
import com.pig4cloud.pig.codegen.mapper.GenFieldTypeMapper;
import com.pig4cloud.pig.codegen.service.GenFieldTypeService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 列属性
 *
 * @author pigx code generator
 * @date 2023-02-06 20:16:01
 */
@Service
public class GenFieldTypeServiceImpl extends ServiceImpl<GenFieldTypeMapper, GenFieldType>
		implements GenFieldTypeService {

	/**
	 * 根据tableId，获取包列表
	 * @param dsName 数据源名称
	 * @param tableName 表名称
	 * @return 返回包列表
	 */
	@Override
	public Set<String> getPackageByTableId(String dsName, String tableName) {
		Set<String> importList = baseMapper.getPackageByTableId(dsName, tableName);
		return importList.stream().filter(StrUtil::isNotBlank).collect(Collectors.toSet());
	}

}
