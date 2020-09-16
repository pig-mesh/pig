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

package com.pig4cloud.pig.codegen.entity;

import lombok.Data;

/**
 * @author lengleng
 * @date 2018/8/2 生成配置
 */
@Data
public class GenConfig {

	/**
	 * 数据源name
	 */
	private String dsName;

	/**
	 * 包名
	 */
	private String packageName;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 模块名称
	 */
	private String moduleName;

	/**
	 * 表前缀
	 */
	private String tablePrefix;

	/**
	 * 表名称
	 */
	private String tableName;

	/**
	 * 表备注
	 */
	private String comments;

	/**
	 * 代码风格 0 - avue 1 - element
	 */
	private String style;

}
