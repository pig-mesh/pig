package com.pig4cloud.pigx.flow.task.constant;

public class ProcessInstanceConstant {

	/**
	 * 空执行人
	 */
	public static final Long DEFAULT_EMPTY_ASSIGN = -99999999L;

	/**
	 * 用户任务没有执行人的情况下如何处理 自动通过
	 */
	public static final String USER_TASK_NOBODY_HANDLER_TO_PASS = "TO_PASS";

	/**
	 * 转交给管理员
	 */
	public static final String USER_TASK_NOBODY_HANDLER_TO_ADMIN = "TO_ADMIN";

	/**
	 * 指定人员
	 */
	public static final String USER_TASK_NOBODY_HANDLER_TO_USER = "TO_USER";

	/**
	 * 结束流程
	 */
	public static final String USER_TASK_NOBODY_HANDLER_TO_END = "TO_END";

	public static final String USER_TASK_NOBODY_HANDLER_TO_REFUSE = "TO_REFUSE";

	/**
	 * 拒绝之后 结束流程
	 */
	public static final String USER_TASK_REFUSE_TYPE_TO_END = "TO_END";

	/**
	 * 拒绝之后 到某个节点
	 */
	public static final String USER_TASK_REFUSE_TYPE_TO_NODE = "TO_NODE";

	/**
	 * 会签
	 */
	public static final int MULTIPLE_MODE_AL_SAME = 1;

	/**
	 * 或签
	 */
	public static final int MULTIPLE_MODE_ONE = 2;

	/**
	 * 顺签
	 */
	public static final int MULTIPLE_MODE_ALL_SORT = 3;

	public static class AssignedTypeClass {

		// 指定用户
		public static final int USER = 1;

		// 指定主管
		public static final int LEADER = 2;

		// 发起人自己
		public static final int SELF = 5;

		// 表单人员
		public static final int FORM_USER = 8;

		// 发起人自选
		public static final int SELF_SELECT = 4;

		// 角色
		public static final int ROLE = 3;

	}

	/**
	 * 表单权限
	 */
	public static class FormPermClass {

		// 隐藏
		public static final String HIDE = "H";

		// 只读
		public static final String READ = "R";

		// 编辑
		public static final String EDIT = "E";

	}

}
