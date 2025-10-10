package com.pig4cloud.pigx.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.flow.entity.ProcessStarter;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程发起人数据访问接口
 * <p>
 * 负责流程发起人表(wflow_process_starter)的数据访问操作
 * 管理流程的发起权限配置，定义哪些用户、角色或部门可以发起特定流程
 * </p>
 *
 * @author Vincent
 * @since 2023-05-30
 */
@Mapper
public interface ProcessStarterMapper extends BaseMapper<ProcessStarter> {

}
