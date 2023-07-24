package com.pig4cloud.pigx.flow.engine.node;

import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;

import java.util.List;
import java.util.Map;

/**
 * 指定用户策略处理器
 */
public interface AssignUserStrategy {

	/**
	 * 抽象方法 处理表达式
	 * @param node
	 * @param rootUser
	 * @param variables
	 */
	List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables);

}
