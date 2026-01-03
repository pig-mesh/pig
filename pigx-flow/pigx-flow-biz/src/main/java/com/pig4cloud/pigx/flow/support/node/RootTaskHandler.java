package com.pig4cloud.pigx.flow.support.node;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 发起人任务处理器
 * <p>
 * 用于处理驳回到发起人场景下，发起人任务的执行人解析。
 * 该处理器作为Spring Bean注入，在BPMN模型中通过EL表达式调用。
 * </p>
 * <p>
 * 使用方式：
 * 在UserTask的assignee属性中配置：${rootTaskHandler.resolveAssignee(execution)}
 * </p>
 *
 * @author pigx
 */
@Component("rootTaskHandler")
@Slf4j
public class RootTaskHandler {

	/**
	 * 解析发起人作为任务执行人
	 * <p>
	 * 从流程变量中获取发起人信息（root变量），返回发起人的用户ID作为任务执行人。
	 * 该方法在任务创建时由Flowable引擎通过EL表达式调用。
	 * </p>
	 *
	 * @param execution 流程执行上下文，包含流程变量等信息
	 * @return 发起人用户ID字符串，如果未找到则返回默认空执行人标识
	 */
	public String resolveAssignee(DelegateExecution execution) {
		// 从流程变量中获取发起人信息
		Object rootUserObj = execution.getVariable("root");
		if (rootUserObj == null) {
			log.warn("未找到发起人信息，流程实例ID: {}", execution.getProcessInstanceId());
			return ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN.toString();
		}

		ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
		try {
			// 发起人信息存储为List<NodeUser>格式
			List<NodeUser> nodeUsers = objectMapper.readValue(objectMapper.writeValueAsString(rootUserObj),
					new TypeReference<List<NodeUser>>() {
					});
			if (!nodeUsers.isEmpty()) {
				Long userId = nodeUsers.get(0).getId();
				log.debug("解析发起人成功，用户ID: {}", userId);
				return userId.toString();
			}
		}
		catch (Exception e) {
			log.error("解析发起人信息失败，流程实例ID: {}", execution.getProcessInstanceId(), e);
		}
		return ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN.toString();
	}

}
