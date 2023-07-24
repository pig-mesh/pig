package com.pig4cloud.pigx.flow.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.task.dto.ProcessNodeRecordAssignUserParamDto;
import com.pig4cloud.pigx.flow.task.entity.ProcessNodeRecordAssignUser;
import com.pig4cloud.pigx.flow.task.mapper.ProcessNodeRecordAssignUserMapper;
import com.pig4cloud.pigx.flow.task.service.IProcessNodeRecordAssignUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 流程节点记录-执行人 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Service
public class ProcessNodeRecordAssignUserServiceImpl
		extends ServiceImpl<ProcessNodeRecordAssignUserMapper, ProcessNodeRecordAssignUser>
		implements IProcessNodeRecordAssignUserService {

	/**
	 * 设置执行人
	 * @param processNodeRecordAssignUserParamDto
	 * @return
	 */
	@Override
	public R addAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
		if (StrUtil.isNotBlank(processNodeRecordAssignUserParamDto.getApproveDesc())) {
			ProcessNodeRecordAssignUser processNodeRecordAssignUser = this.lambdaQuery()
				.eq(ProcessNodeRecordAssignUser::getTaskId, processNodeRecordAssignUserParamDto.getTaskId())
				.orderByDesc(ProcessNodeRecordAssignUser::getId)
				.last("limit 1")
				.one();
			if (processNodeRecordAssignUser != null) {
				processNodeRecordAssignUser.setApproveDesc(processNodeRecordAssignUserParamDto.getApproveDesc());
				processNodeRecordAssignUser.setTaskType(processNodeRecordAssignUserParamDto.getTaskType());
				processNodeRecordAssignUser.setStatus(NodeStatusEnum.YJS.getCode());
				processNodeRecordAssignUser.setEndTime(LocalDateTime.now());
				this.updateById(processNodeRecordAssignUser);
			}

		}

		ProcessNodeRecordAssignUser processNodeRecordAssignUser = BeanUtil
			.copyProperties(processNodeRecordAssignUserParamDto, ProcessNodeRecordAssignUser.class);
		processNodeRecordAssignUser.setStartTime(LocalDateTime.now());
		processNodeRecordAssignUser.setStatus(NodeStatusEnum.JXZ.getCode());
		processNodeRecordAssignUser.setApproveDesc("");
		processNodeRecordAssignUser.setTaskType("");
		this.save(processNodeRecordAssignUser);

		return R.ok();
	}

	/**
	 * 任务完成通知
	 * @param processNodeRecordAssignUserParamDto
	 * @return
	 */
	@Override
	public R completeTaskEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
		ProcessNodeRecordAssignUser processNodeRecordAssignUser = this.lambdaQuery()
			.eq(ProcessNodeRecordAssignUser::getTaskId, processNodeRecordAssignUserParamDto.getTaskId())
			.eq(ProcessNodeRecordAssignUser::getUserId, processNodeRecordAssignUserParamDto.getUserId())
			.eq(ProcessNodeRecordAssignUser::getProcessInstanceId,
					processNodeRecordAssignUserParamDto.getProcessInstanceId())
			.eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.JXZ.getCode())
			.orderByDesc(ProcessNodeRecordAssignUser::getId)
			.last("limit 1")
			.one();
		processNodeRecordAssignUser.setStatus(NodeStatusEnum.YJS.getCode());
		processNodeRecordAssignUser.setApproveDesc(processNodeRecordAssignUserParamDto.getApproveDesc());
		processNodeRecordAssignUser.setEndTime(LocalDateTime.now());
		processNodeRecordAssignUser.setData(processNodeRecordAssignUserParamDto.getData());
		processNodeRecordAssignUser.setLocalData(processNodeRecordAssignUserParamDto.getLocalData());
		processNodeRecordAssignUser.setTaskType("COMPLETE");
		this.updateById(processNodeRecordAssignUser);
		return R.ok();
	}

}
