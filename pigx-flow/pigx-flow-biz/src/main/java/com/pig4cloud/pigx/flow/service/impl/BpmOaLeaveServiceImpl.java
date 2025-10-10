package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.api.feign.RemoteFlowApiFlowService;
import com.pig4cloud.pigx.flow.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.dto.ProcessInstanceParamDto;
import com.pig4cloud.pigx.flow.entity.BpmOaLeaveEntity;
import com.pig4cloud.pigx.flow.mapper.BpmOaLeaveMapper;
import com.pig4cloud.pigx.flow.service.BpmOaLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 请假表单
 *
 * @author pigx
 * @date 2025-08-08 09:32:41
 */
@Service
@RequiredArgsConstructor
public class BpmOaLeaveServiceImpl extends ServiceImpl<BpmOaLeaveMapper, BpmOaLeaveEntity>
		implements BpmOaLeaveService {

	private final RemoteFlowApiFlowService flowApiFlowService;

	/**
	 * 获取监听的流程定义Key
	 * @return 监听的流程定义Key
	 */
	@Override
	public String getFlowId() {
		return "LEAVE";
	}

	/**
	 * 处理状态事件
	 * @param paramDto 流程节点记录分配用户参数DTO
	 */
	@Override
	public void handleStatusEvent(ProcessInstanceParamDto paramDto) {
		this.update(Wrappers.<BpmOaLeaveEntity>lambdaUpdate()
			.set(BpmOaLeaveEntity::getLeaveStatus, paramDto.getStatus())
			.eq(BpmOaLeaveEntity::getProcessInstanceId, paramDto.getProcessInstanceId()));
	}

	/**
	 * 保存并启动请假流程
	 * @param bpmOaLeave 请假实体对象
	 * @return 保存后的请假实体对象
	 */
	@Override
	public BpmOaLeaveEntity saveAndStartProcess(BpmOaLeaveEntity bpmOaLeave) {
		bpmOaLeave.setUsername(SecurityUtils.getUser().getUsername());
		baseMapper.insert(bpmOaLeave);
		// 获取流程配置详情
		ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
		processInstanceParamDto.setFlowId(this.getFlowId());
		processInstanceParamDto.setStartUserId(String.valueOf(SecurityUtils.getUser().getId()));

		Map<String, Object> paramMap = new HashMap<>();
		// 设置发起人
		Dict rootUser = Dict.create()
			.set("id", processInstanceParamDto.getStartUserId())
			.set("name", SecurityUtils.getUser().getUsername())
			.set("type", NodeUserTypeEnum.USER.getKey());
		paramMap.put("root", CollUtil.newArrayList(rootUser));
		processInstanceParamDto.setParamMap(paramMap);

		R<String> stringR = flowApiFlowService.startProcessInstance(processInstanceParamDto);
		bpmOaLeave.setProcessInstanceId(stringR.getData());
		baseMapper.updateById(bpmOaLeave);
		return bpmOaLeave;
	}

}
