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

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 日程
 *
 * @author aeizzz
 * @date 2023-03-06 14:26:23
 */
@Data
@TenantTable
@TableName("sys_schedule")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "日程")
public class SysScheduleEntity extends Model<SysScheduleEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 日程类型
     */
    @Schema(description = "日程类型")
    private String scheduleType;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private String scheduleState;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 时间
     */
    @Schema(description = "时间")
    private LocalTime scheduleTime;

    /**
     * 日期
     */
    @Schema(description = "日期")
    @TableField("schedule_date")
    private LocalDate scheduleDate;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @TableLogic
    @Schema(description = "删除标记")
    @TableField(fill = FieldFill.INSERT)
    private String delFlag;

}
