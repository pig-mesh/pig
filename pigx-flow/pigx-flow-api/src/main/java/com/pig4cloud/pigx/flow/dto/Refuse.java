package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

/**
 * 拒绝处理策略数据传输对象
 * <p>
 * 该DTO用于定义流程节点被拒绝后的处理策略。
 * 当审批人拒绝某个节点时，系统需要根据预定义的策略决定流程的后续走向，
 * 如回退到指定节点、直接结束流程、转交其他人处理等。
 * </p>
 * 
 * @author pigx
 */
@Data
public class Refuse {

	/**
	 * 拒绝处理策略
	 * <p>
	 * 定义节点被拒绝后的处理方式：
	 * - TO_END: 直接结束流程
	 * - TO_NODE: 回退到指定节点
	 * - TO_BEFORE: 回退到上一个节点
	 * - TO_START: 回退到发起人
	 * - CONTINUE: 继续流程（忽略拒绝）
	 * </p>
	 */
	private String handler;

	/**
	 * 目标节点ID
	 * <p>
	 * 当handler为TO_NODE时，指定要回退到的目标节点ID。
	 * 流程将回退到该节点重新执行。
	 * 如果handler为其他值，此字段可为空。
	 * </p>
	 */
	private String nodeId;

}
