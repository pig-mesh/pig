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

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 动态查询Mapper接口
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Mapper
public interface GenDynamicMapper {

	/**
	 * 动态SQL查询
	 * @param sq SQL查询语句
	 * @return 查询结果列表，每个结果以LinkedHashMap形式存储
	 */
	@InterceptorIgnore(tenantLine = "true")
	List<LinkedHashMap<String, Object>> dynamicQuerySql(@Param("value") String sq);

}
