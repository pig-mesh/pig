package com.pig4cloud.pigx.flow.support.utils;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.flow.constant.NodeTypeEnum;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程节点工具类
 * <p>
 * 提供流程节点的常用操作方法，包括节点遍历、节点查找、节点构建等功能。 主要用于处理流程定义的树形结构，支持条件分支和并行分支的递归处理。
 * </p>
 * 
 * @author pigx
 */
public class NodeUtil {

	/**
	 * 从流程定义ID中提取流程ID
	 * <p>
	 * Flowable引擎的流程定义ID格式为：flowId:version:deploymentId 该方法提取冒号前的flowId部分
	 * </p>
	 * @param processDefinitionId Flowable引擎的流程定义ID
	 * @return 流程ID
	 */
	public static String getFlowId(String processDefinitionId) {
		return StrUtil.subBefore(processDefinitionId, ":", false);
	}

	/**
	 * 判断是否为有效节点
	 * <p>
	 * 有效节点的条件： 1. 节点对象不为null 2. 节点ID不为空
	 * </p>
	 * @param childNode 要判断的节点对象
	 * @return true=有效节点，false=无效节点
	 */
	public static boolean isNode(Node childNode) {
		return childNode != null && StrUtil.isNotBlank(childNode.getId());
	}

	/**
	 * 为流程添加结束节点
	 * <p>
	 * 递归遍历流程节点链，找到最末端节点并添加结束节点。 Flowable引擎要求流程必须有明确的结束节点。
	 * </p>
	 * @param node 流程起始节点
	 */
	public static void addEndNode(Node node) {

		Node children = node.getChildren();
		if (isNode(children)) {
			addEndNode(children);
		}
		else {
			Node end = new Node();
			end.setId("end");
			end.setType(NodeTypeEnum.END.getValue());
			end.setName("结束节点");
			end.setParentId(node.getId());
			node.setChildren(end);
		}

	}

	/**
	 * 查找需要发起人选择执行人的节点
	 * <p>
	 * 递归遍历流程节点，找出所有设置为"发起人自选"类型的审批节点。 这些节点在流程发起时需要发起人手动指定执行人。 支持处理条件分支和并行分支中的节点。
	 * </p>
	 * @param node 要遍历的起始节点
	 * @return 需要发起人选择执行人的节点ID列表
	 */
	public static List<String> selectUserNodeId(Node node) {
		List<String> list = new ArrayList<>();

		if (!isNode(node)) {
			return list;
		}

		Integer type = node.getType();

		if (type == NodeTypeEnum.APPROVAL.getValue().intValue()) {

			Integer assignedType = node.getAssignedType();

			boolean selfSelect = assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT;
			if (selfSelect) {
				list.add(node.getId());
			}
		}

		if (type == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()
				|| type == NodeTypeEnum.PARALLEL_GATEWAY.getValue().intValue()) {

			// 条件分支
			List<Node> branchs = node.getConditionNodes();
			for (Node branch : branchs) {
				Node children = branch.getChildren();
				List<String> strings = selectUserNodeId(children);
				list.addAll(strings);
			}
		}

		List<String> next = selectUserNodeId(node.getChildren());
		list.addAll(next);
		return list;
	}

}
