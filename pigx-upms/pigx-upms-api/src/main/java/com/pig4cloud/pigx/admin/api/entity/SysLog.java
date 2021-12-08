/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pigx.admin.api.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author lengleng
 * @since 2017-11-20
 */
@Data
@ApiModel(value = "日志")
public class SysLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ExcelProperty("日志编号")
	@ApiModelProperty(value = "日志编号")
	private Long id;

	/**
	 * 日志类型
	 */
	@NotBlank(message = "日志类型不能为空")
	@ExcelProperty("日志类型（0-正常 9-错误）")
	@ApiModelProperty(value = "日志类型")
	private String type;

	/**
	 * 日志标题
	 */
	@NotBlank(message = "日志标题不能为空")
	@ExcelProperty("日志标题")
	@ApiModelProperty(value = "日志标题")
	private String title;

	/**
	 * 创建者
	 */
	@ExcelProperty("创建人")
	@ApiModelProperty(value = "创建人")
	private String createBy;

	/**
	 * 创建时间
	 */
	@ExcelProperty("创建时间")
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ExcelIgnore
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 操作IP地址
	 */
	@ExcelProperty("操作ip地址")
	@ApiModelProperty(value = "操作ip地址")
	private String remoteAddr;

	/**
	 * 用户代理
	 */
	@ApiModelProperty(value = "用户代理")
	private String userAgent;

	/**
	 * 请求URI
	 */
	@ExcelProperty("浏览器")
	@ApiModelProperty(value = "请求uri")
	private String requestUri;

	/**
	 * 操作方式
	 */
	@ExcelProperty("操作方式")
	@ApiModelProperty(value = "操作方式")
	private String method;

	/**
	 * 操作提交的数据
	 */
	@ExcelProperty("提交数据")
	@ApiModelProperty(value = "提交数据")
	private String params;

	/**
	 * 执行时间
	 */
	@ExcelProperty("执行时间")
	@ApiModelProperty(value = "方法执行时间")
	private Long time;

	/**
	 * 异常信息
	 */
	@ExcelProperty("异常信息")
	@ApiModelProperty(value = "异常信息")
	private String exception;

	/**
	 * 服务ID
	 */
	@ExcelProperty("应用标识")
	@ApiModelProperty(value = "应用标识")
	private String serviceId;

	/**
	 * 删除标记
	 */
	@TableLogic
	@ExcelIgnore
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	private String delFlag;

}
