package com.pig4cloud.pigx.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.flow.entity.Process;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程定义数据访问接口
 * <p>
 * 负责流程定义表(wflow_process)的数据访问操作
 * 包含流程定义的基本信息管理，如流程名称、流程图、表单配置等
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
@Mapper
public interface ProcessMapper extends BaseMapper<Process> {

}
