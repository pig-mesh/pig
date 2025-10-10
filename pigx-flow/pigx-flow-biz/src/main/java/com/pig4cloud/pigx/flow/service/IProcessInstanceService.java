package com.pig4cloud.pigx.flow.service;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.ProcessInstanceParamDto;
import com.pig4cloud.pigx.flow.dto.TaskQueryParamDto;
import com.pig4cloud.pigx.flow.vo.NodeFormatParamVo;

/**
 * 流程实例管理服务接口
 * <p>
 * 该服务提供流程实例的完整生命周期管理功能，包括流程的启动、查询、结束等操作。
 * 同时提供了多维度的任务查询功能，支持查询待办任务、已办任务、我发起的流程、
 * 抄送给我的流程等。还提供了流程节点的格式化显示和流程详情查询等辅助功能。
 * </p>
 *
 * @author pig code generator
 * @date 2023-07-11
 */
public interface IProcessInstanceService {

	/**
	 * 启动新的流程实例
	 * <p>
	 * 根据流程定义启动一个新的流程实例。启动时需要提供流程定义ID、业务关联ID、
	 * 表单数据和流程变量等信息。系统会自动创建第一个任务节点，并根据配置分配
	 * 给相应的处理人。启动成功后会返回流程实例ID供后续跟踪使用。
	 * </p>
	 *
	 * @param processInstanceParamDto 流程实例参数对象，包含：
	 *                                - processDefinitionId: 流程定义ID
	 *                                - businessKey: 业务关联键
	 *                                - formData: 表单数据（JSON格式）
	 *                                - variables: 流程变量
	 *                                - startUserId: 发起人ID（可选，默认当前用户）
	 * @return R 响应结果，成功返回流程实例ID和首个任务信息，失败返回错误信息
	 */
	R startProcessInstance(ProcessInstanceParamDto processInstanceParamDto);

	/**
	 * 查询当前用户的待办任务
	 * <p>
	 * 查询分配给当前登录用户的所有待处理任务。支持分页查询和多条件筛选，
	 * 包括流程类型、紧急程度、创建时间等。返回的任务列表包含任务的基本信息、
	 * 所属流程信息、表单数据预览等，便于用户快速了解和处理任务。
	 * </p>
	 *
	 * @param taskQueryParamDto 任务查询参数，支持：
	 *                          - pageNum: 页码
	 *                          - pageSize: 每页大小
	 *                          - processDefinitionKey: 流程定义标识（可选）
	 *                          - priority: 优先级（可选）
	 *                          - createTimeBegin/End: 创建时间范围（可选）
	 * @return R 响应结果，包含分页的待办任务列表，每个任务包含任务ID、名称、创建时间、流程信息等
	 */
	R queryMineTask(TaskQueryParamDto taskQueryParamDto);

	/**
	 * 查询当前用户的已办任务
	 * <p>
	 * 查询当前用户已经处理完成的历史任务。包括已审批、已转办、已委托完成的任务。
	 * 支持按时间范围、流程类型等条件筛选，帮助用户追溯历史处理记录。
	 * </p>
	 *
	 * @param taskQueryParamDto 任务查询参数，支持：
	 *                          - pageNum: 页码
	 *                          - pageSize: 每页大小
	 *                          - processDefinitionKey: 流程定义标识（可选）
	 *                          - completeTimeBegin/End: 完成时间范围（可选）
	 * @return R 响应结果，包含分页的已办任务列表，包含任务处理时间、处理意见、处理结果等信息
	 */
	R queryMineEndTask(TaskQueryParamDto taskQueryParamDto);

	/**
	 * 强制结束流程实例
	 * <p>
	 * 管理员或流程发起人可以强制结束一个运行中的流程实例。结束时需要提供
	 * 结束原因，系统会记录操作日志。该操作不可逆，结束后的流程无法恢复。
	 * 通常用于流程异常或业务取消等场景。
	 * </p>
	 *
	 * @param processInstanceParamDto 参数对象，必须包含：
	 *                                - processInstanceId: 要结束的流程实例ID
	 *                                - reason: 结束原因
	 * @return R 响应结果，成功返回结束确认信息，失败返回错误原因（如权限不足等）
	 */
	R end(ProcessInstanceParamDto processInstanceParamDto);

	/**
	 * 查询我发起的流程
	 * <p>
	 * 查询当前用户发起的所有流程实例，包括运行中和已结束的流程。
	 * 支持按流程状态、发起时间等条件筛选。用户可以查看流程的当前进度、
	 * 处理人等信息，并可以对自己发起的流程进行撤回、催办等操作。
	 * </p>
	 *
	 * @param taskQueryParamDto 查询参数，支持：
	 *                          - pageNum: 页码
	 *                          - pageSize: 每页大小
	 *                          - processStatus: 流程状态（运行中/已结束）
	 *                          - startTimeBegin/End: 发起时间范围
	 * @return R 响应结果，包含我发起的流程列表，含流程状态、当前节点、处理人等信息
	 */
	R queryMineStarted(TaskQueryParamDto taskQueryParamDto);

	/**
	 * 查询抄送给我的流程
	 * <p>
	 * 查询所有抄送给当前用户的流程信息。抄送是流程中的知会功能，
	 * 被抄送人可以查看流程详情但不参与审批。支持标记已读状态，
	 * 便于用户管理和跟踪需要关注的流程。
	 * </p>
	 *
	 * @param taskQueryParamDto 查询参数，支持：
	 *                          - pageNum: 页码
	 *                          - pageSize: 每页大小
	 *                          - readStatus: 已读/未读状态
	 *                          - copyTimeBegin/End: 抄送时间范围
	 * @return R 响应结果，包含抄送流程列表，含抄送时间、抄送人、流程状态、已读标记等
	 */
	R queryMineCC(TaskQueryParamDto taskQueryParamDto);

	/**
	 * 格式化流程节点显示数据
	 * <p>
	 * 将流程定义的节点数据格式化为前端可展示的结构。包括节点的层级关系、
	 * 条件分支、并行网关等复杂结构的可视化数据。该接口主要用于流程图的
	 * 展示和流程设计器的数据渲染。
	 * </p>
	 *
	 * @param nodeFormatParamVo 节点格式化参数，包含：
	 *                          - processDefinitionId: 流程定义ID
	 *                          - nodeData: 原始节点数据
	 *                          - showType: 显示类型（设计/运行/历史）
	 * @return R 响应结果，包含格式化后的节点树形结构，支持前端流程图渲染
	 */
	R formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo);

	/**
	 * 查询流程实例详情
	 * <p>
	 * 获取指定流程实例的完整信息，包括流程基本信息、表单数据、审批历史、
	 * 流程图当前状态等。该接口会整合多个数据源，提供流程的全景视图，
	 * 支持流程追踪和问题排查。
	 * </p>
	 *
	 * @param processInstanceId 流程实例ID
	 * @return R 响应结果，包含：
	 *         - 流程基本信息（名称、发起人、发起时间、当前状态等）
	 *         - 表单数据（完整的业务表单数据）
	 *         - 审批历史（所有节点的处理记录）
	 *         - 流程图（带当前节点高亮的流程图数据）
	 *         - 操作权限（当前用户对该流程的可操作项）
	 */
	R detail(String processInstanceId);

}
