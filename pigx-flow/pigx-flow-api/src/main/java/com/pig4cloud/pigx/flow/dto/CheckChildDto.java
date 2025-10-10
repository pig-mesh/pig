package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.util.List;

/**
 * 子部门检查数据传输对象
 * <p>
 * 该DTO用于检查指定的子部门是否属于给定的父部门列表中的任意一个。
 * 主要用于流程中判断审批人是否在指定部门范围内的场景。
 * </p>
 * 
 * @author pigx
 */
@Data
public class CheckChildDto {

	/**
	 * 需要检查的子部门ID
	 * <p>
	 * 要验证是否为指定父部门子部门的部门ID
	 * </p>
	 */
	private Long childId;

	/**
	 * 父部门ID列表
	 * <p>
	 * 包含所有需要检查的父部门ID。
	 * 如果childId是这个列表中任意一个部门的子部门（直接或间接），则检查通过。
	 * </p>
	 */
	private List<Long> deptIdList;

}
