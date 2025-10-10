package com.pig4cloud.pigx.flow.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用户节点类型枚举
 */
@Getter
@RequiredArgsConstructor
public enum NodeUserTypeEnum {

	USER("user", "用户"), DEPT("dept", "部门"), ROLE("role", "角色"), POST("post", "岗位");

	private final String key;

	private final String name;

}
