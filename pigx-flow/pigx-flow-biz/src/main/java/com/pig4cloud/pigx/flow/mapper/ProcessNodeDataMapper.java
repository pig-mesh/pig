package com.pig4cloud.pigx.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.flow.entity.ProcessNodeData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程节点数据访问接口
 * <p>
 * 负责流程节点数据表(wflow_process_node_data)的数据访问操作
 * 管理流程各节点的表单数据，存储流程执行过程中各节点产生的业务数据
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Mapper
public interface ProcessNodeDataMapper extends BaseMapper<ProcessNodeData> {

}
