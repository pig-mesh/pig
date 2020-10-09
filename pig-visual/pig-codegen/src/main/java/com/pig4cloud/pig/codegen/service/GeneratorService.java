/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenConfig;

import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/7/29
 */
public interface GeneratorService {

	/**
	 * 生成代码
	 * @param tableNames 表名称
	 * @return
	 */
	byte[] generatorCode(GenConfig tableNames);

	/**
	 * 分页查询表
	 * @param page 分页信息
	 * @param tableName 表名
	 * @param name 数据源ID
	 * @return
	 */
	IPage<List<Map<String, Object>>> getPage(Page page, String tableName, String name);

	/**
	 * 预览代码
	 * @param genConfig 查询条件
	 * @return
	 */
	Map<String, String> previewCode(GenConfig genConfig);

}
