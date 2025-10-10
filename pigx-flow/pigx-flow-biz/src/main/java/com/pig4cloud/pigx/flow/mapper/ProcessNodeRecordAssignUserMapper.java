package com.pig4cloud.pigx.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.flow.entity.ProcessNodeRecordAssignUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程节点执行人记录数据访问接口
 * <p>
 * 负责流程节点执行人记录表(wflow_process_node_record_assign_user)的数据访问操作
 * 管理流程节点的执行人分配记录，包括待办人员、实际执行人、执行状态等信息
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Mapper
public interface ProcessNodeRecordAssignUserMapper extends BaseMapper<ProcessNodeRecordAssignUser> {

}
