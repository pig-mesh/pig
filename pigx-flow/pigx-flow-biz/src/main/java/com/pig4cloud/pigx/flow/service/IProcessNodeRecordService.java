package com.pig4cloud.pigx.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.ProcessNodeRecordParamDto;
import com.pig4cloud.pigx.flow.entity.ProcessNodeRecord;

/**
 * 流程节点执行记录服务接口
 * <p>
 * 该服务负责记录流程执行过程中每个节点的运行状态和执行历史。包括节点的开始时间、
 * 结束时间、执行结果、执行人信息等。这些记录构成了流程的执行轨迹，是流程监控、
 * 审计和问题排查的重要数据来源。服务提供了节点生命周期的管理功能。
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
public interface IProcessNodeRecordService extends IService<ProcessNodeRecord> {

	/**
	 * 记录节点开始执行
	 * <p>
	 * 当流程执行到某个节点时调用此接口，创建节点执行记录。记录节点的基本信息、
	 * 开始时间、节点配置等。同时会根据节点类型（审批、抄送、网关等）执行相应的
	 * 初始化逻辑，如分配执行人、发送通知等。该记录将作为节点执行的主记录。
	 * </p>
	 *
	 * @param processNodeRecordParamDto 节点记录参数，包含：
	 *                                  - processInstanceId: 流程实例ID
	 *                                  - executionId: Flowable执行ID
	 *                                  - nodeId: 节点ID
	 *                                  - nodeName: 节点名称
	 *                                  - nodeType: 节点类型
	 *                                  - parentNodeId: 父节点ID（用于嵌套结构）
	 *                                  - variables: 流程变量
	 * @return R 响应结果，成功返回节点记录ID和初始化状态，失败返回错误信息
	 */
	R start(ProcessNodeRecordParamDto processNodeRecordParamDto);

	/**
	 * 记录节点执行完成
	 * <p>
	 * 当节点执行完成时调用此接口，更新节点记录的结束时间和执行结果。
	 * 对于审批节点，会记录最终的审批结果（通过/拒绝）；对于网关节点，
	 * 会记录选择的分支；对于服务节点，会记录执行状态。完成后会触发
	 * 流程继续向下执行。
	 * </p>
	 *
	 * @param processNodeRecordParamDto 节点记录参数，包含：
	 *                                  - nodeRecordId: 节点记录ID
	 *                                  - status: 执行状态（成功/失败/跳过等）
	 *                                  - result: 执行结果（通过/拒绝/条件值等）
	 *                                  - comment: 处理意见或说明
	 *                                  - endTime: 结束时间
	 *                                  - variables: 更新的流程变量
	 * @return R 响应结果，成功返回更新状态和下一步节点信息，失败返回错误信息
	 */
	R complete(ProcessNodeRecordParamDto processNodeRecordParamDto);

}
