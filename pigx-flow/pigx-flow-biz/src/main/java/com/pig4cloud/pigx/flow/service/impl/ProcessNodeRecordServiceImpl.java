package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.dto.ProcessNodeRecordParamDto;
import com.pig4cloud.pigx.flow.entity.ProcessNodeRecord;
import com.pig4cloud.pigx.flow.mapper.ProcessNodeRecordMapper;
import com.pig4cloud.pigx.flow.service.IProcessNodeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 流程节点记录服务实现类
 * <p>
 * 该类负责管理流程节点的执行记录，主要功能包括：
 * 1. 记录流程节点的开始和结束时间
 * 2. 跟踪节点的执行状态（进行中、已完成）
 * 3. 保存节点执行的历史记录，用于流程追踪
 * 
 * 每个流程实例的每个节点都会生成一条记录，记录节点的执行情况
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Slf4j
@Service
public class ProcessNodeRecordServiceImpl extends ServiceImpl<ProcessNodeRecordMapper, ProcessNodeRecord>
		implements IProcessNodeRecordService {

	/**
	 * 节点开始
	 * <p>
	 * 记录流程节点的开始执行。当流程执行到某个节点时，
	 * 创建该节点的执行记录，设置开始时间和进行中状态
	 * </p>
	 * 
	 * @param processNodeRecordParamDto 节点记录参数，包含：
	 *                                 - processInstanceId: 流程实例ID
	 *                                 - nodeId: 节点ID
	 *                                 - nodeName: 节点名称
	 *                                 - nodeType: 节点类型
	 * @return R 操作结果
	 */
	@Override
	public R start(ProcessNodeRecordParamDto processNodeRecordParamDto) {

		ProcessNodeRecord processNodeRecord = BeanUtil.copyProperties(processNodeRecordParamDto,
				ProcessNodeRecord.class);
		processNodeRecord.setStartTime(new Date());
		processNodeRecord.setStatus(NodeStatusEnum.JXZ.getCode());

		this.save(processNodeRecord);
		return R.ok();
	}

	/**
	 * 节点结束
	 * <p>
	 * 记录流程节点的结束执行。当节点上的所有任务都完成后，
	 * 更新节点记录的状态为已完成，并设置结束时间
	 * </p>
	 * 
	 * @param processNodeRecordParamDto 节点记录参数，包含：
	 *                                 - processInstanceId: 流程实例ID
	 *                                 - nodeId: 节点ID
	 * @return R 操作结果
	 */
	@Override
	public R complete(ProcessNodeRecordParamDto processNodeRecordParamDto) {

		log.info("节点结束---{}", processNodeRecordParamDto);

		// TODO 完成节点和完成任务要区分下
		this.lambdaUpdate()
			.set(ProcessNodeRecord::getStatus, NodeStatusEnum.YJS.getCode())
			.set(ProcessNodeRecord::getEndTime, new Date())
			.eq(ProcessNodeRecord::getProcessInstanceId, processNodeRecordParamDto.getProcessInstanceId())
			.eq(ProcessNodeRecord::getNodeId, processNodeRecordParamDto.getNodeId())
			.update(new ProcessNodeRecord());
		return R.ok();
	}

}
