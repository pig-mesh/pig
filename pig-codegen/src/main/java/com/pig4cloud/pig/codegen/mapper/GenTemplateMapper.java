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
import com.pig4cloud.pig.codegen.entity.GenTemplateEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 模板
 *
 * @author PIG
 * @date 2023-02-21 17:15:44
 */
@Mapper
public interface GenTemplateMapper extends BaseMapper<GenTemplateEntity> {

	/**
	 * 根据groupId查询 模板
	 * @param groupId
	 * @return
	 */
	List<GenTemplateEntity> listTemplateById(Long groupId);

}
