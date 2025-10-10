package com.pig4cloud.pigx.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.flow.entity.ProcessGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程分组数据访问接口
 * <p>
 * 负责流程分组表(wflow_process_group)的数据访问操作
 * 管理流程的分类分组信息，支持流程的分类展示和管理
 * </p>
 *
 * @author Vincent
 * @since 2023-05-25
 */
@Mapper
public interface ProcessGroupMapper extends BaseMapper<ProcessGroup> {

}
