package com.pig4cloud.pigx.flow.task.constant;

import lombok.Getter;

/**
 * 节点枚举
 */
@Getter
public enum NodeTypeEnum {

	ROOT("根节点", 0),

	APPROVAL("审批节点", 1), CC("抄送节点", 2), EXCLUSIVE_GATEWAY("条件分支", 4), PARALLEL_GATEWAY("并行分支", 5), EMPTY("空", 3)

	;

	NodeTypeEnum(String name, Integer value) {

		this.name = name;
		this.value = value;
	}

	private String name;

	private Integer value;

}
