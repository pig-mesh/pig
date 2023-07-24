package com.pig4cloud.pigx.flow.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.ProcessNodeRecordParamDto;
import com.pig4cloud.pigx.flow.task.entity.ProcessNodeRecord;

/**
 * <p>
 * 流程节点记录 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
public interface IProcessNodeRecordService extends IService<ProcessNodeRecord> {

	/**
	 * 节点开始
	 * @param processNodeRecordParamDto
	 * @return
	 */
	R start(ProcessNodeRecordParamDto processNodeRecordParamDto);

	/**
	 * 节点结束
	 * @param processNodeRecordParamDto
	 * @return
	 */
	R complete(ProcessNodeRecordParamDto processNodeRecordParamDto);

}
