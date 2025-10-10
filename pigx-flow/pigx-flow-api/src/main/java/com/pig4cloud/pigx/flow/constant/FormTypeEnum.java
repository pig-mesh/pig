package com.pig4cloud.pigx.flow.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

/**
 * 表单类型枚举
 * <p>
 * 定义流程表单中支持的所有控件类型。每种类型对应不同的表单组件，
 * 用于构建动态表单，收集流程执行过程中所需的各类数据。
 * </p>
 *
 * @author pigx
 * @since 2024/01/01
 */
@Getter
@RequiredArgsConstructor
public enum FormTypeEnum {

	/**
	 * 单行文本输入框
	 * <p>
	 * 用于输入简短的文本内容，如姓名、标题等。
	 * 默认值为空字符串。
	 * </p>
	 */
	INPUT("Input", "单行文本", ""),
	
	/**
	 * 多行文本输入框
	 * <p>
	 * 用于输入较长的文本内容，如描述、备注等。
	 * 支持换行输入，默认值为空字符串。
	 * </p>
	 */
	TEXTAREA("Textarea", "多行文本", ""),
	
	/**
	 * 数字输入框
	 * <p>
	 * 用于输入数字类型的数据，支持整数和小数。
	 * 可设置最大值、最小值等限制。
	 * </p>
	 */
	NUMBER("Number", "数字", null),
	
	/**
	 * 日期选择器
	 * <p>
	 * 用于选择日期，格式为 yyyy-MM-dd。
	 * 不包含时间部分。
	 * </p>
	 */
	DATE("Date", "日期", null),
	
	/**
	 * 日期时间选择器
	 * <p>
	 * 用于选择日期和时间，格式为 yyyy-MM-dd HH:mm:ss。
	 * 包含完整的日期时间信息。
	 * </p>
	 */
	DATE_TIME("DateTime", "日期时间", null),

	/**
	 * 序列号生成器
	 * <p>
	 * 自动生成唯一的序列号，如单据编号、流水号等。
	 * 支持自定义前缀、后缀和序列规则。
	 * </p>
	 */
	SEQUENCE("Sequence", "发号器", null),
	
	/**
	 * 明细表格布局
	 * <p>
	 * 用于创建子表单，支持动态增减行。
	 * 适用于需要录入多条明细数据的场景，如报销明细、订单明细等。
	 * </p>
	 */
	LAYOUT("Layout", "明细", null),
	
	/**
	 * 时间选择器
	 * <p>
	 * 用于选择时间，格式为 HH:mm:ss。
	 * 仅包含时间部分，不包含日期。
	 * </p>
	 */
	TIME("Time", "时间", null),
	
	/**
	 * 金额输入框
	 * <p>
	 * 专门用于输入货币金额，支持千分位显示。
	 * 自动保留两位小数，支持大写金额转换。
	 * </p>
	 */
	MONEY("Money", "金额", null),
	
	/**
	 * 单选下拉框
	 * <p>
	 * 从预定义选项中选择一个值。
	 * 默认值为空列表，需要配置选项数据。
	 * </p>
	 */
	SINGLE_SELECT("SingleSelect", "单选", new ArrayList<>()),
	
	/**
	 * 部门选择器
	 * <p>
	 * 从组织架构中选择部门。
	 * 支持树形结构展示，可配置单选或多选。
	 * </p>
	 */
	SELECT_DEPT("SelectDept", "部门", new ArrayList<>()),
	
	/**
	 * 用户选择器
	 * <p>
	 * 从系统用户中选择人员。
	 * 支持按部门、角色筛选，可配置单选或多选。
	 * </p>
	 */
	SELECT_USER("SelectUser", "用户", new ArrayList<>()),

	;

	/**
	 * 表单类型标识
	 * <p>
	 * 前端组件类型的唯一标识符
	 * </p>
	 */
	private final String type;

	/**
	 * 表单类型名称
	 * <p>
	 * 用于界面显示的中文名称
	 * </p>
	 */
	private final String name;

	/**
	 * 默认值
	 * <p>
	 * 该类型表单项的默认值，可以是字符串、数字、列表等
	 * </p>
	 */
	private final Object defaultValue;

}
