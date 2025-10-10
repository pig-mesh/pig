package com.pig4cloud.pigx.flow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 流程节点记录分配用户实体类
 * <p>
 * 记录流程节点的执行人信息和执行详情。当一个流程节点需要由特定用户处理时， 系统会为每个被分配的用户创建一条记录。这个实体保存了用户的任务执行状态、
 * 处理时间、审批意见等核心信息，是流程执行过程中的重要审计记录。
 * </p>
 * <p>
 * 与ProcessNodeRecord的关系：ProcessNodeRecord记录节点的整体执行情况， 而本实体记录该节点分配给具体用户的执行情况。一个节点可能分配给多个用户
 * （如会签、或签场景），因此一个ProcessNodeRecord可对应多个ProcessNodeRecordAssignUser。
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
	 * 流程定义ID
	 * <p>
	 * 关联到流程定义，标识该任务属于哪个流程模板。 对应Process表的flowId字段，用于追溯任务的流程来源。
	 * </p>
	 */
	@TableField("flow_id")
	private String flowId;

	/**
	 * 流程实例ID
	 * <p>
	 * Flowable引擎中的流程实例标识符，用于关联到具体的流程实例。 通过此ID可以在Flowable引擎中查询流程实例的详细信息和运行状态。
	 * </p>
	 */
	@TableField("process_instance_id")
	private String processInstanceId;

	/**
	 * 节点表单数据
	 * <p>
	 * JSON格式存储的表单数据，记录用户在处理该节点时提交的所有表单信息。 与ProcessNodeData中的data字段类似，但这里专门记录该执行人提交的数据。
	 * 在会签场景下，不同执行人可能提交不同的表单数据。
	 * </p>
	 */
	@TableField("data")
	private String data;

	/**
	 * 节点ID
	 * <p>
	 * 流程定义中的节点标识符，标识任务属于哪个流程节点。 与Node对象的id字段对应，用于定位流程图中的具体位置。
	 * </p>
	 */
	@TableField("node_id")
	private String nodeId;

	/**
	 * 执行用户ID
	 * <p>
	 * 被分配处理该节点任务的用户标识符。 对应系统用户表的用户ID，标识谁负责处理这个任务。 在任务分配时设置，用于权限控制和任务查询。
	 * </p>
	 */
	@TableField("user_id")
	private Long userId;

	/**
	 * 任务执行状态
	 * <p>
	 * 记录该用户对任务的处理状态，对应NodeStatusEnum枚举值： 0-待处理：任务已分配但用户还未处理 1-同意：用户已审批通过 2-拒绝：用户已审批拒绝
	 * 3-撤销：任务已被撤销 4-转办：任务已转交他人处理 5-终止：流程已终止 6-跳过：任务被自动跳过 7-待执行：等待执行中
	 * </p>
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 任务开始时间
	 * <p>
	 * 记录任务分配给用户的时间，即用户开始拥有该任务处理权的时间。 用于计算任务处理时长和监控任务时效性。
	 * </p>
	 */
	@TableField("start_time")
	private LocalDateTime startTime;

	/**
	 * 任务结束时间
	 * <p>
	 * 记录用户完成任务处理的时间，包括同意、拒绝、转办等操作的时间。 与startTime配合可计算任务处理耗时，用于绩效分析和流程优化。
	 * </p>
	 */
	@TableField("end_time")
	private LocalDateTime endTime;

	/**
	 * 流程执行ID
	 * <p>
	 * Flowable引擎中的执行实例标识符，用于精确定位流程执行路径。 在并行网关、子流程等复杂场景下，一个流程实例可能有多个执行实例。
	 * </p>
	 */
	@TableField("execution_id")
	private String executionId;

	/**
	 * Flowable任务ID
	 * <p>
	 * Flowable引擎中的任务实例标识符，是任务在工作流引擎中的唯一标识。 通过此ID可以调用Flowable API完成任务、查询任务详情等操作。
	 * </p>
	 */
	@TableField("task_id")
	private String taskId;

	/**
	 * 审批意见
	 * <p>
	 * 用户在处理任务时填写的文字意见或备注，如批准理由、拒绝原因等。 这是流程审批的重要记录，用于后续的审计追踪和决策参考。
	 * </p>
	 */
	@TableField("approve_desc")
	private String approveDesc;

	/**
	 * 节点名称
	 * <p>
	 * 流程节点的显示名称，如"部门经理审批"、"财务审核"等。 冗余存储以提高查询效率，避免每次都需要关联查询节点配置。
	 * </p>
	 */
	@TableField("node_name")
	private String nodeName;

	/**
	 * 任务类型
	 * <p>
	 * 标识任务的类型，对应NodeTypeEnum枚举值： 0-发起人节点、1-审批人节点、2-抄送人节点、3-条件节点、4-并行节点等。
	 * 不同类型的任务有不同的处理逻辑和界面展示。
	 * </p>
	 */
	@TableField("task_type")
	private String taskType;

	/**
	 * 表单本地数据
	 * <p>
	 * 存储与该任务相关的本地化数据或临时数据，JSON格式。 可用于保存用户的草稿数据、中间计算结果或其他不需要提交到流程的辅助信息。
	 * </p>
	 */
	@TableField("local_data")
	private String localData;

	/**
	 * 主键ID
	 * <p>
	 * 任务分配记录的唯一标识符，采用雪花算法生成。
	 * </p>
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 逻辑删除标记
	 * <p>
	 * 用于软删除功能，删除时不会真正从数据库中移除记录。 0-正常，1-已删除。保留完整的任务处理记录用于审计。
	 * </p>
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 创建时间
	 * <p>
	 * 记录任务分配的创建时间，通常与startTime相同或接近。 自动填充，用于数据审计。
	 * </p>
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 * <p>
	 * 记录最后更新时间，在任务状态变更、审批意见填写等操作时更新。 用于跟踪任务处理的最新动态。
	 * </p>
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}
