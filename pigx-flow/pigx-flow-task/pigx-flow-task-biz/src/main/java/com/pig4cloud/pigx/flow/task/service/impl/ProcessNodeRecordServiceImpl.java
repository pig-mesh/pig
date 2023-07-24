package com.pig4cloud.pigx.flow.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.task.dto.ProcessNodeRecordParamDto;
import com.pig4cloud.pigx.flow.task.entity.ProcessNodeRecord;
import com.pig4cloud.pigx.flow.task.mapper.ProcessNodeRecordMapper;
import com.pig4cloud.pigx.flow.task.service.IProcessNodeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 流程节点记录 服务实现类
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
	 * @param processNodeRecordParamDto
	 * @return
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
	 * @param processNodeRecordParamDto
	 * @return
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
