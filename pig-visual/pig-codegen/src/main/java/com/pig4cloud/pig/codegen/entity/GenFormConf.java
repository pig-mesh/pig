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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pig4cloud.pig.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 生成记录
 *
 * @author lengleng
 * @date 2019-08-12 15:55:35
 */
@Data
@TableName("gen_form_conf")
@Schema(description = "生成记录")
@EqualsAndHashCode(callSuper = true)
public class GenFormConf extends BaseEntity {

	/**
	 * ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "ID")
	private Long id;

	/**
	 * 表名称
	 */
	@Schema(description = "表名称")
	private String tableName;

	/**
	 * 表单信息
	 */
	@Schema(description = "表单信息")
	private String formInfo;

	/**
	 * 删除标记
	 */
	@Schema(description = "删除标记")
	private String delFlag;

}
