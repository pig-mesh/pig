package com.pig4cloud.pigx.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

/**
 * 系统配置
 *
 * @author pig
 * @date 2024-07-14 20:58:54
 */
@Data
@TenantTable
@FieldNameConstants
@TableName("sys_system_config")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统配置")
public class SysSystemConfigEntity extends Model<SysSystemConfigEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long id;

	/**
	* 配置类型
	*/
    @Schema(description="配置类型")
    private String configType;

	/**
	* 配置名称
	*/
    @Schema(description="配置名称")
    private String configName;

	/**
	* 配置标识
	*/
    @Schema(description="配置标识")
    private String configKey;

	/**
	* 配置值
	*/
    @Schema(description="配置值")
    private String configValue;

	/**
	* 开启状态
	*/
    @Schema(description="开启状态")
    private String configStatus;

	/**
	* 创建人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建人")
    private String createBy;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

	/**
	* 修改人
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改人")
    private String updateBy;

	/**
	* 修改时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改时间")
    private LocalDateTime updateTime;

	/**
	* 删除标记
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="删除标记")
    private String delFlag;

	/**
	* 租户ID
	*/
    @Schema(description="租户ID")
    private Long tenantId;
}
