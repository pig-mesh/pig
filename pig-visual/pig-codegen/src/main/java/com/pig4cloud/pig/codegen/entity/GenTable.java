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

package com.pig4cloud.pig.codegen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 列属性
 *
 * @author pigx code generator
 * @date 2023-02-06 20:34:55
 */
@Data
@TableName("gen_table")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "列属性")
public class GenTable extends Model<GenTable> {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "id")
	private Long id;

	/**
	 * 数据源名称
	 */
	@Schema(description = "数据源名称")
	private String dsName;

	/**
	 * 数据源类型
	 */
	@Schema(description = "数据源类型")
	private String dbType;

	/**
	 * 表名
	 */
	@Schema(description = "表名")
	private String tableName;

	/**
	 * 类名
	 */
	@Schema(description = "类名")
	private String className;

	/**
	 * 说明
	 */
	@Schema(description = "说明")
	private String tableComment;

	/**
	 * 作者
	 */
	@Schema(description = "作者")
	private String author;

	/**
	 * 邮箱
	 */
	@Schema(description = "邮箱")
	private String email;

	/**
	 * 项目包名
	 */
	@Schema(description = "项目包名")
	private String packageName;

	/**
	 * 项目版本号
	 */
	@Schema(description = "项目版本号")
	private String version;

	/**
	 * 生成方式 0：zip压缩包 1：自定义目录
	 */
	@Schema(description = "生成方式  0：zip压缩包   1：自定义目录")
	private String generatorType;

	/**
	 * 后端生成路径
	 */
	@Schema(description = "后端生成路径")
	private String backendPath;

	/**
	 * 前端生成路径
	 */
	@Schema(description = "前端生成路径")
	private String frontendPath;

	/**
	 * 模块名
	 */
	@Schema(description = "模块名")
	private String moduleName;

	/**
	 * 功能名
	 */
	@Schema(description = "功能名")
	private String functionName;

	/**
	 * 表单布局 1：一列 2：两列
	 */
	@Schema(description = "表单布局  1：一列   2：两列")
	private Integer formLayout;

	/**
	 * 基类ID
	 */
	@Schema(description = "基类ID")
	private Long baseclassId;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 代码生成风格
	 */
	private Long style;

	/**
	 * 子表名称
	 */
	private String childTableName;

	/**
	 * 主表关联键
	 */
	private String mainField;

	/**
	 * 子表关联键
	 */
	private String childField;

	/**
	 * 字段列表
	 */
	@TableField(exist = false)
	private List<GenTableColumnEntity> fieldList;

	/**
	 * 子表字段列表
	 */
	@TableField(exist = false)
	private List<GenTableColumnEntity> childFieldList;

	/**
	 * 代码风格（模版分组信息）
	 */
	@TableField(exist = false)
	private List<GenGroupEntity> groupList;

}
