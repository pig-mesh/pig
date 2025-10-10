package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * 任务数据传输对象
 * <p>
 * 该DTO用于传输流程任务的详细信息。包含任务的基本信息、
 * 执行信息、关联的流程信息等。主要用于任务列表展示、
 * 任务详情查看、任务处理等场景。
 * </p>
 * 
 * @author pigx
 */
@Data
public class TaskDto {

	/**
	 * 流程定义ID
	 * <p>
	 * 任务所属的流程定义唯一标识。
	 * 标识该任务属于哪个流程模板。
	 * </p>
	 */
	private String flowId;

	/**
	 * 流程变量集合
	 * <p>
	 * 存储流程执行过程中的各种变量。
	 * 包含表单数据、控制变量、业务数据等。
	 * 这些变量可用于任务处理和决策。
	 * </p>
	 */
	private Map<String, Object> paramMap;

	/**
	 * 流程实例ID
	 * <p>
	 * 任务所属的流程实例唯一标识。
	 * 用于关联到具体的流程实例。
	 * </p>
	 */
	private String processInstanceId;

	/**
	 * 执行ID
	 * <p>
	 * Flowable引擎的执行ID。
	 * 用于关联流程引擎中的执行实例。
	 * </p>
	 */
	private String executionId;

	/**
	 * 任务耗时
	 * <p>
	 * 任务从创建到完成的时间，单位为毫秒。
	 * 用于统计任务处理效率。
	 * </p>
	 */
	private Long durationInMillis;

	/**
	 * 任务ID
	 * <p>
	 * Flowable引擎生成的任务唯一标识。
	 * 用于任务的查询和操作。
	 * </p>
	 */
	private String taskId;

	/**
	 * 任务执行人
	 * <p>
	 * 当前任务的执行人用户标识。
	 * 可能是用户ID或用户名，取决于系统配置。
	 * </p>
	 */
	private String assign;

	/**
	 * 任务名称
	 * <p>
	 * 任务的显示名称，如"部门经理审批"。
	 * 对应流程节点的名称。
	 * </p>
	 */
	private String taskName;

	/**
	 * 节点ID
	 * <p>
	 * 任务对应的流程节点ID。
	 * 用于定位任务在流程中的位置。
	 * </p>
	 */
	private String nodeId;

	/**
	 * 任务创建时间
	 * <p>
	 * 任务在流程引擎中的创建时间。
	 * 标记任务何时产生。
	 * </p>
	 */
	private Date taskCreateTime;

	/**
	 * 任务结束时间
	 * <p>
	 * 任务完成或终止的时间。
	 * 用于计算任务处理时长。
	 * </p>
	 */
	private Date taskEndTime;

	/**
	 * 流程组名称
	 * <p>
	 * 流程所属分组的名称，如"行政类流程"、"财务类流程"。
	 * 用于流程分类展示。
	 * </p>
	 */
	private String groupName;

	/**
	 * 流程发起人ID
	 * <p>
	 * 发起该流程实例的用户ID。
	 * 用于显示流程的发起者信息。
	 * </p>
	 */
	private Long rootUserId;

	/**
	 * 流程发起人姓名
	 * <p>
	 * 发起该流程实例的用户姓名。
	 * 用于界面显示，提高可读性。
	 * </p>
	 */
	private String rootUserName;

	/**
	 * 流程发起人头像URL
	 * <p>
	 * 发起人的头像地址。
	 * 用于在界面上展示发起人头像，提升用户体验。
	 * </p>
	 */
	private String rootUserAvatarUrl;

	/**
	 * 流程发起时间
	 * <p>
	 * 流程实例的创建时间。
	 * 记录流程何时开始。
	 * </p>
	 */
	private LocalDateTime startTime;

	/**
	 * 流程名称
	 * <p>
	 * 流程定义的名称，如"请假申请流程"、"报销流程"。
	 * 用于界面显示，便于用户理解。
	 * </p>
	 */
	private String processName;

}
