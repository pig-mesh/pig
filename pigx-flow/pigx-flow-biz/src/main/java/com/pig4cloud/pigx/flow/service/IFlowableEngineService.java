package com.pig4cloud.pigx.flow.service;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.TaskParamDto;

/**
 * Flowable引擎核心服务接口
 * <p>
 * 该服务提供了对Flowable工作流引擎的核心操作封装，主要处理任务的委托、解决和退回等高级功能。
 * 这些功能是对标准Flowable API的扩展，为业务层提供更便捷的工作流操作能力。
 * 所有操作都基于Flowable引擎的任务服务（TaskService）和运行时服务（RuntimeService）实现。
 * </p>
 *
 * @author pig code generator
 * @date 2023-07-11
 */
public interface IFlowableEngineService {

    /**
     * 委托任务给其他用户处理
     * <p>
     * 将当前任务委托给指定的用户处理。委托后，原任务处理人仍保留任务的所有权，
     * 被委托人完成任务后，任务会返回给原处理人进行确认。这种机制常用于任务处理人
     * 临时无法处理任务，需要他人协助的场景。委托操作会记录在任务历史中。
     * </p>
     *
     * @param taskParamDto 任务参数对象，必须包含：
     *                     - taskId: 要委托的任务ID
     *                     - userId: 被委托人的用户ID
     *                     - comment: 委托说明（可选）
     *                     - variables: 流程变量（可选）
     * @return R 响应结果，成功返回委托后的任务信息，失败返回错误信息
     */
    R delegateTask(TaskParamDto taskParamDto);

    /**
     * 解决已委托的任务
     * <p>
     * 被委托人完成委托任务的处理。该操作将任务标记为已解决，并将任务控制权
     * 返回给原任务处理人。原处理人可以查看被委托人的处理结果，并决定是否
     * 继续推进流程。解决任务时可以添加处理意见和更新流程变量。
     * </p>
     *
     * @param taskParamDto 任务参数对象，必须包含：
     *                     - taskId: 要解决的委托任务ID
     *                     - comment: 处理意见
     *                     - variables: 更新的流程变量（可选）
     * @return R 响应结果，成功返回解决后的任务状态，失败返回错误信息
     */
    R resolveTask(TaskParamDto taskParamDto);

    /**
     * 退回任务到上一节点
     * <p>
     * 将当前任务退回到流程的上一个处理节点。该功能用于审批不通过或需要
     * 上一步骤重新处理的场景。退回操作会保留完整的流程历史记录，并且
     * 可以配置是否清空后续节点的处理记录。支持跨多个节点的退回操作。
     * </p>
     *
     * @param taskParamDto 任务参数对象，必须包含：
     *                     - taskId: 要退回的任务ID
     *                     - targetNodeId: 目标节点ID（可选，不指定则退回到上一节点）
     *                     - comment: 退回原因
     *                     - variables: 更新的流程变量（可选）
     * @return R 响应结果，成功返回退回后的任务信息，失败返回错误信息（如无法退回的情况）
     */
    R back(TaskParamDto taskParamDto);
}