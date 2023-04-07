/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.act.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.act.dto.CommentDto;
import com.pig4cloud.pigx.act.dto.LeaveBillDto;
import com.pig4cloud.pigx.act.dto.TaskDTO;
import com.pig4cloud.pigx.act.entity.LeaveBill;
import com.pig4cloud.pigx.act.mapper.ActTaskMapper;
import com.pig4cloud.pigx.act.mapper.LeaveBillMapper;
import com.pig4cloud.pigx.act.service.ActTaskService;
import com.pig4cloud.pigx.common.core.constant.PaginationConstants;
import com.pig4cloud.pigx.common.core.constant.enums.TaskStatusEnum;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2018/9/25
 */
@Slf4j
@Service
@AllArgsConstructor
public class ActTaskServiceImpl implements ActTaskService {

	private static final String FLAG = "审批";

	private final LeaveBillMapper leaveBillMapper;

	private final TaskService taskService;

	private final RuntimeService runtimeService;

	private final RepositoryService repositoryService;

	private final HistoryService historyService;

	private final ProcessEngineFactoryBean processEngine;

	@Override
	public IPage getTaskByName(Map<String, Object> params, String name) {
		int page = MapUtil.getInt(params, PaginationConstants.CURRENT);
		int limit = MapUtil.getInt(params, PaginationConstants.SIZE);

		TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(name)
				.taskTenantId(String.valueOf(TenantContextHolder.getTenantId()));

		IPage result = new Page(page, limit);
		result.setTotal(taskQuery.count());

		List<TaskDTO> taskDTOList = taskQuery.orderByTaskCreateTime().desc().listPage((page - 1) * limit, limit)
				.stream().map(task -> {
					TaskDTO dto = new TaskDTO();
					dto.setTaskId(task.getId());
					dto.setTaskName(task.getName());
					dto.setProcessInstanceId(task.getProcessInstanceId());
					dto.setNodeKey(task.getTaskDefinitionKey());
					dto.setCategory(task.getCategory());
					dto.setTime(task.getCreateTime());
					return dto;
				}).collect(Collectors.toList());
		result.setRecords(taskDTOList);
		return result;
	}

	/**
	 * 通过任务ID查询任务信息关联申请单信息
	 * @param taskId
	 * @return
	 */
	@Override
	public LeaveBillDto getTaskById(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId())
				.singleResult();

		String businessKey = pi.getBusinessKey();
		if (StrUtil.isNotBlank(businessKey)) {
			businessKey = businessKey.split("_")[1];
		}

		List<String> comeList = findOutFlagListByTaskId(task, pi);
		LeaveBill leaveBill = leaveBillMapper.selectById(businessKey);

