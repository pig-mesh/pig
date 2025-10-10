package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.dto.ProcessNodeRecordAssignUserParamDto;
import com.pig4cloud.pigx.flow.entity.ProcessNodeRecordAssignUser;
import com.pig4cloud.pigx.flow.mapper.ProcessNodeRecordAssignUserMapper;
import com.pig4cloud.pigx.flow.service.IProcessNodeRecordAssignUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 流程节点记录-执行人服务实现类
 * <p>
 * 该类负责管理流程节点的执行人记录，主要功能包括：
 * 1. 记录每个节点的执行人分配情况
 * 2. 跟踪执行人的任务处理状态（进行中、已完成）
 * 3. 保存执行人的审批意见和处理时间
 * 4. 处理任务完成事件，更新执行记录
 * 
 * 每个流程节点可能有多个执行人（如会签），该服务记录每个执行人的处理情况
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
	 * <p>
	 * 为流程节点分配执行人，主要处理逻辑：
	 * 1. 如果是带审批意见的操作（如委托、转办），更新上一条记录的审批意见和状态
	 * 2. 创建新的执行人记录，状态为"进行中"
	 * 3. 记录任务开始时间
	 * 
	 * 该方法支持任务的转办、委托等场景
	 * </p>
	 * 
	 * @param processNodeRecordAssignUserParamDto 执行人参数，包含：
	 *                                           - taskId: 任务ID
	 *                                           - userId: 执行人ID
	 *                                           - approveDesc: 审批意见（可选）
	 *                                           - taskType: 任务类型（转办、委托等）
	 * @return R 操作结果
	 */
	@Override
	public R addAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
		// 如果有审批意见，说明是转办、委托等操作，需要更新上一条记录
		if (StrUtil.isNotBlank(processNodeRecordAssignUserParamDto.getApproveDesc())) {
			ProcessNodeRecordAssignUser processNodeRecordAssignUser = this.lambdaQuery()
				.eq(ProcessNodeRecordAssignUser::getTaskId, processNodeRecordAssignUserParamDto.getTaskId())
				.orderByDesc(ProcessNodeRecordAssignUser::getId)
				.last("limit 1")
				.one();
			if (processNodeRecordAssignUser != null) {
				// 更新上一个执行人的记录为已完成
				processNodeRecordAssignUser.setApproveDesc(processNodeRecordAssignUserParamDto.getApproveDesc());
				processNodeRecordAssignUser.setTaskType(processNodeRecordAssignUserParamDto.getTaskType());
				processNodeRecordAssignUser.setStatus(NodeStatusEnum.YJS.getCode());
				processNodeRecordAssignUser.setEndTime(LocalDateTime.now());
				this.updateById(processNodeRecordAssignUser);
			}

		}

		// 创建新的执行人记录
		ProcessNodeRecordAssignUser processNodeRecordAssignUser = BeanUtil
			.copyProperties(processNodeRecordAssignUserParamDto, ProcessNodeRecordAssignUser.class);
		processNodeRecordAssignUser.setStartTime(LocalDateTime.now());
		processNodeRecordAssignUser.setStatus(NodeStatusEnum.JXZ.getCode());  // 进行中状态
		processNodeRecordAssignUser.setApproveDesc("");
		processNodeRecordAssignUser.setTaskType("");
		this.save(processNodeRecordAssignUser);

		return R.ok();
	}

	/**
	 * 任务完成通知
	 * <p>
	 * 处理任务完成事件，更新执行人记录的状态。主要功能：
	 * 1. 根据任务ID和用户ID查找进行中的执行记录
	 * 2. 更新记录状态为已完成
	 * 3. 保存审批意见、结束时间和任务数据
	 * 
	 * 该方法在任务完成时被调用，记录任务的完成信息
	 * </p>
	 * 
	 * @param processNodeRecordAssignUserParamDto 任务完成参数，包含：
	 *                                           - taskId: 任务ID
	 *                                           - userId: 执行人ID
	 *                                           - approveDesc: 审批意见
	 *                                           - data: 任务数据（JSON格式）
	 *                                           - localData: 本地任务数据
	 * @return R 操作结果
	 */
	@Override
	public R completeTaskEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
		// 查找进行中的执行人记录
		ProcessNodeRecordAssignUser processNodeRecordAssignUser = this.lambdaQuery()
			.eq(ProcessNodeRecordAssignUser::getTaskId, processNodeRecordAssignUserParamDto.getTaskId())
			.eq(ProcessNodeRecordAssignUser::getUserId, processNodeRecordAssignUserParamDto.getUserId())
			.eq(ProcessNodeRecordAssignUser::getProcessInstanceId,
					processNodeRecordAssignUserParamDto.getProcessInstanceId())
			.eq(ProcessNodeRecordAssignUser::getStatus, NodeStatusEnum.JXZ.getCode())
			.orderByDesc(ProcessNodeRecordAssignUser::getId)
			.last("limit 1")
			.one();
		
		// 更新记录为已完成状态
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
