package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

import java.util.List;

/**
 * 检查是否是给定的父级
 *
 */
@Data
public class CheckParentDto {

	private Long parentId;

	private List<Long> deptIdList;

}
