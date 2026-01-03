package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.dto.*;
import com.pig4cloud.pigx.flow.entity.Process;
import com.pig4cloud.pigx.flow.entity.ProcessCopy;
import com.pig4cloud.pigx.flow.entity.ProcessInstanceRecord;
import com.pig4cloud.pigx.flow.entity.ProcessNodeRecord;
import com.pig4cloud.pigx.flow.service.*;
import com.pig4cloud.pigx.flow.support.utils.NodeFormatUtil;
import com.pig4cloud.pigx.flow.support.utils.NodeUtil;
import com.pig4cloud.pigx.flow.vo.NodeFormatParamVo;
import com.pig4cloud.pigx.flow.vo.NodeVo;
import com.pig4cloud.pigx.flow.vo.ProcessCopyVo;
import com.pig4cloud.pigx.flow.vo.ProcessInstanceRecordVo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程实例服务实现类
 * <p>
 * 该类是流程实例管理的核心服务，负责处理流程实例的完整生命周期，主要功能包括： 1. 流程实例的启动、结束等生命周期管理 2. 待办任务、已办任务的查询 3.
 * 我发起的流程、抄送给我的流程查询 4. 流程节点的格式化显示 5. 流程详情查询
 * <p>
 * 该服务整合了Flowable引擎和业务数据，为前端提供统一的流程操作接口
 * </p>
 *
 * @author pigx code generator
 * @date 2023-10-01
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessInstanceServiceImpl implements IProcessInstanceService {

    private final RemoteUserService userService;

    private final TaskService taskService;

    private final RuntimeService runtimeService;

    private final HistoryService historyService;

    private final IProcessInstanceRecordService processInstanceRecordService;

    private final IProcessCopyService processCopyService;

    private final IProcessService processService;

    private final IProcessNodeRecordService processNodeRecordService;

    private final ObjectMapper objectMapper;

    /**
     * 启动流程
     * <p>
     * 根据流程定义启动一个新的流程实例。该方法会： 1. 获取当前登录用户作为流程发起人 2. 设置流程发起人信息到流程变量中 3. 调用Flowable引擎启动流程
     * </p>
     *
     * @param processInstanceParamDto 流程启动参数，包含： - flowId: 流程定义ID - paramMap: 流程变量
     * @return R 启动结果，包含流程实例ID等信息
     */
    @Override
    public R startProcessInstance(ProcessInstanceParamDto processInstanceParamDto) {

        // 获取当前登录用户
        PigxUser user = SecurityUtils.getUser();

        // 设置流程发起人ID
        processInstanceParamDto.setStartUserId(String.valueOf(user.getId()));
        Map<String, Object> paramMap = processInstanceParamDto.getParamMap();

        // 将发起人信息作为root变量存入流程，用于流程节点的权限判断
        Dict rootUser = Dict.create().set("id", user.getId()).set("name", user.getUsername()).set("type", NodeUserTypeEnum.USER.getKey());
        paramMap.put("root", CollUtil.newArrayList(rootUser));

        // 调用引擎启动流程
        Authentication.setAuthenticatedUserId(processInstanceParamDto.getStartUserId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(processInstanceParamDto.getFlowId(), processInstanceParamDto.getParamMap(), TenantContextHolder.getTenantId().toString());

        String processInstanceId = processInstance.getProcessInstanceId();
        return R.ok(processInstanceId);
    }

    /**
     * 查询当前登录用户的待办任务
     * <p>
     * 查询分配给当前用户的待办任务列表，并补充以下信息： 1. 流程实例的详细信息（流程名称、分组等） 2. 流程发起人信息（姓名、头像等） 3. 流程启动时间
     * </p>
     *
     * @param taskQueryParamDto 查询参数，支持分页和条件过滤
     * @return R 包含分页后的待办任务列表
     */
    @Override
    public R queryMineTask(TaskQueryParamDto taskQueryParamDto) {
        taskQueryParamDto.setAssign(SecurityUtils.getUser().getId().toString());

        String assign = taskQueryParamDto.getAssign();

        List<TaskDto> taskDtoList = new ArrayList<>();

        int pageIndex = taskQueryParamDto.getCurrent() - 1;
        int pageSize = taskQueryParamDto.getSize();

        Page<TaskDto> pageResultDto = new Page<>();
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(assign).orderByTaskCreateTime().desc();
        if (StrUtil.isNotBlank(taskQueryParamDto.getProcessName())) {
            List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processDefinitionName(taskQueryParamDto.getProcessName()).processInstanceTenantId(TenantContextHolder.getTenantId().toString()).list();
            if (CollUtil.isNotEmpty(processInstanceList)) {
                taskQuery.processInstanceIdIn(processInstanceList.stream().map(ProcessInstance::getProcessInstanceId).toList());
            }
        }

        if (ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime())) {
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedBeforeDateTime = taskQueryParamDto.getTaskTime()[0].atZone(zoneId);
            ZonedDateTime zonedAfterDateTime = taskQueryParamDto.getTaskTime()[1].atZone(zoneId);
            Date beforeDate = Date.from(zonedBeforeDateTime.toInstant());
            Date afterDate = Date.from(zonedAfterDateTime.toInstant());
            taskQuery.taskCreatedBefore(afterDate).taskCreatedAfter(beforeDate);
        }

        taskQuery.listPage(pageIndex * pageSize, pageSize).forEach(task -> {
            String taskId = task.getId();
            String processInstanceId = task.getProcessInstanceId();
            log.debug("(taskId) {} processInstanceId={} executrionId={}", task.getName(), processInstanceId, task.getExecutionId());

            String taskDefinitionKey = task.getTaskDefinitionKey();
            String processDefinitionId = task.getProcessDefinitionId();
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            TaskDto taskDto = new TaskDto();
            taskDto.setFlowId(flowId);
            taskDto.setTaskCreateTime(task.getCreateTime());
            taskDto.setNodeId(taskDefinitionKey);
            taskDto.setProcessInstanceId(processInstanceId);
            taskDto.setTaskId(taskId);
            taskDto.setAssign(task.getAssignee());
            taskDto.setTaskName(task.getName());
            taskDtoList.add(taskDto);
        });

        long count = taskQuery.count();

        log.debug("当前有 {} 个任务:", count);

        pageResultDto.setTotal(count);
        pageResultDto.setRecords(taskDtoList);

        if (CollUtil.isEmpty(taskDtoList)) {
            return R.ok();
        }

        Set<String> processInstanceIdSet = taskDtoList.stream().map(TaskDto::getProcessInstanceId).collect(Collectors.toSet());

        // 流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId, processInstanceIdSet).list();

        // 发起人
        List<Long> collect = processInstanceRecordList.stream().map(ProcessInstanceRecord::getUserId).toList();
        List<SysUser> startUserList = userService.getUserById(collect).getData();

        List<TaskDto> taskNewDtoList = new ArrayList<>();
        for (TaskDto record : taskDtoList) {
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(), record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {
                record.setProcessName(processInstanceRecord.getName());
                SysUser startUser = startUserList.stream().filter(w -> w.getUserId().longValue() == processInstanceRecord.getUserId()).findAny().orElse(null);

                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getUsername());
                record.setRootUserAvatarUrl(startUser.getAvatar());
                record.setStartTime(processInstanceRecord.getCreateTime());

                taskNewDtoList.add(record);
            }
        }

        pageResultDto.setRecords(taskNewDtoList);

        return R.ok(pageResultDto);
    }

    /**
     * 查询已办任务
     * <p>
     * 查询当前用户已经完成的任务列表，包括： 1. 用户已审批的任务 2. 补充流程实例信息、发起人信息等
     * </p>
     *
     * @param taskQueryParamDto 查询参数，支持分页和时间范围过滤
     * @return R 包含分页后的已办任务列表
     */
    @Override
    public R queryMineEndTask(TaskQueryParamDto taskQueryParamDto) {
        taskQueryParamDto.setAssign(SecurityUtils.getUser().getId().toString());

        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        HistoricActivityInstanceQuery activityInstanceQuery = historicActivityInstanceQuery.taskAssignee(taskQueryParamDto.getAssign()).finished().orderByHistoricActivityInstanceEndTime().desc();

        if (ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime())) {
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedBeforeDateTime = taskQueryParamDto.getTaskTime()[0].atZone(zoneId);
            ZonedDateTime zonedAfterDateTime = taskQueryParamDto.getTaskTime()[1].atZone(zoneId);
            Date beforeDate = Date.from(zonedBeforeDateTime.toInstant());
            Date afterDate = Date.from(zonedAfterDateTime.toInstant());
            activityInstanceQuery.finishedBefore(afterDate).finishedAfter(beforeDate);
        }

        List<HistoricActivityInstance> list = activityInstanceQuery.listPage((taskQueryParamDto.getCurrent() - 1) * taskQueryParamDto.getSize(), taskQueryParamDto.getSize());

        long count = activityInstanceQuery.count();
        List<TaskDto> taskDtoList = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : list) {
            String activityId = historicActivityInstance.getActivityId();
            String activityName = historicActivityInstance.getActivityName();
            String executionId = historicActivityInstance.getExecutionId();
            String taskId = historicActivityInstance.getTaskId();
            Date startTime = historicActivityInstance.getStartTime();
            Date endTime = historicActivityInstance.getEndTime();
            Long durationInMillis = historicActivityInstance.getDurationInMillis();
            String processInstanceId = historicActivityInstance.getProcessInstanceId();

            String processDefinitionId = historicActivityInstance.getProcessDefinitionId();
            // 流程id
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            TaskDto taskDto = new TaskDto();
            taskDto.setFlowId(flowId);
            taskDto.setTaskCreateTime(startTime);
            taskDto.setTaskEndTime(endTime);
            taskDto.setNodeId(activityId);
            taskDto.setExecutionId(executionId);
            taskDto.setProcessInstanceId(processInstanceId);
            taskDto.setDurationInMillis(durationInMillis);
            taskDto.setTaskId(taskId);
            taskDto.setAssign(historicActivityInstance.getAssignee());
            taskDto.setTaskName(activityName);

            taskDtoList.add(taskDto);
        }

        Page<TaskDto> pageResultDto = new Page<>();
        pageResultDto.setTotal(count);
        pageResultDto.setRecords(taskDtoList);

        List<TaskDto> records = pageResultDto.getRecords();
        if (CollUtil.isEmpty(records)) {
            return R.ok(pageResultDto);

        }

        Set<String> processInstanceIdSet = records.stream().map(TaskDto::getProcessInstanceId).collect(Collectors.toSet());

        // 流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId, processInstanceIdSet).list();

        // 发起人
        List<Long> collect = processInstanceRecordList.stream().map(ProcessInstanceRecord::getUserId).toList();
        List<SysUser> startUserList = userService.getUserById(collect).getData();

        List<TaskDto> taskNewDtoList = new ArrayList<>();
        for (TaskDto record : records) {

            ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(), record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {

                record.setProcessName(processInstanceRecord.getName());

                SysUser startUser = startUserList.stream().filter(w -> w.getUserId().longValue() == processInstanceRecord.getUserId()).findAny().orElse(null);

                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getName());
                record.setRootUserAvatarUrl(startUser.getAvatar());
                record.setTaskId(record.getTaskId());
                record.setStartTime(processInstanceRecord.getCreateTime());
                taskNewDtoList.add(record);
            }
        }

        pageResultDto.setRecords(taskNewDtoList);

        return R.ok(pageResultDto);
    }

    /**
     * 结束流程实例
     * <p>
     * 手动结束一个流程实例，通常用于： 1. 流程审批完成，正常结束 2. 流程被终止或取消
     * <p>
     * 该方法会更新流程实例记录的结束时间、状态和结束原因
     * </p>
     *
     * @param processInstanceParamDto 流程实例参数，包含： - processInstanceId: 流程实例ID - paramMap:
     *                                可能包含approve_condition等结束原因
     * @return R 操作结果
     */
    @Override
    public R end(ProcessInstanceParamDto processInstanceParamDto) {

        // 从参数中提取审批条件，用于判断流程结束原因（通过/拒绝）
        Optional<Boolean> objectOptional = processInstanceParamDto.getParamMap().keySet().stream().filter(key -> key.contains("approve_condition")).reduce((first, second) -> second).map(key -> MapUtil.getBool(processInstanceParamDto.getParamMap(), key, true));

        // 构建更新条件
        LambdaUpdateChainWrapper<ProcessInstanceRecord> lambdaUpdateChainWrapper = processInstanceRecordService.lambdaUpdate().set(ProcessInstanceRecord::getEndTime, new Date()).set(ProcessInstanceRecord::getStatus, NodeStatusEnum.YJS.getCode());

        // 如果有审批结果，设置结束原因
        objectOptional.ifPresent(aBoolean -> lambdaUpdateChainWrapper.set(ProcessInstanceRecord::getFinishReason, aBoolean));

        // 执行更新
        lambdaUpdateChainWrapper.eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceParamDto.getProcessInstanceId()).update(new ProcessInstanceRecord());
        return R.ok();
    }

    /**
     * 查询我发起的流程
     * <p>
     * 查询当前用户发起的所有流程实例，支持： 1. 按状态过滤（进行中、已完成等） 2. 按时间范围过滤 3. 分页查询
     * </p>
     *
     * @param taskQueryParamDto 查询条件，包含状态、时间范围、分页参数
     * @return R 包含分页后的流程实例列表
     */
    @Override
    public R queryMineStarted(TaskQueryParamDto taskQueryParamDto) {

        long userId = SecurityUtils.getUser().getId();

        Page<ProcessInstanceRecord> instanceRecordPage = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getUserId, userId).eq(ProcessInstanceRecord::getStatus, taskQueryParamDto.getStatus()).between(ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()), ProcessInstanceRecord::getCreateTime, ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()) ? taskQueryParamDto.getTaskTime()[0] : null, ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()) ? taskQueryParamDto.getTaskTime()[1] : null).orderByDesc(ProcessInstanceRecord::getCreateTime).page(new Page<>(taskQueryParamDto.getCurrent(), taskQueryParamDto.getSize()));

        // 转换为 VO 并添加驳回状态信息
        List<ProcessInstanceRecordVo> voList = BeanUtil.copyToList(instanceRecordPage.getRecords(), ProcessInstanceRecordVo.class);

        for (ProcessInstanceRecordVo vo : voList) {
            try {
                // 从 Flowable 变量中获取 rejectToStarter 状态
                Object rejectVar = runtimeService.getVariable(
                    vo.getProcessInstanceId(),
                    "rejectToStarter"
                );
                vo.setRejectToStarter(Boolean.TRUE.equals(rejectVar));

                // 如果是驳回状态，获取 root 任务 ID 用于重新提交
                if (Boolean.TRUE.equals(vo.getRejectToStarter())) {
                    Task task = taskService.createTaskQuery()
                        .processInstanceId(vo.getProcessInstanceId())
                        .taskDefinitionKey("root")
                        .singleResult();
                    if (task != null) {
                        vo.setResubmitTaskId(task.getId());
                    }
                }
            } catch (Exception e) {
                // 流程可能已结束，设置为 false
                vo.setRejectToStarter(false);
            }
        }

        // 构建返回的分页对象
        Page<ProcessInstanceRecordVo> resultPage = new Page<>(instanceRecordPage.getCurrent(), instanceRecordPage.getSize(), instanceRecordPage.getTotal());
        resultPage.setRecords(voList);

        return R.ok(resultPage);
    }

    /**
     * 查询抄送给我的流程
     * <p>
     * 查询抄送给当前用户的流程列表。抄送功能允许流程参与者将流程信息 通知给其他相关人员，被抄送人可以查看流程但不参与审批
     * </p>
     *
     * @param taskQueryParamDto 查询参数，支持时间范围过滤和分页
     * @return R 包含分页后的抄送记录，包括发起人信息
     */
    @Override
    public R queryMineCC(TaskQueryParamDto taskQueryParamDto) {

        long userId = SecurityUtils.getUser().getId();

        Page<ProcessCopy> page = processCopyService.lambdaQuery().eq(ProcessCopy::getUserId, userId).between(ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()), ProcessCopy::getCreateTime, ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()) ? taskQueryParamDto.getTaskTime()[0] : null, ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()) ? taskQueryParamDto.getTaskTime()[1] : null).orderByDesc(ProcessCopy::getNodeTime).page(new Page<>(taskQueryParamDto.getCurrent(), taskQueryParamDto.getSize()));

        List<ProcessCopy> records = page.getRecords();

        List<ProcessCopyVo> processCopyVoList = BeanUtil.copyToList(records, ProcessCopyVo.class);

        if (CollUtil.isNotEmpty(records)) {
            // 发起人
            List<Long> userIdList = records.stream().map(ProcessCopy::getStartUserId).toList();
            List<SysUser> startUserList = userService.getUserById(userIdList).getData();

            for (ProcessCopyVo record : processCopyVoList) {
                SysUser startUser = startUserList.stream().filter(w -> w.getUserId().longValue() == record.getStartUserId()).findAny().orElse(null);
                record.setStartUserName(startUser.getUsername());
            }
        }

        Page p = BeanUtil.copyProperties(page, Page.class);
        p.setRecords(processCopyVoList);
        return R.ok(p);
    }

    /**
     * 格式化流程显示
     * <p>
     * 将流程定义数据格式化为前端可展示的节点结构，主要功能： 1. 解析流程定义的JSON数据 2. 标记已完成的节点 3. 根据流程变量计算条件节点的显示状态 4.
     * 处理节点权限和表单权限
     * <p>
     * 该方法用于流程图的可视化展示
     * </p>
     *
     * @param nodeFormatParamVo 格式化参数，包含： - flowId: 流程定义ID - processInstanceId: 流程实例ID -
     *                          taskId: 任务ID（可选） - paramMap: 流程变量
     * @return R 包含格式化后的节点列表
     */
    @SneakyThrows
    @Override
    public R formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo) {
        String flowId = nodeFormatParamVo.getFlowId();
        String processInstanceId = nodeFormatParamVo.getProcessInstanceId();
        if (StrUtil.isAllBlank(flowId, processInstanceId)) {
            return R.ok(new ArrayList<>());
        }

        if (StrUtil.isBlankIfStr(flowId) && StrUtil.isNotBlank(processInstanceId)) {
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();
            flowId = processInstanceRecord.getFlowId();

        }
        Map<String, Object> paramMap = nodeFormatParamVo.getParamMap();
        if (StrUtil.isNotBlank(nodeFormatParamVo.getTaskId())) {
            VariableQueryParamDto variableQueryParamDto = new VariableQueryParamDto();
            variableQueryParamDto.setTaskId(nodeFormatParamVo.getTaskId());
            Map<String, Object> variables;
            List<String> keyList = variableQueryParamDto.getKeyList();
            if (CollUtil.isEmpty(keyList)) {
                TaskQuery taskQuery = taskService.createTaskQuery();

                Task task = taskQuery.taskId(variableQueryParamDto.getTaskId()).singleResult();
                if (task == null) {
                    return R.failed("任务不存在");
                }

                variables = runtimeService.getVariables(task.getExecutionId());
            } else {
                variables = taskService.getVariables(variableQueryParamDto.getTaskId(), keyList);
            }
            paramMap.putAll(variables);
        }

        Set<String> completeNodeSet = new HashSet<>();
        Set<String> continueNodeSet = new HashSet<>();
        if (StrUtil.isNotBlank(processInstanceId)) {
            List<ProcessNodeRecord> processNodeRecordList = processNodeRecordService.lambdaQuery().eq(ProcessNodeRecord::getProcessInstanceId, processInstanceId).eq(ProcessNodeRecord::getStatus, NodeStatusEnum.YJS.getCode()).list();
            Set<String> collect = processNodeRecordList.stream().map(ProcessNodeRecord::getNodeId).collect(Collectors.toSet());


            List<ProcessNodeRecord> jxzNodeRecordList = processNodeRecordService.lambdaQuery().eq(ProcessNodeRecord::getProcessInstanceId, processInstanceId).eq(ProcessNodeRecord::getStatus, NodeStatusEnum.JXZ.getCode()).list();

            Set<String> jxzCollect = jxzNodeRecordList.stream().map(ProcessNodeRecord::getNodeId).collect(Collectors.toSet());
            continueNodeSet.addAll(jxzCollect);
            completeNodeSet.addAll(collect);
        }

        Process oaForms = processService.getByFlowId(flowId);
        String process = oaForms.getProcess();
        Node nodeDto = objectMapper.readValue(process, new TypeReference<>() {
        });

        List<NodeVo> processNodeShowDtos = NodeFormatUtil.formatProcessNodeShow(nodeDto, completeNodeSet, continueNodeSet, processInstanceId, paramMap);

        return R.ok(processNodeShowDtos);
    }

    /**
     * 流程详情
     * <p>
     * 查询流程实例的详细信息，包括： 1. 流程定义信息（流程结构） 2. 表单项定义 3. 表单数据（用户填写的数据） 4. 表单权限配置 5. 流程状态信息
     * <p>
     * 该方法主要用于流程详情页面的数据展示
     * </p>
     *
     * @param processInstanceId 流程实例ID
     * @return R 包含流程详情的Dict对象
     * @throws Exception 当解析JSON数据失败时抛出异常
     */
    @SneakyThrows
    @Override
    public R detail(String processInstanceId) {
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();

        Process oaForms = processService.getByFlowId(processInstanceRecord.getFlowId());
        if (oaForms == null) {
            return R.failed("流程不存在");
        }

        // 发起人变量数据
        String formData = processInstanceRecord.getFormData();
        String process = oaForms.getProcess();

        Node nodeDto = objectMapper.readValue(process, Node.class);
        Map<String, String> formPerms1 = nodeDto.getFormPerms();

        Dict set = Dict.create().set(ProcessInstanceRecord.Fields.processInstanceId, processInstanceId).set(ProcessInstanceRecord.Fields.status, processInstanceRecord.getStatus()).set("process", oaForms.getProcess()).set("formItems", oaForms.getFormItems()).set("formData", formData).set("formData", formData).set("formConfig", oaForms.getFormConfig()).set("formPerms", formPerms1);

        return R.ok(set);
    }

}
