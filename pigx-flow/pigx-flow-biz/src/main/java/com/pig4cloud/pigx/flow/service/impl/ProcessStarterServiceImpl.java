package com.pig4cloud.pigx.flow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.flow.entity.ProcessStarter;
import com.pig4cloud.pigx.flow.mapper.ProcessStarterMapper;
import com.pig4cloud.pigx.flow.service.IProcessStarterService;
import org.springframework.stereotype.Service;

/**
 * 流程发起人服务实现类
 * <p>
 * 该类负责管理流程的发起人权限配置，主要功能包括：
 * 1. 配置哪些用户可以发起特定流程
 * 2. 配置哪些部门可以发起特定流程
 * 3. 支持按用户、部门等不同维度设置发起权限
 * 4. 为流程列表的权限过滤提供数据支持
 * 
 * 通过该服务，可以精确控制流程的发起权限，确保只有授权的用户才能发起相应的流程
 * </p>
 *
 * @author Vincent
 * @since 2023-05-30
 */
@Service
public class ProcessStarterServiceImpl extends ServiceImpl<ProcessStarterMapper, ProcessStarter>
		implements IProcessStarterService {

}
