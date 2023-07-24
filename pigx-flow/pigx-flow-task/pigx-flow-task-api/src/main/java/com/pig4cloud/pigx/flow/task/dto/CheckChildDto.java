package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

import java.util.List;

/**
 * 检查子部门DTO
 */
@Data
public class CheckChildDto {

	/**
	 * 子部门ID
	 */
	private Long childId;

	/**
	 * 父部门ID列表
	 */
	private List<Long> deptIdList;

}
