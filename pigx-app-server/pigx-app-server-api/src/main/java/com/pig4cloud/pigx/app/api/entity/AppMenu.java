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

package com.pig4cloud.pigx.app.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 菜单权限表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@Data
@TableName("app_menu")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "菜单权限表")
public class AppMenu extends Model<AppMenu> {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "菜单ID")
	private Long menuId;

	/**
	 * name
	 */
	@ApiModelProperty(value = "name")
	private String name;

	/**
	 * permission
	 */
	@ApiModelProperty(value = "permission")
	private String permission;

	/**
	 * path
	 */
	@ApiModelProperty(value = "path")
	private String path;

	/**
	 * 父菜单ID
	 */
	@ApiModelProperty(value = "父菜单ID")
	private Long parentId;

	/**
	 * 是否显示
	 */
	@ApiModelProperty(value = "是否显示")
	private String visible;

	/**
	 * 排序值
	 */
	@ApiModelProperty(value = "排序值")
	private Integer sortOrder;

	/**
	 * menuType
	 */
	@ApiModelProperty(value = "menuType")
	private String menuType;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createBy;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updateBy;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * delFlag
	 */
	@ApiModelProperty(value = "delFlag")
	private String delFlag;

	/**
	 * 租户ID
	 */
	@ApiModelProperty(value = "租户ID", hidden = true)
	private Long tenantId;

}
