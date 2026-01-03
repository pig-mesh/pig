package com.pig4cloud.pigx.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.entity.Process;
import com.pig4cloud.pigx.flow.vo.ProcessVO;

/**
 * 流程定义服务接口
 * <p>
 * 提供流程定义的创建、编辑、查询、隐藏等核心功能。
 * 该服务是流程管理的核心服务，负责管理流程的生命周期。
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
public interface IProcessService extends IService<Process> {

	/**
	 * 获取流程详细信息
	 * 包括流程基本信息、表单配置、流程配置、权限信息等完整数据
	 * 
	 * @param flowId 流程定义ID
	 * @return 流程详细信息VO对象
	 */
	R<ProcessVO> getDetail(String flowId);

	/**
	 * 根据流程ID获取流程定义实体
	 * 
	 * @param flowId 流程定义ID
	 * @return 流程定义实体
	 */
	Process getByFlowId(String flowId);

	/**
	 * 根据流程ID更新流程定义
	 * 用于更新流程的表单配置、流程配置等信息
	 * 
	 * @param process 包含更新信息的流程实体
	 */
	void updateByFlowId(Process process);

	/**
	 * 隐藏流程定义
	 * 隐藏的流程不在列表中显示，但已创建的实例仍可继续执行
	 * 
	 * @param flowId 流程定义ID
	 */
	void hide(String flowId);

	/**
	 * 创建新的流程定义
	 * 创建流程时会同时生成流程的唯一标识和默认配置
	 * 
	 * @param process 流程定义实体
	 * @return 创建结果，包含生成的flowId
	 */
	R create(Process process);

	/**
	 * 更新流程状态或分组
	 *
	 * @param flowId 流程定义ID
	 * @param type 操作类型：stop（停用）、using（启用）、delete（删除）
	 * @param groupId 流程分组ID（可选，用于修改流程分组）
	 * @return 操作结果
	 */
	R update(String flowId, String type, Long groupId);

	/**
	 * 验证流程ID是否在所有IProcessInstanceStatusEventService实现类中存在
	 *
	 * @param flowId 流程定义ID
	 * @return 验证结果：true表示存在，false表示不存在
	 */
	R<Boolean> validateFlowId(String flowId);

}
