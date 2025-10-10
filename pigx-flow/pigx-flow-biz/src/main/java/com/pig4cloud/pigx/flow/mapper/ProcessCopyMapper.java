package com.pig4cloud.pigx.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pigx.flow.entity.ProcessCopy;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程抄送数据访问接口
 * <p>
 * 负责流程抄送表(wflow_process_copy)的数据访问操作
 * 管理流程实例的抄送记录，包括抄送人、抄送时间、已读状态等信息
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
@Mapper
public interface ProcessCopyMapper extends BaseMapper<ProcessCopy> {

}
