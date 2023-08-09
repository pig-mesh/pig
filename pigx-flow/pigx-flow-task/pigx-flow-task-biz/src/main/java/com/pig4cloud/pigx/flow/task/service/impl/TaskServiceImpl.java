package com.pig4cloud.pigx.flow.task.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.task.api.feign.RemoteFlowEngineService;
import com.pig4cloud.pigx.flow.task.constant.FormTypeEnum;
import com.pig4cloud.pigx.flow.task.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.IndexPageStatistics;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.TaskParamDto;
import com.pig4cloud.pigx.flow.task.dto.TaskResultDto;
import com.pig4cloud.pigx.flow.task.entity.Process;
import com.pig4cloud.pigx.flow.task.entity.ProcessCopy;
import com.pig4cloud.pigx.flow.task.entity.ProcessInstanceRecord;
import com.pig4cloud.pigx.flow.task.entity.ProcessNodeRecordAssignUser;
import com.pig4cloud.pigx.flow.task.service.*;
import com.pig4cloud.pigx.flow.task.vo.FormItemVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {

	private final RemoteFlowEngineService flowEngineService;

	private final IProcessService processService;

	private final IProcessCopyService processCopyService;

	private final IProcessNodeDataService nodeDataService;

	private final IProcessNodeRecordAssignUserService processNodeRecordAssignUserService;

	private final IProcessInstanceRecordService processInstanceRecordService;

	private final ObjectMapper objectMapper;

	/**
	 * 查询首页数据看板
	 * @return
	 */
	@Override
	public R queryTaskData() {
		R<IndexPageStatistics> indexPageStatisticsR = flowEngineService
			.querySimpleData(SecurityUtils.getUser().getId());

		// 获取抄送任务
		Long copyCount = processCopyService.lambdaQuery()
			.eq(ProcessCopy::getUserId, SecurityUtils.getUser().getId())
			.count();
		indexPageStatisticsR.getData().setCopyNum(copyCount);
		return indexPageStatisticsR;
	}

	/**
	 * 查询任务
	 * @param taskId
	 * @param view
	 * @return
	 */
	@SneakyThrows
	@Override
	public R queryTask(String taskId, boolean view) {

		long userId = SecurityUtils.getUser().getId();

		R<TaskResultDto> r = flowEngineService.queryTask(taskId, userId);

		if (!r.isOk()) {
			return R.failed(r.getMsg());
		}

		TaskResultDto taskResultDto = r.getData();

		// 变量
		Map<String, Object> paramMap = taskResultDto.getVariableAll();
		// 是否是当前活动任务
		Boolean currentTask = taskResultDto.getCurrentTask();
		if (!currentTask) {
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

		List<FormItemVO> formItemVOList = JSONUtil.toList(oaForms.getFormItems(), FormItemVO.class);
		for (FormItemVO formItemVO : formItemVOList) {

			String id = formItemVO.getId();

			String perm = formPerms.get(id);

			if (StrUtil.isNotBlank(perm)) {

				formItemVO.setPerm(view ? (ProcessInstanceConstant.FormPermClass.EDIT.equals(perm)
						? ProcessInstanceConstant.FormPermClass.READ : perm) : perm);

			}
			else {
				formItemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
			}

			if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
				// 明细

				List<Map<String, Object>> subParamList = MapUtil.get(paramMap, id,
						new cn.hutool.core.lang.TypeReference<>() {
						});

				Object value = formItemVO.getProps().getValue();

				List<List<FormItemVO>> l = new ArrayList<>();
				for (Map<String, Object> map : subParamList) {
					List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
					for (FormItemVO itemVO : subItemList) {
						itemVO.getProps().setValue(map.get(itemVO.getId()));

						String permSub = formPerms.get(itemVO.getId());
						if (StrUtil.isNotBlank(permSub)) {
							itemVO.setPerm(view ? (ProcessInstanceConstant.FormPermClass.EDIT.equals(permSub)
									? ProcessInstanceConstant.FormPermClass.READ : permSub) : permSub);

						}
						else {
							itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
						}

					}
					l.add(subItemList);
				}
				formItemVO.getProps().setValue(l);
				{
					List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
					for (FormItemVO itemVO : subItemList) {

						String permSub = formPerms.get(itemVO.getId());
						if (StrUtil.isNotBlank(permSub)) {

							itemVO.setPerm(permSub);

						}
						else {
							itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
						}

					}
					formItemVO.getProps().setOriForm(subItemList);

				}

			}
			else {
				formItemVO.getProps().setValue(paramMap.get(id));

			}

		}
		Dict set = Dict.create()
			.set("processInstanceId", taskResultDto.getProcessInstanceId())
			.set("node", taskResultDto.getTaskNode())
			.set("process", oaForms.getProcess())
			.set("delegateAgain", taskResultDto.getDelegate())
			.set("delegationTask", StrUtil.equals(taskResultDto.getDelegationState(), "PENDING"))

			.set("formItems", formItemVOList);

		return R.ok(set);
	}

	/**
	 * 完成任务
	 * @param taskParamDto
	 * @return
	 */
	@Override
	public R completeTask(TaskParamDto taskParamDto) {
		long userId = SecurityUtils.getUser().getId();
		taskParamDto.setUserId(String.valueOf(userId));

		R r = flowEngineService.completeTask(taskParamDto);

		if (!r.isOk()) {
			return R.failed(r.getMsg());
		}

		return R.ok();
	}

	/**
	 * 前加签
	 * @param taskParamDto
	 * @return
	 */
	@Transactional
	@Override
	public R delegateTask(TaskParamDto taskParamDto) {

		taskParamDto.setUserId(SecurityUtils.getUser().getId().toString());

		R r = flowEngineService.delegateTask(taskParamDto);
		if (!r.isOk()) {
			return R.failed(r.getMsg());
		}

		return R.ok();
	}

	/**
	 * 加签完成任务
	 * @param taskParamDto
	 * @return
	 */
	@Override
	public R resolveTask(TaskParamDto taskParamDto) {
		R r = flowEngineService.resolveTask(taskParamDto);
		if (!r.isOk()) {
			return R.failed(r.getMsg());
		}

		return R.ok();
	}

	/**
	 * 设置执行人
	 * @param taskParamDto
	 * @return
	 */
	@Override
	public R setAssignee(TaskParamDto taskParamDto) {
		taskParamDto.setUserId(SecurityUtils.getUser().getId().toString());
		R r = flowEngineService.setAssignee(taskParamDto);
		if (!r.isOk()) {
			return R.failed(r.getMsg());
		}

		return R.ok();
	}

	/**
	 * 结束流程
	 * @param taskParamDto
	 * @return
	 */
	@Override
	public R stopProcessInstance(TaskParamDto taskParamDto) {

		String processInstanceId = taskParamDto.getProcessInstanceId();

		List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(processInstanceId);
		CollUtil.reverse(allStopProcessInstanceIdList);
		allStopProcessInstanceIdList.add(processInstanceId);

		taskParamDto.setProcessInstanceIdList(allStopProcessInstanceIdList);
		taskParamDto.setUserId(SecurityUtils.getUser().getId().toString());
		R r = flowEngineService.stopProcessInstance(taskParamDto);

		if (!r.isOk()) {
			return R.failed(r.getMsg());
		}

		return R.ok();
	}

	/**
	 * 退回
	 * @param taskParamDto
	 * @return
	 */
	@Override
	public R back(TaskParamDto taskParamDto) {
		taskParamDto.setUserId(SecurityUtils.getUser().getId().toString());
		R r = flowEngineService.back(taskParamDto);
		if (!r.isOk()) {
			return R.failed(r.getMsg());
		}

		return R.ok();
	}

	private List<String> getAllStopProcessInstanceIdList(String processInstanceId) {
		List<ProcessInstanceRecord> list = processInstanceRecordService.lambdaQuery()
			.eq(ProcessInstanceRecord::getParentProcessInstanceId, processInstanceId)
			.list();

		List<String> collect = list.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toList());

		for (ProcessInstanceRecord processInstanceRecord : list) {
			List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(
					processInstanceRecord.getProcessInstanceId());

			collect.addAll(allStopProcessInstanceIdList);

		}
		return collect;
	}

}
