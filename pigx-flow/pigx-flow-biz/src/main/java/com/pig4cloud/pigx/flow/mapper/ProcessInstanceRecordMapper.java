package com.pig4cloud.pigx.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.flow.entity.ProcessInstanceRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程实例记录数据访问接口
 * <p>
 * 负责流程实例记录表(wflow_process_instance_record)的数据访问操作
 * 管理流程实例的执行记录，包括流程实例的创建、状态变更、完成等信息
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Mapper
public interface ProcessInstanceRecordMapper extends BaseMapper<ProcessInstanceRecord> {

}
