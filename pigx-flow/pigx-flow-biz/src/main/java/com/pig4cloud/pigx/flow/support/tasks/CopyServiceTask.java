package com.pig4cloud.pigx.flow.support.tasks;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import com.pig4cloud.pigx.flow.dto.ProcessCopyDto;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import com.pig4cloud.pigx.flow.service.IRemoteService;
import lombok.SneakyThrows;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 抄送服务任务
 * <p>
 * Flowable服务任务（Service Task）实现，用于处理流程中的抄送功能。
 * 抄送任务会将流程信息发送给指定的用户或部门成员，供其查看但不需要审批。
 * <p>
 * 主要功能：
 * 1. 获取节点配置的抄送人员（支持用户和部门）
 * 2. 将部门展开为具体的用户列表
 * 3. 为每个抄送人创建抄送记录
 * 4. 记录流程快照信息供抄送人查看
 * <p>
 * 使用场景：
 * - 需要通知相关人员但不需要审批的节点
 * - 流程信息备案和知会
 * - 跨部门协作中的信息同步
 *
 * @author pigx
 */
public class CopyServiceTask implements JavaDelegate {

	/**
	 * 执行抄送任务
	 * <p>
	 * 从节点配置中获取抄送人员列表，创建抄送记录并通过远程服务保存。
	 * 支持直接指定用户或指定部门（会展开为部门下的所有用户）。
	 *
	 * @param execution 流程执行上下文，包含流程实例信息和变量
	 * @throws Exception 当JSON解析或远程服务调用失败时抛出异常
	 */
	@SneakyThrows
	@Override
	public void execute(DelegateExecution execution) {

		ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
		String nodeId = entity.getActivityId();
		String flowId = entity.getProcessDefinitionKey();

		IRemoteService remoteService = SpringUtil.getBean(IRemoteService.class);
		IProcessNodeDataService processNodeDataService = SpringUtil.getBean(IProcessNodeDataService.class);
		Node node = processNodeDataService.getNodeData(flowId, nodeId).getData();

		// 获取指定人员
		List<NodeUser> userDtoList = node.getNodeUserList();
		// 用户ID列表
		List<String> userIdList = userDtoList.stream()
			.filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey()))
			.map(w -> Convert.toStr(w.getId()))
			.collect(Collectors.toList());
		// 部门ID列表
		List<Long> deptIdList = userDtoList.stream()
			.filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey()))
			.map(NodeUser::getId)
			.toList();

		if (CollUtil.isNotEmpty(deptIdList)) {

			R<List<Long>> r = remoteService.queryUserIdListByDepIdList(deptIdList);

			List<Long> data = r.getData();
			if (CollUtil.isNotEmpty(data)) {
				for (Long datum : data) {
					String userIdStr = Convert.toStr(datum);
					if (!userIdList.contains(userIdStr)) {
						userIdList.add(userIdStr);
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
			remoteService.saveCC(processCopyDto);
		}

	}

}
