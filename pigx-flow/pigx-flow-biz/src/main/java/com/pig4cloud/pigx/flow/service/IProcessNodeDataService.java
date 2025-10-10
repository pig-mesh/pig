package com.pig4cloud.pigx.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.ProcessNodeDataDto;
import com.pig4cloud.pigx.flow.entity.ProcessNodeData;

/**
 * 流程节点数据管理服务接口
 * <p>
 * 该服务负责管理流程定义中每个节点的配置数据，包括节点的属性设置、审批人配置、
 * 表单权限、条件表达式等。这些数据是流程引擎执行时的重要依据，决定了流程节点
 * 的行为方式。服务提供了节点数据的保存和查询功能，支持流程的动态配置。
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
public interface IProcessNodeDataService extends IService<ProcessNodeData> {

	/**
	 * 保存或更新流程节点数据
	 * <p>
	 * 保存流程节点的完整配置信息，包括节点基本属性、审批人设置、表单字段权限、
	 * 条件配置等。如果节点数据已存在则更新，不存在则新增。保存时会进行数据
	 * 验证，确保配置的合法性和完整性。该接口通常在流程设计时调用。
	 * </p>
	 *
	 * @param processNodeDataDto 节点数据传输对象，包含：
	 *                           - flowId: 流程定义ID
	 *                           - nodeId: 节点ID
	 *                           - nodeName: 节点名称
	 *                           - nodeType: 节点类型（审批/抄送/条件等）
	 *                           - nodeUser: 节点处理人配置
	 *                           - formPermission: 表单字段权限配置
	 *                           - conditions: 条件表达式（条件节点使用）
	 *                           - properties: 其他扩展属性
	 * @return R 响应结果，成功返回保存后的节点数据ID，失败返回错误信息
	 */
	R saveNodeData(ProcessNodeDataDto processNodeDataDto);

	/**
	 * 获取指定节点的配置数据
	 * <p>
	 * 根据流程ID和节点ID查询节点的完整配置信息。返回的数据包括节点的所有
	 * 配置项，可直接用于流程引擎的执行或流程设计器的回显。查询时会进行
	 * 权限校验，确保调用者有权限访问该流程的节点数据。
	 * </p>
	 *
	 * @param flowId 流程定义ID，标识节点所属的流程
	 * @param nodeId 节点ID，流程中节点的唯一标识
	 * @return R<Node> 响应结果，包含节点的完整配置信息：
	 *                 - 基本信息（ID、名称、类型等）
	 *                 - 处理人配置（固定人员、角色、部门负责人等）
	 *                 - 表单权限（各字段的可见、可编辑权限）
	 *                 - 条件配置（分支条件、跳转条件等）
	 *                 - 其他属性（超时设置、多实例配置等）
	 */
	R<Node> getNodeData(String flowId, String nodeId);

}
