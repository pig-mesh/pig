package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.TaskParamDto;
import com.pig4cloud.pigx.flow.service.IFlowableEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Flowable引擎服务实现类
 * <p>
 * 该类提供了对Flowable工作流引擎的高级操作封装，主要包括：
 * 1. 委托任务（delegate）：将任务委托给其他用户处理
 * 2. 解决委托（resolve）：处理完委托任务后返回给原委托人
 * 3. 任务退回（back）：将任务退回到指定节点重新处理
 * 
 * 这些功能扩展了Flowable原生的任务处理能力，为复杂的业务流程提供了更灵活的处理方式
 * </p>
 * 
 * @author pigx code generator
 * @date 2023-10-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowableEngineServiceImpl implements IFlowableEngineService {

    private final TaskService taskService;
    private final RuntimeService runtimeService;

    /**
     * 委托任务
     * <p>
     * 将当前任务委托给其他用户处理。委托后，原任务的处理人变为被委托人，
     * 但原委托人仍保留对任务的控制权，被委托人完成后需要resolve返回给原委托人
     * </p>
     * 
     * @param taskParamDto 任务参数，包含：
     *                     - taskId: 需要委托的任务ID
     *                     - targetUserIdList: 被委托人用户ID列表
     *                     - taskLocalParamMap: 任务本地变量
     * @return R 操作结果，成功返回R.ok()，失败返回R.failed()及错误信息
     */
    @Override
    public R delegateTask(TaskParamDto taskParamDto) {
        try {
            String taskId = taskParamDto.getTaskId();
            Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
            
            // 设置本地变量
            if (CollUtil.isNotEmpty(taskLocalParamMap)) {
                taskService.setVariablesLocal(taskId, taskLocalParamMap);
            }
            
            // 设置委托标记
            taskService.setVariableLocal(taskId, "delegate", true);
            
            // 委托任务给目标用户
            for (Long targetUserId : taskParamDto.getTargetUserIdList()) {
                taskService.delegateTask(taskId, targetUserId.toString());
            }
            
            return R.ok();
        } catch (Exception e) {
            log.error("委托任务失败", e);
            return R.failed("委托任务失败: " + e.getMessage());
        }
    }

    /**
     * 解决委托任务
     * <p>
     * 被委托人完成任务处理后，通过此方法将任务返回给原委托人。
     * 该方法会清除委托状态，恢复任务的正常流转
     * </p>
     * 
     * @param taskParamDto 任务参数，包含：
     *                     - taskId: 需要解决的委托任务ID
     *                     - taskLocalParamMap: 任务本地变量
     *                     - paramMap: 任务完成时的全局变量
     * @return R 操作结果，成功返回R.ok()，失败返回R.failed()及错误信息
     */
    @Override
    public R resolveTask(TaskParamDto taskParamDto) {
        try {
            String taskId = taskParamDto.getTaskId();
            Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
            
            // 设置本地变量
            if (CollUtil.isNotEmpty(taskLocalParamMap)) {
                taskService.setVariablesLocal(taskId, taskLocalParamMap);
            }
            
            // 解决委托任务
            taskService.resolveTask(taskId);
            
            // 完成任务
            if (CollUtil.isNotEmpty(taskParamDto.getParamMap())) {
                taskService.complete(taskId, taskParamDto.getParamMap());
            }
            
            return R.ok();
        } catch (Exception e) {
            log.error("解决委托任务失败", e);
            return R.failed("解决委托任务失败: " + e.getMessage());
        }
    }

    /**
     * 退回任务
     * <p>
     * 将当前任务退回到指定的历史节点重新处理。
     * 该功能常用于审批不通过需要重新修改的场景。
     * 退回操作会保留退回原因和相关信息，便于追溯
     * </p>
     * 
     * @param taskParamDto 任务参数，包含：
     *                     - taskId: 需要退回的任务ID
     *                     - targetNodeId: 目标节点ID（退回到的节点）
     *                     - paramMap: 包含退回原因等参数
     * @return R 操作结果，成功返回R.ok()，失败返回R.failed()及错误信息
     */
    @Override
    public R back(TaskParamDto taskParamDto) {
        try {
            String taskId = taskParamDto.getTaskId();
            String targetNodeId = taskParamDto.getTargetNodeId();
            
            // 获取当前任务
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                return R.failed("任务不存在");
            }
            
            // 设置退回标记
            Map<String, Object> variables = taskParamDto.getParamMap();
            variables.put("isBack", true);
            // 退回原因可以从paramMap中获取
            if (variables.containsKey("comment")) {
                variables.put("backReason", variables.get("comment"));
            }
            
            // 使用流程跳转实现退回
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdTo(task.getTaskDefinitionKey(), targetNodeId)
                    .changeState();
            
            return R.ok();
        } catch (Exception e) {
            log.error("退回任务失败", e);
            return R.failed("退回任务失败: " + e.getMessage());
        }
    }
}