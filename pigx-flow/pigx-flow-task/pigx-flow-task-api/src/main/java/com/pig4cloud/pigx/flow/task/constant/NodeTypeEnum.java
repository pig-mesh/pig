package com.pig4cloud.pigx.flow.task.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * 节点枚举
 */
@Getter
public enum NodeTypeEnum {

	ROOT("根节点", 0, false), END("结束节点", -1, false),

	APPROVAL("审批节点", 1, false), CC("抄送节点", 2, false), EXCLUSIVE_GATEWAY("条件分支", 4, true),

	PARALLEL_GATEWAY("并行分支", 5, true), EMPTY("空", 3, false),

	;

	public static NodeTypeEnum getByValue(int value) {
		return Arrays.stream(NodeTypeEnum.values()).filter(w -> w.getValue() == value).findAny().orElse(null);
	}

	NodeTypeEnum(String name, Integer value, Boolean branch) {
		this.name = name;
		this.value = value;
		this.branch = branch;
	}

	private String name;

	private Integer value;

	private Boolean branch;

}
