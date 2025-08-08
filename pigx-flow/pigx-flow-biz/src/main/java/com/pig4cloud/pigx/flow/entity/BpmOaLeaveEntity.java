package com.pig4cloud.pigx.flow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 请假表单
 *
 * @author pigx
 * @date 2025-08-08 09:32:41
 */
@Data
@TenantTable
@TableName("bpm_oa_leave")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "请假表单")
public class BpmOaLeaveEntity extends Model<BpmOaLeaveEntity> {

	/**
	 * 请假表单主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "请假表单主键")
	private Long id;

	/**
	 * 申请人的用户编号
	 */
	@Schema(description = "申请人的用户编号")
	private String username;

	/**
	 * 请假类型
	 */
	@Schema(description = "请假类型")
	private Integer leaveType;

	/**
	 * 请假原因
	 */
	@Schema(description = "请假原因")
	private String leaveReason;

	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间")
	private LocalDateTime endTime;

	/**
	 * 请假天数
	 */
	@Schema(description = "请假天数")
	private Integer leaveDay;

	/**
	 * 请假结果
	 */
	@Schema(description = "请假结果")
	private Integer leaveStatus;

	/**
	 * 流程实例的编号
	 */
	@Schema(description = "流程实例的编号")
	private String processInstanceId;

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建者")
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新者")
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 是否删除
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "是否删除")
	private String delFlag;

	/**
	 * 租户编号
	 */
	@Schema(description = "租户编号")
	private Long tenantId;

}
