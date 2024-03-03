package com.pig4cloud.pigx.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统消息推送记录
 *
 * @author pig
 * @date 2023-10-25 18:07:23
 */
@Data
@TenantTable
@TableName("sys_message_relation")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统消息推送记录")
public class SysMessageRelationEntity extends Model<SysMessageRelationEntity> {

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	/**
	 * 消息ID
	 */
	@Schema(description = "消息ID")
	private Long msgId;

	/**
	 * 接收人ID
	 */
	@Schema(description = "接收人ID")
	private Long userId;

	/**
	 * 内容
	 */
	@Schema(description = "内容")
	private String content;

	/**
	 * 已读（0否，1是）
	 */
	@Schema(description = "已读（0否，1是）")
	private String readFlag;

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
	 * 更新人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新人")
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 删除时间
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除时间")
	private String delFlag;

	/**
	 * 租户
	 */
	@Schema(description = "租户")
	private Long tenantId;

}
