package com.pig4cloud.pigx.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.flow.entity.ProcessInstanceRecord;

/**
 * 流程实例记录服务接口
 * <p>
 * 该服务负责管理流程实例的运行记录，记录每个流程实例从创建到结束的完整生命周期信息。
 * 包括流程的启动时间、结束时间、当前状态、流程变量等核心数据。这些记录是流程审计、
 * 统计分析和问题排查的重要依据。服务提供了对流程实例记录的基础CRUD操作。
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
public interface IProcessInstanceRecordService extends IService<ProcessInstanceRecord> {

}
