package com.pig4cloud.pigx.flow.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节点用户对象
 */
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class NodeUser {

	/**
	 * 用户od
	 */
	private Long id;

	/**
	 * 用户名称
	 */
	private String name;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 选择
	 */
	private Boolean selected;

	private String avatar;

}
