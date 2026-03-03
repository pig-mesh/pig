package com.pig4cloud.pigx.flow.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 流程节点类型枚举
 * <p>
 * 定义流程中所有可用的节点类型，每种节点类型在流程中有不同的作用和行为。
 * </p>
 * 
 * @author pigx
 */
@Getter
@RequiredArgsConstructor
public enum NodeTypeEnum {

	/**
	 * 根节点 - 流程的起始点，每个流程必须有且只有一个根节点
	 */
	ROOT("根节点", 0, false),
	
	/**
	 * 结束节点 - 流程的终止点，标识流程执行完成
	 */
	END("结束节点", -1, false),

	/**
	 * 审批节点 - 需要用户进行审批操作的节点，支持单人或多人审批
	 */
	APPROVAL("审批节点", 1, false),
	
	/**
	 * 抄送节点 - 仅用于通知，不阻塞流程执行，抄送人只需查看无需审批
	 */
	CC("抄送节点", 2, false),
	
	/**
	 * 条件分支 - 根据表单数据或表达式判断流程走向，只会选择一条分支执行
	 */
	EXCLUSIVE_GATEWAY("条件分支", 4, true),

	/**
	 * 并行分支 - 同时执行多条分支，所有分支都完成后才能继续后续流程
	 */
	PARALLEL_GATEWAY("并行分支", 5, true),

    /**
     * 延时器节点 - 流程暂停等待指定时间后自动继续
     */
    TIMER("延时器", 10, false),

	/**
	 * 空节点 - 占位节点，用于流程设计时的过渡
	 */
	EMPTY("空", 3, false);

	/**
	 * 节点名称
	 */
	private final String name;

	/**
	 * 节点类型值
	 */
	private final Integer value;

	/**
	 * 是否为分支节点
	 * true=分支节点（如条件分支、并行分支），false=普通节点
	 */
	private final Boolean branch;

	/**
	 * 根据节点类型值获取枚举实例
	 * 
	 * @param value 节点类型值
	 * @return 对应的节点类型枚举，找不到返回null
	 */

	public static NodeTypeEnum getByValue(int value) {
		return Arrays.stream(NodeTypeEnum.values()).filter(w -> w.getValue() == value).findAny().orElse(null);
	}

}
