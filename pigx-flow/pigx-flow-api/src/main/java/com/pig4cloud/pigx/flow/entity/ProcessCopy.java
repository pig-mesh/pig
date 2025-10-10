package com.pig4cloud.pigx.flow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 流程抄送记录实体类
 * <p>
 * 记录流程执行过程中的抄送信息。抄送是指将流程信息通知给相关人员，
 * 但这些人员不需要进行审批操作，仅供查看和了解流程进展。
 * 一个流程节点可以抄送给多个人员，每个抄送生成一条记录。
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@TenantTable
@Accessors(chain = true)
@TableName("process_copy")
public class ProcessCopy {

	/**
	 * 主键ID
	 * 使用雪花算法生成的分布式ID
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 流程发起时间
	 * 记录流程实例的创建时间，用于追踪流程的开始时刻
	 */
	@TableField("start_time")
	private LocalDateTime startTime;

	/**
	 * 抄送节点执行时间
	 * 记录产生抄送的节点执行时间，即何时触发的抄送通知
	 */
	@TableField("node_time")
	private LocalDateTime nodeTime;

	/**
	 * 流程发起人ID
	 * 记录启动流程实例的用户ID，方便追溯流程的发起者
	 */
	@TableField("start_user_id")
	private Long startUserId;

	/**
	 * 流程定义ID
	 * 关联process表的flow_id，标识该抄送属于哪个流程定义
	 */
	@TableField("flow_id")
	private String flowId;

	/**
	 * 流程实例ID
	 * 关联process_instance_record表，标识该抄送属于哪个流程实例
	 */
	@TableField("process_instance_id")
	private String processInstanceId;

	/**
	 * 流程节点ID
	 * 标识在哪个节点产生的抄送，对应流程定义中的节点标识
	 */
	@TableField("node_id")
	private String nodeId;

	/**
	 * 流程分组ID
	 * 流程所属的分组ID，继承自流程定义
	 */
	@TableField("group_id")
	private Long groupId;

	/**
	 * 流程分组名称
	 * 缓存流程分组的名称，方便查询展示，避免关联查询
	 */
	@TableField("group_name")
	private String groupName;

	/**
	 * 流程名称
	 * 缓存流程定义的名称，方便在抄送列表中直接展示
	 */
	@TableField("process_name")
	private String processName;

	/**
	 * 抄送节点名称
	 * 产生抄送的节点名称，如"部门经理审批"、"财务审核"等
	 */
	@TableField("node_name")
	private String nodeName;

	/**
	 * 表单数据快照
	 * JSON格式存储抄送时的表单数据，确保抄送人看到的是当时的数据状态
	 */
	@TableField("form_data")
	private String formData;

	/**
	 * 抄送接收人ID
	 * 接收抄送通知的用户ID，该用户可以查看但不能操作流程
	 */
	@TableField("user_id")
	private Long userId;

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
