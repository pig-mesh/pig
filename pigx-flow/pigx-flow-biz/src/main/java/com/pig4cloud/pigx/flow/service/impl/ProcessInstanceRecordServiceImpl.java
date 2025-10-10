package com.pig4cloud.pigx.flow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.flow.entity.ProcessInstanceRecord;
import com.pig4cloud.pigx.flow.mapper.ProcessInstanceRecordMapper;
import com.pig4cloud.pigx.flow.service.IProcessInstanceRecordService;
import org.springframework.stereotype.Service;

/**
 * 流程实例记录服务实现类
 * <p>
 * 该类负责管理流程实例的历史记录，主要功能包括：
 * 1. 记录流程实例的创建、执行、完成等状态变化
 * 2. 保存流程实例的快照信息，包括流程变量、表单数据等
 * 3. 提供流程实例历史数据的查询功能
 * 4. 支持流程追踪和审计需求
 * 
 * 该服务是流程执行历史的重要存储，用于流程回溯、统计分析等场景
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Service
public class ProcessInstanceRecordServiceImpl extends ServiceImpl<ProcessInstanceRecordMapper, ProcessInstanceRecord>
		implements IProcessInstanceRecordService {

}
