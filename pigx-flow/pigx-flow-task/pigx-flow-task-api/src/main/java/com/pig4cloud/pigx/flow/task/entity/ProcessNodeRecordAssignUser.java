package com.pig4cloud.pigx.flow.task.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 流程节点记录-执行人
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@TenantTable
@Accessors(chain = true)
@TableName("process_node_record_assign_user")
public class ProcessNodeRecordAssignUser {

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
	@TableField("data")
	private String data;

	@TableField("node_id")
	private String nodeId;

	/**
	 * 用户id
	 */
	@TableField("user_id")
	private String userId;

	/**
	 * 节点状态
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 开始时间
	 */
	@TableField("start_time")
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@TableField("end_time")
	private LocalDateTime endTime;

	/**
	 * 执行id
	 */
	@TableField("execution_id")
	private String executionId;

	/**
	 * 任务id
	 */
	@TableField("task_id")
	private String taskId;

	/**
	 * 审批意见
	 */
	@TableField("approve_desc")
	private String approveDesc;

	/**
	 * 节点名称
	 */
	@TableField("node_name")
	private String nodeName;

	/**
	 * 任务类型
	 */
	@TableField("task_type")
	private String taskType;

	/**
	 * 表单本地数据
	 */
	@TableField("local_data")
	private String localData;

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
