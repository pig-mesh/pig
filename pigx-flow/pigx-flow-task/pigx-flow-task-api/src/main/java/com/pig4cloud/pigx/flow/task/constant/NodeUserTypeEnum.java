package com.pig4cloud.pigx.flow.task.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户节点类型枚举
 */
@Getter
@AllArgsConstructor
public enum NodeUserTypeEnum {

	USER("user", "用户"), DEPT("dept", "部门"), ROLE("role", "角色"),;

	private String key;

	private String name;

}
