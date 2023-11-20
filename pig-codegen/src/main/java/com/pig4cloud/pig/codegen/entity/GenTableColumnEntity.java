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

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lengleng
 * @date 2023-02-06
 *
 * 记录表字段的配置信息
 */
@Data
@TableName("gen_table_column")
@EqualsAndHashCode(callSuper = true)
public class GenTableColumnEntity extends Model<GenDatasourceConf> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 数据源名
	 */
	private String dsName;

	/**
	 * 表名称
	 */
	private String tableName;

	/**
	 * 字段名称
	 */
	private String fieldName;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 字段类型
	 */
	private String fieldType;

	/**
	 * 字段说明
	 */
	private String fieldComment;

	/**
	 * 属性名
	 */
	private String attrName;

	/**
	 * 属性类型
	 */
	private String attrType;

	/**
	 * 属性包名
	 */
	private String packageName;

	/**
	 * 自动填充
	 */
	private String autoFill;

	/**
	 * 主键 0：否 1：是
	 */
	private String primaryPk;

	/**
	 * 基类字段 0：否 1：是
	 */
	private String baseField;

	/**
	 * 表单项 0：否 1：是
	 */
	private String formItem;

	/**
	 * 表单必填 0：否 1：是
	 */
	private String formRequired;

	/**
	 * 表单类型
	 */
	private String formType;

	/**
	 * 表单效验
	 */
	private String formValidator;

	/**
	 * 列表项 0：否 1：是
	 */
	private String gridItem;

	/**
	 * 列表排序 0：否 1：是
	 */
	private String gridSort;

	/**
	 * 查询项 0：否 1：是
	 */
	private String queryItem;

	/**
	 * 查询方式
	 */
	private String queryType;

	/**
	 * 查询表单类型
	 */
	private String queryFormType;

	/**
	 * 字段字典类型
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String fieldDict;

}
