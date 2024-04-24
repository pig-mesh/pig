package com.pig4cloud.pigx.flow.engine.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.TaskParamDto;
import com.pig4cloud.pigx.flow.task.dto.TaskResultDto;
import com.pig4cloud.pigx.flow.task.dto.VariableQueryParamDto;
import com.pig4cloud.pigx.flow.task.utils.NodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 任务控制器
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/task")
public class EngineTaskController {

    private final TaskService taskService;

    private final HistoryService historyService;

    private final RuntimeService runtimeService;

    /**
     * 查询任务变量
     *
     * @param paramDto 参数DTO
     * @return 变量Map
     */
    @PostMapping("queryTaskVariables")
    public R queryTaskVariables(@RequestBody VariableQueryParamDto paramDto) {

        List<String> keyList = paramDto.getKeyList();
        if (CollUtil.isEmpty(keyList)) {
            TaskQuery taskQuery = taskService.createTaskQuery();

            Task task = taskQuery.taskId(paramDto.getTaskId()).singleResult();
            if (task == null) {
                return R.failed("任务不存在");
            }

            Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());

            return R.ok(variables);
        }

        Map<String, Object> variables = taskService.getVariables(paramDto.getTaskId(), keyList);
        return R.ok(variables);

    }

    /**
     * 查询任务详情
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 任务结果DTO
     */
    @GetMapping("/engine/queryTask")
    public R queryTask(String taskId, String userId) {
        Optional<Task> task = Optional
                .ofNullable(taskService.createTaskQuery().taskId(taskId).taskAssignee(userId).singleResult());

        if (task.isPresent()) {
            String processDefinitionId = task.get().getProcessDefinitionId();
            String taskDefinitionKey = task.get().getTaskDefinitionKey();
            DelegationState delegationState = task.get().getDelegationState();
            String processInstanceId = task.get().getProcessInstanceId();
            Object delegateVariable = taskService.getVariableLocal(taskId, "delegate");

            String flowId = NodeUtil.getFlowId(processDefinitionId);
            Map<String, Object> variables = taskService.getVariables(taskId);
            Map<String, Object> variableAll = new HashMap<>(variables);

            TaskResultDto taskResultDto = new TaskResultDto();
            taskResultDto.setFlowId(flowId);
            taskResultDto.setProcessInstanceId(processDefinitionId);
            taskResultDto.setNodeId(taskDefinitionKey);
            taskResultDto.setCurrentTask(true);
            taskResultDto.setDelegate(Convert.toBool(delegateVariable, false));
            taskResultDto.setVariableAll(variableAll);
            taskResultDto.setProcessInstanceId(processInstanceId);
            taskResultDto.setDelegationState(delegationState == null ? null : delegationState.toString());

            return R.ok(taskResultDto);
        } else {
            Optional<HistoricTaskInstance> historicTaskInstance = Optional
                    .ofNullable(historyService.createHistoricTaskInstanceQuery()
                            .taskId(taskId)
                            .taskAssignee(userId)
                            .singleResult());

            if (historicTaskInstance.isPresent()) {
                String processDefinitionId = historicTaskInstance.get().getProcessDefinitionId();
                String taskDefinitionKey = historicTaskInstance.get().getTaskDefinitionKey();
                String processInstanceId = historicTaskInstance.get().getProcessInstanceId();

                String flowId = NodeUtil.getFlowId(processDefinitionId);

                List<HistoricVariableInstance> variableInstanceList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
                Map<String, Object> variableAll = new HashMap<>();

                if (CollUtil.isNotEmpty(variableInstanceList)) {
                    variableInstanceList.forEach(historicVariableInstance -> variableAll.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue()));
                }

                TaskResultDto taskResultDto = new TaskResultDto();
                taskResultDto.setFlowId(flowId);
                taskResultDto.setNodeId(taskDefinitionKey);
                taskResultDto.setCurrentTask(false);
                taskResultDto.setVariableAll(variableAll);
                taskResultDto.setProcessInstanceId(processInstanceId);

                return R.ok(taskResultDto);
            } else {
                return R.failed("任务不存在");
            }
        }
    }

    /**
     * 完成任务
     *
     * @param taskParamDto 任务参数DTO
     * @return 操作结果
     */
    @PostMapping("/complete")
    public R complete(@RequestBody TaskParamDto taskParamDto) {
        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        if (CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
        }

        taskService.complete(taskParamDto.getTaskId(), taskParamDto.getParamMap());

        return R.ok();
    }

}
