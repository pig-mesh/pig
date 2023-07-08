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

package com.pig4cloud.pig.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author lengleng
 * @date 2018/8/27 前端日志vo
 */
@Data
@Schema(description = "前端日志展示对象")
public class PreLogVO {

	/**
	 * 请求url
	 */
	@Schema(description = "请求url")
	private String url;

	/**
	 * 请求耗时
	 */
	@Schema(description = "请求耗时")
	private String time;

	/**
	 * 请求用户
	 */
	@Schema(description = "请求用户")
	private String user;

	/**
	 * 请求结果
	 */
	@Schema(description = "请求结果0:成功9:失败")
	private String type;

	/**
	 * 请求传递参数
	 */
	@Schema(description = "请求传递参数")
	private String message;

	/**
	 * 异常信息
	 */
	@Schema(description = "异常信息")
	private String stack;

	/**
	 * 日志标题
	 */
	@Schema(description = "日志标题")
	private String info;

}
