package com.pig4cloud.pigx.flow.support.listeners;

import cn.hutool.core.lang.Validator;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.RetOps;
import com.pig4cloud.pigx.common.security.util.NonWebTokenContextHolder;
import com.pig4cloud.pigx.flow.dto.ProcessInstanceParamDto;
import com.pig4cloud.pigx.flow.dto.ProcessNodeRecordAssignUserParamDto;
import com.pig4cloud.pigx.flow.entity.ProcessInstanceRecord;
import com.pig4cloud.pigx.flow.event.FlowStatusEventPublisher;
import com.pig4cloud.pigx.flow.mapper.ProcessInstanceRecordMapper;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 节点任务完成通知服务
 * <p>
 * 该服务负责在流程节点任务完成时发送异步通知。当节点配置了外部事件回调URL时， 系统会自动调用该URL通知任务完成事件。这种机制允许流程与外部系统进行集成，
 * 实现业务流程的自动化。通知采用异步方式发送，不会阻塞主流程的执行。
 * </p>
 *
 * @author lengleng
 * @date 2026-02-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NodeTaskCompleteNotify {

	private final IProcessNodeDataService processNodeDataService;

	private final ProcessInstanceRecordMapper processInstanceRecordMapper;

	private final FlowStatusEventPublisher flowStatusEventPublisher;

	/**
	 * 异步发送任务完成通知
	 * <p>
	 * 当流程节点任务完成时，如果节点配置了事件通知URL，则通过HTTP POST请求 将任务完成信息发送到指定的URL。通知内容包括任务ID、处理人、处理结果等
	 * 完整信息。该方法使用异步执行，确保不影响主流程的性能。通知失败时会 记录日志但不会影响流程执行。
	 * </p>
	 * @param token 安全令牌，用于在异步线程中传递认证信息
	 * @param taskCompleteParamDto 任务完成参数，包含： - flowId: 流程定义ID - nodeId: 节点ID - taskId:
	 * 任务ID - userId: 处理人ID - result: 处理结果 - comment: 处理意见 - completeTime: 完成时间 -
	 * variables: 流程变量
	 */
	@Async
	public void sendNotify(String token, ProcessNodeRecordAssignUserParamDto taskCompleteParamDto) {
		// 在异步线程中设置认证令牌
		NonWebTokenContextHolder.setToken(token);

		// 获取节点配置并发送通知
		RetOps
			.of(processNodeDataService.getNodeData(taskCompleteParamDto.getFlowId(), taskCompleteParamDto.getNodeId()))
			.getData()
			.ifPresent(node -> {
				// 验证事件配置是否为有效的URL
				if (Validator.isUrl(node.getEventConfig())) {
					// 发送HTTP POST请求到配置的URL
					HttpResponse response = HttpRequest.post(node.getEventConfig())
						.body(JSONUtil.toJsonStr(taskCompleteParamDto))
						.timeout(10000) // 设置10秒超时
						.execute();
					log.debug("节点任务完成通知处理完成:{}，数据：{}", response.getStatus(), taskCompleteParamDto);
				}
			});
	}

	/**
	 * 异步发送流程实例状态变更通知
	 * <p>
	 * 当流程实例状态发生变更时，查询最新的流程实例记录状态，
	 * 并通过Redis发布流程状态变更事件，供订阅方消费处理。
	 * </p>
	 * @param token 安全令牌，用于在异步线程中传递认证信息
	 * @param instanceRecordParamDto 流程实例参数，包含流程实例ID等信息
	 */
	public void sendFlowNotify(String token, ProcessInstanceParamDto instanceRecordParamDto) {
		// 在异步线程中设置认证令牌
		NonWebTokenContextHolder.setToken(token);

		// 查询最新状态
		ProcessInstanceRecord processInstanceRecord = processInstanceRecordMapper
			.selectOne(Wrappers.<ProcessInstanceRecord>lambdaQuery()
				.eq(ProcessInstanceRecord::getProcessInstanceId, instanceRecordParamDto.getProcessInstanceId()));

		instanceRecordParamDto.setStatus(processInstanceRecord.getStatus());
		instanceRecordParamDto.setFinishReason(processInstanceRecord.getFinishReason());

		// 发布到 Redis
		flowStatusEventPublisher.publish(instanceRecordParamDto);
	}

}
