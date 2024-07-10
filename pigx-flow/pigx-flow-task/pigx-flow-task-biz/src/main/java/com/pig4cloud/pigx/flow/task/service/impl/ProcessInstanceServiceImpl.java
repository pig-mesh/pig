package com.pig4cloud.pigx.flow.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.task.api.feign.RemoteFlowEngineService;
import com.pig4cloud.pigx.flow.task.constant.FormTypeEnum;
import com.pig4cloud.pigx.flow.task.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.task.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.*;
import com.pig4cloud.pigx.flow.task.entity.Process;
import com.pig4cloud.pigx.flow.task.entity.*;
import com.pig4cloud.pigx.flow.task.service.*;
import com.pig4cloud.pigx.flow.task.utils.NodeFormatUtil;
import com.pig4cloud.pigx.flow.task.vo.FormItemVO;
import com.pig4cloud.pigx.flow.task.vo.NodeFormatParamVo;
import com.pig4cloud.pigx.flow.task.vo.NodeVo;
import com.pig4cloud.pigx.flow.task.vo.ProcessCopyVo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 实例进程服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessInstanceServiceImpl implements IProcessInstanceService {

    private final RemoteFlowEngineService flowEngineService;

    private final RemoteUserService userService;

    private final IProcessInstanceRecordService processInstanceRecordService;

    private final IProcessCopyService processCopyService;

    private final IProcessService processService;

    private final IProcessNodeRecordService processNodeRecordService;

    private final IProcessNodeRecordAssignUserService processNodeRecordAssignUserService;

    private final ObjectMapper objectMapper;

    /**
     * 启动流程
     *
     * @param processInstanceParamDto
     * @return
     */
    @Override
    public R startProcessInstance(ProcessInstanceParamDto processInstanceParamDto) {

        PigxUser user = SecurityUtils.getUser();

        processInstanceParamDto.setStartUserId(String.valueOf(user.getId()));
        Map<String, Object> paramMap = processInstanceParamDto.getParamMap();
        Dict rootUser = Dict.create()
                .set("id", user.getId())
                .set("name", user.getUsername())
                .set("type", NodeUserTypeEnum.USER.getKey());
        paramMap.put("root", CollUtil.newArrayList(rootUser));
        return flowEngineService.startProcess(processInstanceParamDto);
    }

    /**
     * 查询当前登录用户的待办任务
     *
     * @param taskQueryParamDto 查询参数
     * @return R
     */
    @Override
    public R queryMineTask(TaskQueryParamDto taskQueryParamDto) {
        taskQueryParamDto.setAssign(SecurityUtils.getUser().getId().toString());
		R<Page<TaskDto>> r = flowEngineService.queryAssignTask(taskQueryParamDto);

		if (CollUtil.isEmpty(r.getData().getRecords())) {
			return r;
		}

		Set<String> processInstanceIdSet = r.getData()
			.getRecords()
			.stream()
			.map(TaskDto::getProcessInstanceId)
			.collect(Collectors.toSet());

		// 流程实例记录
		List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery()
			.in(ProcessInstanceRecord::getProcessInstanceId, processInstanceIdSet)
			.list();

		// 发起人
        // 发起人
        List<Long> collect = processInstanceRecordList.stream().map(ProcessInstanceRecord::getUserId).collect(Collectors.toList());
        List<SysUser> startUserList = userService.getUserById(collect).getData();

		Page<TaskDto> pageResultDto = r.getData();
		List<TaskDto> taskDtoList = new ArrayList<>();
		for (TaskDto record : r.getData().getRecords()) {
			ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream()
				.filter(w -> StrUtil.equals(w.getProcessInstanceId(), record.getProcessInstanceId()))
				.findAny()
				.orElse(null);

			if (processInstanceRecord != null) {
				record.setProcessName(processInstanceRecord.getName());
				SysUser startUser = startUserList.stream()
					.filter(w -> w.getUserId().longValue() == processInstanceRecord.getUserId())
					.findAny()
					.orElse(null);

				record.setRootUserId(processInstanceRecord.getUserId());
				record.setGroupName(processInstanceRecord.getGroupName());
				record.setRootUserName(startUser.getUsername());
				record.setRootUserAvatarUrl(startUser.getAvatar());
				record.setStartTime(processInstanceRecord.getCreateTime());

				taskDtoList.add(record);
			}
		}

		pageResultDto.setRecords(taskDtoList);

		return R.ok(pageResultDto);
    }

    /**
     * 查询已办任务
     *
     * @param taskQueryParamDto 查询参数
     * @return R
     */
    @Override
    public R queryMineEndTask(TaskQueryParamDto taskQueryParamDto) {
        taskQueryParamDto.setAssign(SecurityUtils.getUser().getId().toString());
        R<Page<TaskDto>> r = flowEngineService.queryCompletedTask(taskQueryParamDto);

        Page<TaskDto> pageResultDto = r.getData();
        List<TaskDto> records = pageResultDto.getRecords();
        if (CollUtil.isEmpty(records)) {
            return R.ok(pageResultDto);

        }

        Set<String> processInstanceIdSet = records.stream()
                .map(TaskDto::getProcessInstanceId)
                .collect(Collectors.toSet());

        // 流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList = processInstanceRecordService.lambdaQuery()
                .in(ProcessInstanceRecord::getProcessInstanceId, processInstanceIdSet)
                .list();

        // 发起人
        List<Long> collect = processInstanceRecordList.stream().map(ProcessInstanceRecord::getUserId).collect(Collectors.toList());
        List<SysUser> startUserList = userService.getUserById(collect).getData();

        List<TaskDto> taskDtoList = new ArrayList<>();
        for (TaskDto record : records) {

            ProcessInstanceRecord processInstanceRecord = processInstanceRecordList.stream()
                    .filter(w -> StrUtil.equals(w.getProcessInstanceId(), record.getProcessInstanceId()))
                    .findAny()
                    .orElse(null);

            if (processInstanceRecord != null) {

                record.setProcessName(processInstanceRecord.getName());

                SysUser startUser = startUserList.stream()
                        .filter(w -> w.getUserId().longValue() == processInstanceRecord.getUserId())
                        .findAny()
                        .orElse(null);

                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getName());
                record.setRootUserAvatarUrl(startUser.getAvatar());
                record.setTaskId(record.getTaskId());
                record.setStartTime(processInstanceRecord.getCreateTime());
                taskDtoList.add(record);
            }
        }

        pageResultDto.setRecords(taskDtoList);

        return R.ok(pageResultDto);
    }

    /**
     * 流程结束
     *
     * @param processsInstanceId
     * @return
     */
    @Override
    public R end(String processsInstanceId) {
        processInstanceRecordService.lambdaUpdate()
                .set(ProcessInstanceRecord::getEndTime, new Date())
                .set(ProcessInstanceRecord::getStatus, NodeStatusEnum.YJS.getCode())
                .eq(ProcessInstanceRecord::getProcessInstanceId, processsInstanceId)
                .update(new ProcessInstanceRecord());
        return R.ok();
    }

    /**
     * 查询我发起的
     *
     * @param taskQueryParamDto 查询条件
     * @return
     */
    @Override
    public R queryMineStarted(TaskQueryParamDto taskQueryParamDto) {

        long userId = SecurityUtils.getUser().getId();

        Page<ProcessInstanceRecord> instanceRecordPage = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getUserId, userId)
                .eq(ProcessInstanceRecord::getStatus, taskQueryParamDto.getStatus())
                .between(ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()), ProcessInstanceRecord::getCreateTime,
                        ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()) ? taskQueryParamDto.getTaskTime()[0] : null,
                        ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()) ? taskQueryParamDto.getTaskTime()[1] : null)
                .orderByDesc(ProcessInstanceRecord::getCreateTime)
                .page(new Page<>(taskQueryParamDto.getCurrent(), taskQueryParamDto.getSize()));

        return R.ok(instanceRecordPage);
    }

    /**
     * 查询抄送给我的
     *
     * @param taskQueryParamDto 查询参数
     * @return
     */
    @Override
    public R queryMineCC(TaskQueryParamDto taskQueryParamDto) {

        long userId = SecurityUtils.getUser().getId();

        Page<ProcessCopy> page = processCopyService.lambdaQuery()
                .eq(ProcessCopy::getUserId, userId)
                .between(ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()), ProcessCopy::getCreateTime,
                        ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()) ? taskQueryParamDto.getTaskTime()[0] : null,
                        ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime()) ? taskQueryParamDto.getTaskTime()[1] : null)
                .orderByDesc(ProcessCopy::getNodeTime)
                .page(new Page<>(taskQueryParamDto.getCurrent(), taskQueryParamDto.getSize()));

        List<ProcessCopy> records = page.getRecords();

        List<ProcessCopyVo> processCopyVoList = BeanUtil.copyToList(records, ProcessCopyVo.class);

        if (CollUtil.isNotEmpty(records)) {
            // 发起人
            List<Long> userIdList = records.stream().map(ProcessCopy::getStartUserId).collect(Collectors.toList());
            List<SysUser> startUserList = userService.getUserById(userIdList).getData();

            for (ProcessCopyVo record : processCopyVoList) {
                SysUser startUser = startUserList.stream()
                        .filter(w -> w.getUserId().longValue() == record.getStartUserId())
                        .findAny()
                        .orElse(null);
                record.setStartUserName(startUser.getUsername());
            }
        }

        Page p = BeanUtil.copyProperties(page, Page.class);

        p.setRecords(processCopyVoList);

        return R.ok(p);
    }

    /**
     * 格式化流程显示
     *
     * @param nodeFormatParamVo
     * @return
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
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                    .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                    .one();
            flowId = processInstanceRecord.getFlowId();

        }
        Map<String, Object> paramMap = nodeFormatParamVo.getParamMap();
        if (StrUtil.isNotBlank(nodeFormatParamVo.getTaskId())) {
            VariableQueryParamDto variableQueryParamDto = new VariableQueryParamDto();
            variableQueryParamDto.setTaskId(nodeFormatParamVo.getTaskId());
            R<Map<String, Object>> r = flowEngineService.queryTaskVariables(variableQueryParamDto);
            if (!r.isOk()) {
                ProcessNodeRecordAssignUser processNodeRecordAssignUser = processNodeRecordAssignUserService
                        .lambdaQuery()
                        .eq(ProcessNodeRecordAssignUser::getTaskId, nodeFormatParamVo.getTaskId())
                        .eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.YJS.getCode())
                        .last("limit 1")
                        .orderByDesc(ProcessNodeRecordAssignUser::getEndTime)
                        .one();

                String data = processNodeRecordAssignUser.getData();

                Map<String, Object> variableMap = objectMapper.readValue(data, new TypeReference<>() {
                });
                variableMap.putAll(paramMap);
                paramMap.putAll(variableMap);
            } else {
                Map<String, Object> variableMap = r.getData();
                variableMap.putAll(paramMap);
                paramMap.putAll(variableMap);
            }

        }

        Set<String> completeNodeSet = new HashSet<>();

        if (StrUtil.isNotBlank(processInstanceId)) {
            List<ProcessNodeRecord> processNodeRecordList = processNodeRecordService.lambdaQuery()
                    .eq(ProcessNodeRecord::getProcessInstanceId, processInstanceId)
                    .eq(ProcessNodeRecord::getStatus, NodeStatusEnum.YJS.getCode())
                    .list();
            Set<String> collect = processNodeRecordList.stream()
                    .map(ProcessNodeRecord::getNodeId)
                    .collect(Collectors.toSet());
            completeNodeSet.addAll(collect);
        }

        Process oaForms = processService.getByFlowId(flowId);
        String process = oaForms.getProcess();
        Node nodeDto = objectMapper.readValue(process, new TypeReference<>() {
        });

        List<NodeVo> processNodeShowDtos = NodeFormatUtil.formatProcessNodeShow(nodeDto, completeNodeSet,
                new HashSet<>(), processInstanceId, paramMap);

        return R.ok(processNodeShowDtos);
    }

    /**
     * 流程详情
     *
     * @param processInstanceId
     * @return
     */
    @SneakyThrows
    @Override
    public R detail(String processInstanceId) {
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                .one();

        Process oaForms = processService.getByFlowId(processInstanceRecord.getFlowId());
        if (oaForms == null) {
            return R.failed("流程不存在");
        }

        // 发起人变量数据
        String formData = processInstanceRecord.getFormData();
        Map<String, Object> variableMap = objectMapper.readValue(formData, new TypeReference<>() {
        });
        // 发起人表单权限
        String process = oaForms.getProcess();

        Node nodeDto = objectMapper.readValue(process, Node.class);
        Map<String, String> formPerms1 = nodeDto.getFormPerms();

        List<FormItemVO> jsonObjectList = objectMapper.readValue(oaForms.getFormItems(), new TypeReference<>() {
        });
        for (FormItemVO formItemVO : jsonObjectList) {
            String id = formItemVO.getId();
            String perm = formPerms1.get(id);

            formItemVO.setPerm(StrUtil.isBlankIfStr(perm) ? ProcessInstanceConstant.FormPermClass.READ
                    : (StrUtil.equals(perm, ProcessInstanceConstant.FormPermClass.HIDE) ? perm
                    : ProcessInstanceConstant.FormPermClass.READ));

            if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
                // 明细
                List<Map<String, Object>> subParamList = MapUtil.get(variableMap, id,
                        new cn.hutool.core.lang.TypeReference<>() {
                        });

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l = new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        itemVO.getProps().setValue(map.get(itemVO.getId()));

                        String permSub = formPerms1.get(itemVO.getId());

                        itemVO.setPerm(StrUtil.isBlankIfStr(permSub) ? ProcessInstanceConstant.FormPermClass.READ
                                : (StrUtil.equals(permSub, ProcessInstanceConstant.FormPermClass.HIDE) ? permSub
                                : ProcessInstanceConstant.FormPermClass.READ));

                    }
                    l.add(subItemList);
                }
                formItemVO.getProps().setValue(l);

            } else {
                formItemVO.getProps().setValue(variableMap.get(id));

            }

        }
        Dict set = Dict.create()
                .set("processInstanceId", processInstanceId)
                .set("process", oaForms.getProcess())

                .set("formItems", jsonObjectList);

        return R.ok(set);
    }

}
