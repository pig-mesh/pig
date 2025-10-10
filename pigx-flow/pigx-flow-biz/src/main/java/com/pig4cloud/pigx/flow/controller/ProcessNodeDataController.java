package com.pig4cloud.pigx.flow.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.ProcessNodeDataDto;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 流程节点数据管理控制器
 * <p>
 * 该控制器负责管理流程节点的配置数据，包括节点的保存和查询操作。
 * 流程节点数据是流程定义的核心组成部分，定义了每个节点的属性、
 * 审批人员、条件规则、表单权限等关键信息。
 * </p>
 * <p>
 * 节点数据的主要内容：
 * 1. 节点基本信息 - 节点类型、名称、描述等
 * 2. 人员配置 - 审批人、抄送人的分配规则
 * 3. 条件配置 - 分支条件、跳转规则等
 * 4. 表单权限 - 节点对表单字段的查看、编辑权限
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/processNodeData")
public class ProcessNodeDataController {

	private final IProcessNodeDataService processNodeDataService;

	/**
	 * 保存或更新流程节点数据
	 * <p>
	 * 保存流程中某个节点的配置信息。如果节点已存在则更新，
	 * 不存在则新增。该接口通常在流程设计器中保存节点配置时调用。
	 * </p>
	 *
	 * @param processNodeDataDto 节点数据对象，包含：
	 *                           - flowId: 流程定义ID
	 *                           - nodeId: 节点ID
	 *                           - nodeData: 节点配置数据（JSON格式）
	 * @return R 统一响应对象，保存成功返回节点数据ID
	 */
	@PostMapping("saveNodeData")
	public R saveNodeData(@RequestBody ProcessNodeDataDto processNodeDataDto) {
		return processNodeDataService.saveNodeData(processNodeDataDto);
	}

	/**
	 * 获取指定节点的配置数据
	 * <p>
	 * 查询流程中特定节点的详细配置信息。返回的数据包含节点的
	 * 所有配置项，如审批人规则、条件设置、表单权限等。
	 * 主要用于流程设计器加载节点配置或流程执行时获取节点规则。
	 * </p>
	 *
	 * @param flowId 流程定义ID
	 * @param nodeId 节点ID
	 * @return R<Node> 统一响应对象，包含节点的完整配置信息
	 */
	@GetMapping("getNodeData")
	public R<Node> getNodeData(String flowId, String nodeId) {
		return processNodeDataService.getNodeData(flowId, nodeId);
	}

}
