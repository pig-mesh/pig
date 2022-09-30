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
package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.pig4cloud.pig.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典项
 *
 * @author lengleng
 * @date 2019/03/19
 */
@Data
@Schema(description = "字典项")
@EqualsAndHashCode(callSuper = true)
public class SysDictItem extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "字典项id")
	private Long id;

	/**
	 * 所属字典类id
	 */
	@Schema(description = "所属字典类id")
	private Long dictId;

	/**
	 * 所属字典类id
	 */
	@Schema(description = "所属字典类key")
	private String dictKey;

	/**
	 * 数据值
	 */
	@Schema(description = "数据值")
	private String value;

	/**
	 * 标签名
	 */
	@Schema(description = "标签名")
	private String label;

	/**
	 * 类型
	 */
	@Schema(description = "类型")
	private String type;

	/**
	 * 描述
	 */
	@Schema(description = "描述")
	private String description;

	/**
	 * 排序（升序）
	 */
	@Schema(description = "排序值，默认升序")
	private Integer sortOrder;

	/**
	 * 备注信息
	 */
	@Schema(description = "备注信息")
	private String remark;

	/**
	 * 删除标记
	 */
	@TableLogic
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

}
