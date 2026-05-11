package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.IndexPageStatistics;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.TaskParamDto;
import com.pig4cloud.pigx.flow.dto.TaskResultDto;
import com.pig4cloud.pigx.flow.entity.*;
import com.pig4cloud.pigx.flow.entity.Process;
import com.pig4cloud.pigx.flow.service.*;
import com.pig4cloud.pigx.flow.support.utils.NodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 任务服务实现类
 * <p>
 * 该类是流程任务操作的核心服务，负责处理用户与流程任务的交互，主要功能包括： 1. 任务查询（待办任务详情、任务表单权限等） 2. 任务操作（完成、委托、转办、退回等） 3.
 * 任务统计（首页数据看板） 4. 流程控制（终止流程、加签等高级操作）
 * <p>
 * 该服务整合了流程引擎的任务API，为前端提供统一的任务操作接口
 * </p>
 *
 * @author pigx code generator
 * @date 2023-10-01
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {

    private final IProcessService processService;

    private final IProcessCopyService processCopyService;

    private final IProcessNodeDataService nodeDataService;

    private final IProcessNodeRecordAssignUserService processNodeRecordAssignUserService;

    private final IProcessInstanceRecordService processInstanceRecordService;

    private final IProcessNodeRecordService processNodeRecordService;

    private final IFlowableEngineService flowableEngineService;

    private final TaskService taskService;

    private final HistoryService historyService;

    private final RuntimeService runtimeService;

    private final ObjectMapper objectMapper;

    /**
     * 查询首页数据看板
     * <p>
     * 获取当前用户的任务统计数据，包括： 1. 待办任务数量 2. 已办任务数量 3. 我发起的流程数量 4. 抄送给我的数量
     * </p>
     *
     * @return R 包含IndexPageStatistics对象，含有各项统计数据
     */
    @Override
    public R queryTaskData() {

        TaskQuery taskQuery = taskService.createTaskQuery();

        // 待办数量
        long pendingNum = taskQuery.taskAssignee(String.valueOf(SecurityUtils.getUser().getId()))
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .count();
        // 已完成任务
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService
                .createHistoricActivityInstanceQuery();

        long completedNum = historicActivityInstanceQuery.taskAssignee(String.valueOf(SecurityUtils.getUser().getId()))
                .tenantIdIn(List.of(TenantContextHolder.getTenantId().toString()))
                .finished()
                .count();

        IndexPageStatistics indexPageStatistics = IndexPageStatistics.builder()
                .pendingNum(pendingNum)
                .completedNum(completedNum)
                .build();

        // 获取抄送任务
        Long copyCount = processCopyService.lambdaQuery()
                .eq(ProcessCopy::getUserId, SecurityUtils.getUser().getId())
                .count();
        indexPageStatistics.setCopyNum(copyCount);
        return R.ok(indexPageStatistics);
    }

    /**
     * 查询任务
     * <p>
     * 查询指定任务的详细信息，包括： 1. 任务的基本信息（节点名称、执行人等） 2. 流程表单定义和表单数据 3. 当前节点的表单权限配置 4.
     * 任务是否可委托、是否是委托任务等状态
     * <p>
     * 对于已完成的任务，会从历史记录中获取任务数据
     * </p>
     *
     * @param taskId 任务ID
     * @param view   是否是查看模式（true:只查看，false:可操作）
     * @return R 包含任务详情的Dict对象
     * @throws Exception 当解析JSON数据失败时抛出异常
     */
    @SneakyThrows
    @Override
    public R queryTask(String taskId, boolean view) {
        // 默认使用主表单数据源
        return queryTask(taskId, view, true);
    }

    /**
     * 查询任务（支持选择表单数据源）
     * <p>
     * 查询指定任务的详细信息，包括： 1. 任务的基本信息（节点名称、执行人等） 2. 流程表单定义和表单数据 3. 当前节点的表单权限配置 4.
     * 任务是否可委托、是否是委托任务等状态
     * <p>
     * 对于已完成的任务，可选择从主表单数据或节点快照读取表单数据
     * </p>
     *
     * @param taskId          任务ID
     * @param view            是否是查看模式（true:只查看，false:可操作）
     * @param useMainFormData 是否使用主表单数据（true=从process_instance_record.form_data读取最新数据，false=从节点快照读取）
     * @return R 包含任务详情的Dict对象
     * @throws Exception 当解析JSON数据失败时抛出异常
     */
    @SneakyThrows
    @Override
    public R queryTask(String taskId, boolean view, boolean useMainFormData) {

        long userId = SecurityUtils.getUser().getId();

        Optional<Task> task = Optional.ofNullable(taskService.createTaskQuery()
                .taskId(taskId)
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .taskAssignee(String.valueOf(userId))
                .singleResult());
        TaskResultDto taskResultDto = new TaskResultDto();

        if (task.isPresent()) {
            String processDefinitionId = task.get().getProcessDefinitionId();
            String taskDefinitionKey = task.get().getTaskDefinitionKey();
            DelegationState delegationState = task.get().getDelegationState();
            String processInstanceId = task.get().getProcessInstanceId();
            Object delegateVariable = taskService.getVariableLocal(taskId, "delegate");

            String flowId = NodeUtil.getFlowId(processDefinitionId);
            Map<String, Object> variables = taskService.getVariables(taskId);
            Map<String, Object> variableAll = new HashMap<>(variables);

            taskResultDto.setFlowId(flowId);
            taskResultDto.setNodeId(taskDefinitionKey);
            taskResultDto.setCurrentTask(true);
            taskResultDto.setDelegate(Convert.toBool(delegateVariable, false));
            taskResultDto.setVariableAll(variableAll);
            taskResultDto.setProcessInstanceId(processInstanceId);
            taskResultDto.setDelegationState(delegationState == null ? null : delegationState.toString());
        } else {
            Optional<HistoricTaskInstance> historicTaskInstance = Optional
                    .ofNullable(historyService.createHistoricTaskInstanceQuery()
                            .taskId(taskId)
                            .taskAssignee(String.valueOf(userId))
                            .singleResult());

            String processDefinitionId = historicTaskInstance.get().getProcessDefinitionId();
            String taskDefinitionKey = historicTaskInstance.get().getTaskDefinitionKey();
            String processInstanceId = historicTaskInstance.get().getProcessInstanceId();

            String flowId = NodeUtil.getFlowId(processDefinitionId);

            List<HistoricVariableInstance> variableInstanceList = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .list();
            Map<String, Object> variableAll = new HashMap<>();

            if (CollUtil.isNotEmpty(variableInstanceList)) {
                variableInstanceList.forEach(historicVariableInstance -> variableAll
                        .put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue()));
            }

            taskResultDto.setFlowId(flowId);
            taskResultDto.setNodeId(taskDefinitionKey);
            taskResultDto.setCurrentTask(false);
            taskResultDto.setVariableAll(variableAll);
            taskResultDto.setProcessInstanceId(processInstanceId);

        }

        // 变量
        Map<String, Object> paramMap = taskResultDto.getVariableAll();
        // 是否是当前活动任务
        Boolean currentTask = taskResultDto.getCurrentTask();
        if (!currentTask) {
            // 判断是否使用主表单数据源
            boolean shouldUseMainFormData = view && useMainFormData;

            if (shouldUseMainFormData) {
                // 新增: 从主流程实例记录读取最新表单数据
                String processInstanceId = taskResultDto.getProcessInstanceId();
                ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                        .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                        .one();

                if (processInstanceRecord != null && StrUtil.isNotBlank(processInstanceRecord.getFormData())) {
                    try {
                        Map<String, Object> mainFormData = objectMapper.readValue(processInstanceRecord.getFormData(),
                                new TypeReference<Map<String, Object>>() {
                                });
                        paramMap.putAll(mainFormData);
                        log.debug("Using main form data for completed task {}", taskId);
                    } catch (Exception e) {
                        log.warn("Failed to parse main form data, falling back to node snapshot: {}", e.getMessage());
                        shouldUseMainFormData = false;
                    }
                } else {
                    log.debug("Main form data empty, falling back to node snapshot for task {}", taskId);
                    shouldUseMainFormData = false;
                }
            }

            // 回退: 使用节点级别快照（原有逻辑）
            if (!shouldUseMainFormData) {
                ProcessNodeRecordAssignUser processNodeRecordAssignUser = processNodeRecordAssignUserService.lambdaQuery()
                        .eq(ProcessNodeRecordAssignUser::getTaskId, taskId)
                        .eq(ProcessNodeRecordAssignUser::getUserId, userId)
                        .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.YJS.getCode())
                        .last("limit 1")
                        .orderByDesc(ProcessNodeRecordAssignUser::getEndTime)
                        .one();

                if (processNodeRecordAssignUser != null) {
                    String data = processNodeRecordAssignUser.getData();
                    if (StrUtil.isNotBlank(data)) {
                        Map<String, Object> collect = objectMapper.readValue(data, new TypeReference<>() {
                        });
                        paramMap.putAll(collect);
                        log.debug("Using node-level snapshot for task {}", taskId);
                    }
                }
            }

        }

        // 当前节点数据
        Node node = nodeDataService.getNodeData(taskResultDto.getFlowId(), taskResultDto.getNodeId()).getData();
        Map<String, String> formPerms = node.getFormPerms();

        Process oaForms = processService.getByFlowId(taskResultDto.getFlowId());
        if (oaForms == null) {
            return R.failed("流程不存在");
        }

        Dict set = Dict.create()
                .set("taskId", taskId)
                .set("nodeId", taskResultDto.getNodeId())
                .set("processInstanceId", taskResultDto.getProcessInstanceId())
                .set("node", node)
                .set("process", oaForms.getProcess())
                .set("delegateAgain", taskResultDto.getDelegate())
                .set("delegationTask", StrUtil.equals(taskResultDto.getDelegationState(), "PENDING"))
                .set("formItems", oaForms.getFormItems())
                .set("formData", paramMap)
                .set("formConfig", oaForms.getFormConfig())
                .set("formPerms", formPerms);

        return R.ok(set);
    }

    /**
     * 完成任务
     * <p>
     * 执行任务的完成操作，将任务标记为已完成并流转到下一个节点。 该方法会将当前用户ID设置到任务参数中，确保任务由正确的用户完成
     * </p>
     *
     * @param taskParamDto 任务参数，包含任务ID、审批意见、流程变量等
     * @return R 操作结果
     */
    @Override
    public R completeTask(TaskParamDto taskParamDto) {
        long userId = SecurityUtils.getUser().getId();
        taskParamDto.setUserId(String.valueOf(userId));

        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        if (CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
        }

        // 始终获取 processInstanceId，确保触发器终止时可用
        Task task = taskService.createTaskQuery().taskId(taskParamDto.getTaskId()).singleResult();
        taskParamDto.setProcessInstanceId(task.getProcessInstanceId());

        if (CollUtil.isNotEmpty(taskParamDto.getFormData())) {
            runtimeService.setVariables(task.getExecutionId(), taskParamDto.getFormData());

            // 同步更新流程实例记录的主表单数据
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                    .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                    .one();

            if (processInstanceRecord != null) {
                try {
                    // 合并现有表单数据和新提交的数据
                    Map<String, Object> existingFormData = new HashMap<>();
                    if (StrUtil.isNotBlank(processInstanceRecord.getFormData())) {
                        existingFormData = objectMapper.readValue(processInstanceRecord.getFormData(),
                                new TypeReference<Map<String, Object>>() {
                                });
                    }

                    // 用新数据覆盖现有数据
                    existingFormData.putAll(taskParamDto.getFormData());

                    // 更新到数据库
                    String updatedFormData = objectMapper.writeValueAsString(existingFormData);
                    processInstanceRecordService.lambdaUpdate()
                            .set(ProcessInstanceRecord::getFormData, updatedFormData)
                            .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                            .update();

                    log.debug("流程实例 {} 的表单数据已同步更新", processInstanceId);
                } catch (Exception e) {
                    log.error("同步更新流程实例表单数据失败: {}", e.getMessage(), e);
                    // 不中断流程执行，仅记录错误
                }
            }
        }

        try {
            taskService.complete(taskParamDto.getTaskId(), taskParamDto.getParamMap());
        } catch (FlowableException e) {
            String message = e.getMessage();
            if (StrUtil.isNotBlank(message) && message.startsWith("TRIGGER_TERMINATE:")) {
                return handleTriggerTerminate(message, taskParamDto.getProcessInstanceId());
            }
            throw e;
        }

        return R.ok();
    }

    /**
     * 处理触发器异常终止
     * <p>
     * 在 Flowable 事务回滚后执行，此时处于新的事务上下文中，
     * 可安全地删除流程实例和更新 ProcessInstanceRecord 状态。
     * </p>
     *
     * @param exceptionMessage  格式: TRIGGER_TERMINATE:{errorMessage}
     * @param processInstanceId 流程实例ID
     * @return R 失败结果
     */
    private R handleTriggerTerminate(String exceptionMessage, String processInstanceId) {
        String errorMsg = StrUtil.removePrefix(exceptionMessage, "TRIGGER_TERMINATE:");

        // 在 Flowable 事务外删除流程实例（新的命令上下文 = 新事务）
        try {
            runtimeService.deleteProcessInstance(processInstanceId, "触发器异常终止: " + errorMsg);
        } catch (Exception ex) {
            log.error("触发器终止-删除流程实例失败: processInstanceId={}, error={}", processInstanceId, ex.getMessage());
        }

        // 更新 ProcessInstanceRecord 状态为已结束（与 stopProcessInstance 保持一致）
        processInstanceRecordService.lambdaUpdate()
                .set(ProcessInstanceRecord::getStatus, NodeStatusEnum.YJS.getCode())
                .set(ProcessInstanceRecord::getEndTime, new Date())
                .set(ProcessInstanceRecord::getFinishReason, String.valueOf(NodeStatusEnum.YZZ.getCode()))
                .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                .update();

        return R.failed("触发器调用失败: " + errorMsg);
    }

    /**
     * 前加签
     * <p>
     * 将任务委托给其他用户处理，委托后任务会分配给被委托人， 被委托人完成后任务会返回给原委托人
     * </p>
     *
     * @param taskParamDto 任务参数，包含任务ID、被委托人ID列表等
     * @return R 操作结果
     */
    @Transactional
    @Override
    public R delegateTask(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(SecurityUtils.getUser().getId().toString());
        return flowableEngineService.delegateTask(taskParamDto);
    }

    /**
     * 加签完成任务
     * <p>
     * 被委托人完成委托任务后，通过此方法将任务返回给原委托人。 这是委托流程的第二步，用于结束委托状态
     * </p>
     *
     * @param taskParamDto 任务参数，包含任务ID、审批意见等
     * @return R 操作结果
     */
    @Override
    public R resolveTask(TaskParamDto taskParamDto) {
        return flowableEngineService.resolveTask(taskParamDto);
    }

    /**
     * 设置执行人
     * <p>
     * 转办任务给其他用户，与委托不同，转办后任务直接由新执行人处理， 不会返回给原执行人
     * </p>
     *
     * @param taskParamDto 任务参数，包含任务ID、新执行人ID等
     * @return R 操作结果
     */
    @Override
    public R setAssignee(TaskParamDto taskParamDto) {
        Map<String, Object> taskLocalParamMap = taskParamDto.getTaskLocalParamMap();
        if (CollUtil.isNotEmpty(taskLocalParamMap)) {
            taskService.setVariablesLocal(taskParamDto.getTaskId(), taskLocalParamMap);
        }

        // 设置任务的 owner 为当前任务的 assignee
        Task task = taskService.createTaskQuery()
                .taskId(taskParamDto.getTaskId())
                .includeTaskLocalVariables()
                .singleResult();
        taskService.setOwner(taskParamDto.getTaskId(), task.getAssignee());

        // 设置任务的 assignee 为目标用户 (转办用户)
        for (Long targetUserId : taskParamDto.getTargetUserIdList()) {
            taskService.setAssignee(taskParamDto.getTaskId(), targetUserId.toString());
        }

        // 更新表单
        if (CollUtil.isNotEmpty(taskParamDto.getFormData())) {
            runtimeService.setVariables(task.getExecutionId(), taskParamDto.getFormData());
        }
        return R.ok();
    }

    /**
     * 结束流程
     * <p>
     * 强制终止流程实例的执行。该方法会： 1. 递归查找并终止所有子流程实例 2. 更新流程实例记录状态为已结束 3.
     * 设置结束原因为强制终止（finishReason=9）
     * <p>
     * 通常用于流程异常或需要强制结束的场景
     * </p>
     *
     * @param taskParamDto 任务参数，包含流程实例ID
     * @return R 操作结果
     */
    @Override
    public R stopProcessInstance(TaskParamDto taskParamDto) {
        return terminateProcessInstance(taskParamDto.getProcessInstanceId(),
                ProcessInstanceConstant.FINISH_REASON_TERMINATE);
    }

    /**
     * 撤回流程
     *
     * @param taskParamDto 任务参数，包含流程实例ID
     * @return R 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R withdrawProcessInstance(TaskParamDto taskParamDto) {
        String processInstanceId = taskParamDto.getProcessInstanceId();
        if (StrUtil.isBlank(processInstanceId)) {
            return R.failed("流程实例ID不能为空");
        }

        long userId = SecurityUtils.getUser().getId();
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                .one();
        if (processInstanceRecord == null
                || !Objects.equals(processInstanceRecord.getStatus(), NodeStatusEnum.JXZ.getCode())) {
            return R.failed("流程不存在或已结束");
        }

        if (!Objects.equals(processInstanceRecord.getUserId(), userId)) {
            return R.failed("仅发起人可撤回流程");
        }

        try {
            Object rejectToStarter = runtimeService.getVariable(processInstanceId,
                    ProcessInstanceConstant.REJECT_TO_STARTER_VAR);
            if (Boolean.TRUE.equals(rejectToStarter)) {
                return R.failed("驳回待提交状态不可撤回");
            }
        } catch (Exception e) {
            log.warn("查询撤回状态变量失败: processInstanceId={}, error={}", processInstanceId, e.getMessage());
        }

        boolean hasProcessedApproval = processNodeRecordAssignUserService.lambdaQuery()
                .eq(ProcessNodeRecordAssignUser::getProcessInstanceId, processInstanceId)
                .ne(ProcessNodeRecordAssignUser::getNodeId, "root")
                .and(wrapper -> wrapper
                        .isNotNull(ProcessNodeRecordAssignUser::getEndTime)
                        .or()
                        .ne(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.JXZ.getCode()))
                .exists();
        if (hasProcessedApproval) {
            return R.failed("首个审批节点已处理，无法撤回");
        }

        return terminateProcessInstance(processInstanceId, ProcessInstanceConstant.FINISH_REASON_WITHDRAW);
    }

    /**
     * 退回
     * <p>
     * 将任务退回到指定的历史节点重新处理。 常用于审批不通过需要重新修改的场景
     * </p>
     *
     * @param taskParamDto 任务参数，包含任务ID、目标节点ID、退回原因等
     * @return R 操作结果
     */
    @Override
    public R back(TaskParamDto taskParamDto) {
        taskParamDto.setUserId(SecurityUtils.getUser().getId().toString());
        return flowableEngineService.back(taskParamDto);
    }

    /**
     * 重新提交任务（驳回到发起人后）
     * <p>
     * 当流程被驳回到发起人时，发起人使用此方法重新编辑表单并提交。
     * 该方法会：
     * 1. 验证任务存在且属于当前用户
     * 2. 验证任务是 root 节点任务
     * 3. 更新表单数据
     * 4. 清理 rejectToStarter 流程变量
     * 5. 完成任务，流程继续到下一个审批节点
     * </p>
     *
     * @param taskParamDto 任务参数，包含taskId和更新后的formData
     * @return R 操作结果
     */
    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public R resubmitTask(TaskParamDto taskParamDto) {
        long userId = SecurityUtils.getUser().getId();

        // 1. 获取任务并验证
        Task task = taskService.createTaskQuery()
                .taskId(taskParamDto.getTaskId())
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .taskAssignee(String.valueOf(userId))
                .singleResult();

        if (task == null) {
            return R.failed("任务不存在或无权限操作");
        }

        // 2. 验证是 root 任务（发起人节点）
        if (!StrUtil.equals(task.getTaskDefinitionKey(), "root")) {
            return R.failed("非重新提交任务，无法操作");
        }

        String processInstanceId = task.getProcessInstanceId();

        // 3. 更新表单数据
        if (CollUtil.isNotEmpty(taskParamDto.getFormData())) {
            runtimeService.setVariables(task.getExecutionId(), taskParamDto.getFormData());

            // 同步更新到 ProcessInstanceRecord
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                    .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                    .one();

            if (processInstanceRecord != null) {
                Map<String, Object> existingFormData = new HashMap<>();
                if (StrUtil.isNotBlank(processInstanceRecord.getFormData())) {
                    existingFormData = objectMapper.readValue(processInstanceRecord.getFormData(),
                            new TypeReference<Map<String, Object>>() {
                            });
                }
                existingFormData.putAll(taskParamDto.getFormData());
                String updatedFormData = objectMapper.writeValueAsString(existingFormData);

                processInstanceRecordService.lambdaUpdate()
                        .set(ProcessInstanceRecord::getFormData, updatedFormData)
                        .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                        .update();
            }
        }

        // 4. 清理 rejectToStarter 变量
        runtimeService.removeVariable(processInstanceId, "rejectToStarter");

        // 5. 完成任务，流程继续
        taskService.complete(taskParamDto.getTaskId());

        return R.ok("重新提交成功");
    }

    /**
     * 递归获取所有需要停止的子流程实例ID列表
     * <p>
     * 当一个流程包含子流程时，终止主流程需要同时终止所有子流程。 该方法递归查找指定流程的所有子流程实例ID
     * </p>
     *
     * @param processInstanceId 父流程实例ID
     * @return 所有子流程实例ID列表
     */
    private List<String> getAllStopProcessInstanceIdList(String processInstanceId) {
        List<ProcessInstanceRecord> list = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getParentProcessInstanceId, processInstanceId)
                .list();

        List<String> collect = new ArrayList<>(list.stream().map(ProcessInstanceRecord::getProcessInstanceId).toList());

        for (ProcessInstanceRecord processInstanceRecord : list) {
            List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(
                    processInstanceRecord.getProcessInstanceId());

            collect.addAll(allStopProcessInstanceIdList);

        }
        return collect;
    }

    private R terminateProcessInstance(String reqProcessInstanceId, String finishReason) {
        if (StrUtil.isBlank(reqProcessInstanceId)) {
            return R.failed("流程实例ID不能为空");
        }

        List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(reqProcessInstanceId);
        CollUtil.reverse(allStopProcessInstanceIdList);
        allStopProcessInstanceIdList.add(reqProcessInstanceId);

        Date now = new Date();
        int assignUserStatus = Objects.equals(finishReason, ProcessInstanceConstant.FINISH_REASON_WITHDRAW)
                ? ProcessInstanceConstant.ASSIGN_USER_STATUS_WITHDRAW
                : ProcessInstanceConstant.ASSIGN_USER_STATUS_TERMINATE;

        // 先更新本地记录，确保流程结束通知能拿到准确的 finishReason
        processInstanceRecordService.lambdaUpdate()
                .set(ProcessInstanceRecord::getStatus, NodeStatusEnum.YJS.getCode())
                .set(ProcessInstanceRecord::getFinishReason, finishReason)
                .set(ProcessInstanceRecord::getEndTime, now)
                .in(ProcessInstanceRecord::getProcessInstanceId, allStopProcessInstanceIdList)
                .update(new ProcessInstanceRecord());

        processNodeRecordService.lambdaUpdate()
                .set(ProcessNodeRecord::getStatus, NodeStatusEnum.YJS.getCode())
                .set(ProcessNodeRecord::getEndTime, now)
                .in(ProcessNodeRecord::getProcessInstanceId, allStopProcessInstanceIdList)
                .eq(ProcessNodeRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .update(new ProcessNodeRecord());

        processNodeRecordAssignUserService.lambdaUpdate()
                .set(ProcessNodeRecordAssignUser::getStatus, assignUserStatus)
                .set(ProcessNodeRecordAssignUser::getEndTime, Convert.toLocalDateTime(now))
                .set(ProcessNodeRecordAssignUser::getTaskType,
                        Objects.equals(finishReason, ProcessInstanceConstant.FINISH_REASON_WITHDRAW)
                                ? "WITHDRAW"
                                : "TERMINATE")
                .in(ProcessNodeRecordAssignUser::getProcessInstanceId, allStopProcessInstanceIdList)
                .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.JXZ.getCode())
                .update(new ProcessNodeRecordAssignUser());

        allStopProcessInstanceIdList.forEach(processInstanceId -> {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .processInstanceTenantId(TenantContextHolder.getTenantId().toString())
                    .singleResult();
            if (processInstance == null) {
                return;
            }

            List<String> executionIds = runtimeService.createExecutionQuery()
                    .parentId(processInstanceId)
                    .list()
                    .stream()
                    .map(Execution::getId)
                    .toList();
            if (CollUtil.isEmpty(executionIds)) {
                log.warn("流程实例缺少可迁移执行流，跳过状态迁移: processInstanceId={}", processInstanceId);
                return;
            }

            runtimeService.createChangeActivityStateBuilder()
                    .moveExecutionsToSingleActivityId(executionIds, "end")
                    .changeState();
        });

        return R.ok();
    }

}
