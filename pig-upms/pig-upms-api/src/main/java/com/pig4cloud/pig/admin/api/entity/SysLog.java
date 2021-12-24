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

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pig4cloud.pig.common.mybatis.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ExcelProperty("日志编号")
	@ApiModelProperty(value = "日志编号")
	@JsonSerialize(using = ToStringSerializer.class)
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
	 * 操作IP地址
	 */
	@ExcelProperty("IP")
	@ApiModelProperty(value = "操作ip地址")
	private String remoteAddr;

	/**
	 * 用户浏览器
	 */
	@ExcelProperty("浏览器类型")
	@ApiModelProperty(value = "用户代理")
	private String userAgent;

	/**
	 * 请求URI
	 */
	@ExcelProperty("请求URI")
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
	@ExcelProperty("请求参数")
	@ApiModelProperty(value = "数据")
	private String params;

	/**
	 * 执行时间
	 */
	@ExcelProperty("方法执行时间")
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
	private String delFlag;

}
