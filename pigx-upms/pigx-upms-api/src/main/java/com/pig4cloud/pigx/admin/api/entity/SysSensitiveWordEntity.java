package com.pig4cloud.pigx.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import java.time.LocalDateTime;

/**
 * 敏感词
 *
 * @author pig
 * @date 2024-07-06 15:54:43
 */
@Data
@TenantTable
@TableName("sys_sensitive_word")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "敏感词")
public class SysSensitiveWordEntity extends Model<SysSensitiveWordEntity> {


	/**
	* 主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键")
    private Long sensitiveId;

	/**
	* 敏感词
	*/
    @Schema(description="敏感词")
    private String sensitiveWord;

	/**
	* 类型
	*/
    @Schema(description="类型")
    private String sensitiveType;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

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