		LeaveBillDto leaveBillDto = new LeaveBillDto();
		BeanUtils.copyProperties(leaveBill, leaveBillDto);
		leaveBillDto.setTaskId(taskId);
		leaveBillDto.setTime(task.getCreateTime());
		leaveBillDto.setFlagList(comeList);
		return leaveBillDto;
	}

	/**
	 * 提交任务
	 * @param leaveBillDto
	 * @return
	 */
	@Override
	public Boolean submitTask(LeaveBillDto leaveBillDto) {
		String taskId = leaveBillDto.getTaskId();
		String message = leaveBillDto.getComment();
		Long id = leaveBillDto.getLeaveId();

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

		String processInstanceId = task.getProcessInstanceId();
		Authentication.setAuthenticatedUserId(SecurityUtils.getUser().getUsername());
		taskService.addComment(taskId, processInstanceId, message);

		Map<String, Object> variables = new HashMap<>(4);
		variables.put("flag", leaveBillDto.getTaskFlag());
		variables.put("days", leaveBillDto.getDays());

		taskService.complete(taskId, variables);
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();

		if (pi == null) {
			LeaveBill bill = new LeaveBill();
			bill.setLeaveId(id);
			bill.setState(StrUtil.equals(TaskStatusEnum.OVERRULE.getDescription(), leaveBillDto.getTaskFlag())
					? TaskStatusEnum.OVERRULE.getStatus() : TaskStatusEnum.COMPLETED.getStatus());
			leaveBillMapper.updateById(bill);
		}
		return null;
	}

	@Override
	public List<CommentDto> getCommentByTaskId(String taskId) {
		// 使用当前的任务ID，查询当前流程对应的历史任务ID
		// 使用当前任务ID，获取当前任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 获取流程实例ID
		List<CommentDto> commentDtoList = taskService.getProcessInstanceComments(task.getProcessInstanceId()).stream()
				.map(comment -> {
					CommentDto commentDto = new CommentDto();
					commentDto.setId(comment.getId());
					commentDto.setTime(comment.getTime());
					commentDto.setType(comment.getType());
					commentDto.setTaskId(comment.getTaskId());
					commentDto.setUserId(comment.getUserId());
					commentDto.setFullMessage(comment.getFullMessage());
					commentDto.setProcessInstanceId(comment.getProcessInstanceId());
					return commentDto;
				}).collect(Collectors.toList());
		return commentDtoList;
	}

	/**
	 * 追踪图片节点
	 * @param id
	 */
	@Override
	public InputStream viewByTaskId(String id) {
		// 使用当前任务ID，获取当前任务对象
		Task task = taskService.createTaskQuery().taskId(id).singleResult();

		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		String processDefinitionId = null;
		List<String> executedActivityIdList = new ArrayList<>();
		if (processInstance != null) {
			processDefinitionId = processInstance.getProcessDefinitionId();
			executedActivityIdList = this.runtimeService.getActiveActivityIds(processInstance.getId());
		}
		else if (historicProcessInstance != null) {
			processDefinitionId = historicProcessInstance.getProcessDefinitionId();
			executedActivityIdList = historyService.createHistoricActivityInstanceQuery()
					.processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().asc().list().stream()
					.map(HistoricActivityInstance::getActivityId).collect(Collectors.toList());
		}

		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
		Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
		ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();

		return diagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList, Collections.emptyList(),
				processEngine.getProcessEngineConfiguration().getActivityFontName(),
				processEngine.getProcessEngineConfiguration().getLabelFontName(), "宋体", null, 1.0);

	}

	/**
	 * 批量删除
	 * @param ids
	 */
	@Override
	public void delTasks(String[] ids) {
		for (String taskId : ids) {
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

			runtimeService.suspendProcessInstanceById(task.getProcessInstanceId());
			runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "Deleted");
		}
	}

	@Override
	public List list(TaskDTO taskDTO) {

		TaskQuery taskQuery = taskService.createTaskQuery();
		List<TaskDTO> taskDTOList = taskQuery.orderByTaskCreateTime().desc().list().stream().map(task -> {
			TaskDTO dto = new TaskDTO();
			dto.setTaskId(task.getId());
			dto.setTaskName(task.getName());
			dto.setProcessInstanceId(task.getProcessInstanceId());
			dto.setNodeKey(task.getTaskDefinitionKey());
			dto.setCategory(task.getCategory());
			dto.setTime(task.getCreateTime());
			return dto;
		}).collect(Collectors.toList());
		return taskDTOList;
	}

	private List<String> findOutFlagListByTaskId(Task task, ProcessInstance pi) {
		// 查询ProcessDefinitionEntiy对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(task.getProcessDefinitionId());

		ActivityImpl activityImpl = processDefinitionEntity.findActivity(pi.getActivityId());
		// 获取当前活动完成之后连线的名称
		List<String> nameList = activityImpl.getOutgoingTransitions().stream().map(pvm -> {
			String name = (String) pvm.getProperty("name");
			return StrUtil.isNotBlank(name) ? name : FLAG;
		}).collect(Collectors.toList());
		return nameList;
	}

}
