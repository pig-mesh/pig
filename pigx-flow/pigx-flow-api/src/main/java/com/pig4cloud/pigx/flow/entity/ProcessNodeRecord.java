package com.pig4cloud.pigx.flow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 流程节点记录实体类
 * <p>
 * 记录流程实例中每个节点的执行情况。
 * 一个流程实例在执行过程中会产生多个节点记录，用于跟踪和审计流程执行路径。
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@TenantTable
@Accessors(chain = true)
@TableName("process_node_record")
public class ProcessNodeRecord {

	/**
	 * 流程定义ID
	 * 关联process表的flow_id
	 */
	@TableField("flow_id")
	private String flowId;

	/**
	 * 流程实例ID
	 * 关联process_instance_record表，标识该节点记录属于哪个流程实例
	 */
	@TableField("process_instance_id")
	private String processInstanceId;

	/**
	 * 节点执行时的表单数据
	 * JSON格式存储节点执行时的表单数据快照，用于记录和审计
	 */
	@TableField("data")
	private String data;

	/**
	 * 节点ID
	 * 流程定义中的节点唯一标识
	 */
	@TableField("node_id")
	private String nodeId;

	/**
	 * 节点类型
	 * 对应NodeTypeEnum中的值（审批节点、抄送节点、条件分支等）
	 */
	@TableField("node_type")
	private String nodeType;

	/**
	 * 节点名称
	 * 节点的显示名称，如“部门经理审批”、“财务审核”等
	 */
	@TableField("node_name")
	private String nodeName;

	/**
	 * 节点执行状态
	 * 1=进行中，2=已完成，3=已取消，4=已跳过
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 节点开始执行时间
	 * 记录节点开始执行的时间
	 */
	@TableField("start_time")
	private Date startTime;

	/**
	 * 节点结束执行时间
	 * 记录节点完成、取消或跳过的时间
	 */
	@TableField("end_time")
	private Date endTime;

	/**
	 * Flowable引擎执行ID
	 * 对应Flowable引擎中的execution_id，用于与引擎交互
	 */
	@TableField("execution_id")
	private String executionId;

	/**
	 * 主键ID
	 * 使用雪花算法生成的分布式ID
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
