package com.pig4cloud.pigx.flow.engine.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.api.feign.RemoteFlowTaskService;
import com.pig4cloud.pigx.flow.task.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;
import com.pig4cloud.pigx.flow.task.dto.ProcessCopyDto;
import lombok.SneakyThrows;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 抄送任务处理器--java服务任务
 */
public class CopyServiceTask implements JavaDelegate {

	/**
	 * 执行给定执行的任务。
	 * @param execution 要处理的执行
	 */
	@SneakyThrows
	@Override
	public void execute(DelegateExecution execution) {

		ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
		String nodeId = entity.getActivityId();
		String flowId = entity.getProcessDefinitionKey();

		RemoteFlowTaskService remoteFlowTaskService = SpringUtil.getBean(RemoteFlowTaskService.class);
		Node node = remoteFlowTaskService.queryNodeOriData(flowId, nodeId).getData();

		// 获取指定人员
		List<NodeUser> userDtoList = node.getNodeUserList();
		// 用户ID列表
		List<String> userIdList = userDtoList.stream()
			.filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey()))
			.map(w -> Convert.toStr(w.getId()))
			.collect(Collectors.toList());
		// 部门ID列表
		List<String> deptIdList = userDtoList.stream()
			.filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey()))
			.map(w -> Convert.toStr(w.getId()))
			.collect(Collectors.toList());

		if (CollUtil.isNotEmpty(deptIdList)) {

			R<List<String>> r = remoteFlowTaskService.queryUserIdListByDepIdList(deptIdList);

			List<String> data = r.getData();
			if (CollUtil.isNotEmpty(data)) {
				for (String datum : data) {
					if (!userIdList.contains(datum)) {
						userIdList.add(datum);
					}
				}
			}
		}

		ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
		// 获取发起人
		Object rootUserObj = execution.getVariable("root");
		NodeUser rootUser = objectMapper
			.readValue(objectMapper.writeValueAsString(rootUserObj), new TypeReference<List<NodeUser>>() {
			})
			.get(0);

		Map<String, Object> variables = execution.getVariables();

		for (String userIds : userIdList) {
			// 发送抄送任务
			ProcessCopyDto processCopyDto = new ProcessCopyDto();
			processCopyDto.setNodeTime(LocalDateTime.now());
			processCopyDto.setStartUserId(rootUser.getId());
			processCopyDto.setFlowId(flowId);
			processCopyDto.setProcessInstanceId(execution.getProcessInstanceId());
			processCopyDto.setNodeId(nodeId);
			processCopyDto.setNodeName(node.getName());
			processCopyDto.setFormData(objectMapper.writeValueAsString(variables));
			processCopyDto.setUserId(Long.parseLong(userIds));
			remoteFlowTaskService.saveCC(processCopyDto);
		}

	}

}
