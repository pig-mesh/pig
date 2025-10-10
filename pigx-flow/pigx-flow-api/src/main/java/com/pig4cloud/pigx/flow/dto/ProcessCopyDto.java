package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程抄送数据传输对象
 * <p>
 * 该DTO用于创建流程抄送记录。当流程执行到某个节点时，
 * 可以将该节点的信息抄送给相关人员，抄送人员可以查看但不需要审批。
 * 常用于让相关人员了解流程进展，保持信息透明。
 * </p>
 * 
 * @author pigx
 */
@Data
public class ProcessCopyDto {

	/**
	 * 抄送时间
	 * <p>
	 * 记录抄送动作发生的时间，即当前节点的执行时间。
	 * 用于追溯抄送记录的时间线。
	 * </p>
	 */
	private LocalDateTime nodeTime;

	/**
	 * 流程发起人ID
	 * <p>
	 * 发起该流程实例的用户ID。
	 * 用于标识流程的发起者，便于抄送人了解流程来源。
	 * </p>
	 */
	private Long startUserId;

	/**
	 * 流程定义ID
	 * <p>
	 * 流程定义的唯一标识，标识这是哪个流程模板。
	 * 如：leave_process（请假流程）、purchase_process（采购流程）等。
	 * </p>
	 */
	private String flowId;

	/**
	 * 流程实例ID
	 * <p>
	 * 当前运行的流程实例的唯一标识。
	 * 用于关联到具体的流程实例，便于查看详情。
	 * </p>
	 */
	private String processInstanceId;

	/**
	 * 节点ID
	 * <p>
	 * 触发抄送的流程节点ID。
	 * 标识是在哪个节点产生的抄送记录。
	 * </p>
	 */
	private String nodeId;

	/**
	 * 节点名称
	 * <p>
	 * 触发抄送的流程节点名称，如"部门经理审批"、"财务审核"等。
	 * 用于在界面上显示，让抄送人了解流程进展到哪一步。
	 * </p>
	 */
	private String nodeName;

	/**
	 * 表单数据
	 * <p>
	 * JSON格式的表单数据，包含流程实例的业务数据。
	 * 如请假流程的请假事由、天数等信息，便于抄送人查看具体内容。
	 * </p>
	 */
	private String formData;

	/**
	 * 抄送接收人ID
	 * <p>
	 * 接收抄送通知的用户ID。
	 * 该用户将在抄送列表中看到此流程的信息。
	 * </p>
	 */
	private Long userId;

}
