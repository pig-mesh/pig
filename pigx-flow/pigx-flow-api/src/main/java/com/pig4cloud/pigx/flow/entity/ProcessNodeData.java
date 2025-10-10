package com.pig4cloud.pigx.flow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 流程节点数据实体类
 * <p>
 * 用于存储流程节点的表单数据和配置信息。当流程实例运行时，每个节点可能需要填写表单或提交数据，
 * 这些数据通过本实体进行持久化存储。支持JSON格式的灵活数据结构，可以适应不同类型节点的数据需求。
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@TenantTable
@Accessors(chain = true)
@TableName("process_node_data")
public class ProcessNodeData {

	/**
	 * 流程实例ID
	 * <p>
	 * 关联到具体的流程实例，标识这些节点数据属于哪个流程实例。
	 * 对应ProcessInstanceRecord表的flowId字段。
	 * </p>
	 */
	@TableField("flow_id")
	private String flowId;

	/**
	 * 节点表单数据
	 * <p>
	 * 以JSON格式存储的节点表单数据，包含用户在该节点填写的所有表单字段值。
	 * 数据结构根据节点配置的表单项动态变化，支持文本、数字、日期、选择等多种表单类型。
	 * 例如：{"amount": 5000, "reason": "采购办公用品", "approver": "user123"}
	 * </p>
	 */
	@TableField("data")
	private String data;

	/**
	 * 节点ID
	 * <p>
	 * 流程定义中的节点标识符，用于关联到具体的流程节点。
	 * 通过此ID可以确定数据属于流程的哪个环节，如发起节点、审批节点、抄送节点等。
	 * 与Node对象中的id字段对应。
	 * </p>
	 */
	@TableField("node_id")
	private String nodeId;

	/**
	 * 主键ID
	 * <p>
	 * 节点数据记录的唯一标识符，采用雪花算法生成。
	 * </p>
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 逻辑删除标记
	 * <p>
	 * 用于软删除功能，删除时不会真正从数据库中移除记录，而是标记为已删除。
	 * 0-正常，1-已删除。保留历史数据便于审计和数据恢复。
	 * </p>
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 创建时间
	 * <p>
	 * 记录节点数据的创建时间，通常是用户提交表单的时间。
	 * 自动填充，用于数据审计和流程追踪。
	 * </p>
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 * <p>
	 * 记录节点数据的最后更新时间，在创建和更新时自动填充。
	 * 用于跟踪数据的修改历史，如表单数据的补充或修正。
	 * </p>
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}
