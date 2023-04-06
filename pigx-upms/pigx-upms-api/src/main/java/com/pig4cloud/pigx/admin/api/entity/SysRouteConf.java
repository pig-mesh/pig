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

package com.pig4cloud.pigx.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 路由
 *
 * @author lengleng
 * @date 2018-11-06 10:17:18
 */
@Data
@Schema(description = "网关路由信息")
@EqualsAndHashCode(callSuper = true)
public class SysRouteConf extends Model<SysRouteConf> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	/**
	 * 路由ID
	 */
	@Schema(description = "路由id")
	private String routeId;

	/**
	 * 路由名称
	 */
	@Schema(description = "路由名称")
	private String routeName;

	/**
	 * 断言
	 */
	@Schema(description = "断言")
	private String predicates;

	/**
	 * 过滤器
	 */
	@Schema(description = "过滤器")
	private String filters;

	/**
	 * uri
	 */
	@Schema(description = "请求uri")
	private String uri;

	/**
	 * 排序
	 */
	@Schema(description = "排序值")
	private Integer sortOrder;

	@Schema(description = "元数据")
	private String metadata;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 删除标识（0-正常,1-删除）
	 */
	@TableLogic
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

}
