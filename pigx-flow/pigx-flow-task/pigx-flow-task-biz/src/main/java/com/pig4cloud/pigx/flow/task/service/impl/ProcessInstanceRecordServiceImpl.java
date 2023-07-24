package com.pig4cloud.pigx.flow.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.flow.task.entity.ProcessInstanceRecord;
import com.pig4cloud.pigx.flow.task.mapper.ProcessInstanceRecordMapper;
import com.pig4cloud.pigx.flow.task.service.IProcessInstanceRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流程记录 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Service
public class ProcessInstanceRecordServiceImpl extends ServiceImpl<ProcessInstanceRecordMapper, ProcessInstanceRecord>
		implements IProcessInstanceRecordService {

}
