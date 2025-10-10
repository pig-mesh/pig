package com.pig4cloud.pigx.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.flow.entity.ProcessNodeRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程节点记录数据访问接口
 * <p>
 * 负责流程节点记录表(wflow_process_node_record)的数据访问操作
 * 管理流程执行过程中各节点的执行记录，包括节点的开始、结束时间、执行状态、审批意见等
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Mapper
public interface ProcessNodeRecordMapper extends BaseMapper<ProcessNodeRecord> {

}
