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

package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clarity 监控数据缓存
 *
 * @author lengleng
 * @date 2026-03-26
 */
@Data
@Schema(description = "Clarity 监控数据缓存")
@EqualsAndHashCode(callSuper = true)
public class SysClarityData extends Model<SysClarityData> {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	@Schema(description = "数据日期")
	private LocalDate dataDate;

	@Schema(description = "总会话数（Traffic.totalSessionCount）")
	private Integer totalSessions;

	@Schema(description = "独立访客数 UV（Traffic.distinctUserCount）")
	private Integer distinctUsers;

	@Schema(description = "每会话页面数（Traffic.pagesPerSessionPercentage）")
	private BigDecimal pagesPerSession;

	@Schema(description = "平均滚动深度 %（ScrollDepth.averageScrollDepth）")
	private BigDecimal scrollDepth;

	@Schema(description = "死点击率 %（DeadClickCount.sessionsWithMetricPercentage）")
	private BigDecimal deadClickRate;

	@Schema(description = "激怒点击率 %（RageClickCount.sessionsWithMetricPercentage）")
	private BigDecimal rageClickRate;

	@Schema(description = "设备分布 JSON 文本，格式：[{name,value}]")
	private String deviceData;

	@Schema(description = "热门页面 Top10 JSON 文本，格式：[{name,value}]")
	private String topUrls;

	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建人")
	private String createBy;

	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "修改人")
	private String updateBy;

	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记，1:已删除，0:正常")
	private String delFlag;

	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

}
