package com.pig4cloud.pigx.flow.task.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 流程记录
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@TenantTable
@Accessors(chain = true)
@TableName("process_instance_record")
public class ProcessInstanceRecord {

	/**
	 * 流程名字
	 */
	@TableField("name")
	private String name;

	/**
	 * 头像
	 */
	@TableField("logo")
	private String logo;

	/**
	 * 用户id
	 */
	@TableField("user_id")
	private Long userId;

	/**
	 * 流程id
	 */
	@TableField("flow_id")
	private String flowId;

	/**
	 * 流程实例id
	 */
	@TableField("process_instance_id")
	private String processInstanceId;

	/**
	 * 表单数据
	 */
	@TableField("form_data")
	private String formData;

	/**
	 * 组id
	 */
	@TableField("group_id")
	private Long groupId;

	/**
	 * 组名称
	 */
	@TableField("group_name")
	private String groupName;

	/**
	 * 状态
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 结束时间
	 */
	@TableField("end_time")
	private Date endTime;

	/**
	 * 上级流程实例id
	 */
	@TableField("parent_process_instance_id")
	private String parentProcessInstanceId;

	/**
	 * 用户id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 逻辑删除字段
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}
