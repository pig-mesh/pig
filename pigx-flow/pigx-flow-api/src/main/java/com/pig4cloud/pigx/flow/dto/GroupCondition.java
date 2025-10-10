package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.util.List;

/**
 * 分组条件数据传输对象
 * <p>
 * 该DTO用于定义一组条件的逻辑关系。可以将多个条件组合在一起，
 * 通过AND（与）或OR（或）的方式进行逻辑运算，用于复杂的流程条件判断。
 * 常用于条件分支节点，支持构建复杂的条件表达式。
 * </p>
 * 
 * @author pigx
 */
@Data
public class GroupCondition {

	/**
	 * 条件组合模式
	 * <p>
	 * 定义条件列表中各个条件之间的逻辑关系：
	 * - true: AND模式，所有条件都必须满足（与关系）
	 * - false: OR模式，满足任意一个条件即可（或关系）
	 * </p>
	 */
	private Boolean mode;

	/**
	 * 条件列表
	 * <p>
	 * 包含该分组下的所有条件。
	 * 这些条件会根据mode字段指定的逻辑关系进行组合判断。
	 * 每个条件包含键、表达式、值等信息，用于具体的条件判断。
	 * </p>
	 */
	private List<Condition> conditionList;

}
