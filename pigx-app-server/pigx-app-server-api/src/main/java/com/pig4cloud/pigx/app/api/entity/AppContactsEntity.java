package com.pig4cloud.pigx.app.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 通讯录表
 *
 * @author pigx
 * @date 2025-05-29 17:30:30
 */
@Data
@TenantTable
@TableName("app_contacts")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "通讯录表")
public class AppContactsEntity extends Model<AppContactsEntity> {


    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 联系人
     */
    @Schema(description = "联系人")
    private String contactName;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String contactPhone;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改人")
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "删除标记")
    private String delFlag;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long tenantId;
}