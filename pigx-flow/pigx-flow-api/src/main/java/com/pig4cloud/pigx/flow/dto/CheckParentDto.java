package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.util.List;

/**
 * 父部门检查数据传输对象
 * <p>
 * 该DTO用于检查指定的父部门是否为给定部门列表中任意一个的父部门。
 * 主要用于流程中验证部门层级关系，判断审批人是否具有对某些部门的管辖权限。
 * </p>
 *
 * @author pigx
 */
@Data
public class CheckParentDto {

	/**
	 * 需要检查的父部门ID
	 * <p>
	 * 要验证是否为指定部门列表父部门的部门ID
	 * </p>
	 */
	private Long parentId;

	/**
	 * 子部门ID列表
	 * <p>
	 * 包含所有需要检查的子部门ID。
	 * 如果parentId是这个列表中任意一个部门的父部门（直接或间接），则检查通过。
	 * </p>
	 */
	private List<Long> deptIdList;

}
