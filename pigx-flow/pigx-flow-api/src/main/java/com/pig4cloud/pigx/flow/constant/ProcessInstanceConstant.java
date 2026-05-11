package com.pig4cloud.pigx.flow.constant;

/**
 * 流程实例常量定义
 * <p>
 * 定义流程实例执行过程中使用的各种常量，包括： - 无执行人处理策略 - 拒绝处理策略 - 多人审批模式 - 任务分配类型 - 表单权限
 * </p>
 * 
 * @author pigx
 */
public class ProcessInstanceConstant {

	/**
	 * 空执行人默认ID 当节点没有找到合适的执行人时使用该默认值
	 */
	public static final Long DEFAULT_EMPTY_ASSIGN = -99999999L;

	/**
	 * 无执行人处理策略：自动通过 当任务节点没有找到执行人时，自动通过该节点
	 */
	public static final String USER_TASK_NOBODY_HANDLER_TO_PASS = "TO_PASS";

	/**
	 * 无执行人处理策略：转交给管理员 当任务节点没有找到执行人时，将任务转交给流程管理员处理
	 */
	public static final String USER_TASK_NOBODY_HANDLER_TO_ADMIN = "TO_ADMIN";

	/**
	 * 无执行人处理策略：指定人员 当任务节点没有找到执行人时，转交给预先指定的人员处理
	 */
	public static final String USER_TASK_NOBODY_HANDLER_TO_USER = "TO_USER";

	/**
	 * 无执行人处理策略：结束流程 当任务节点没有找到执行人时，直接结束整个流程
	 */
	public static final String USER_TASK_NOBODY_HANDLER_TO_END = "TO_END";

	/**
	 * 无执行人处理策略：拒绝 当任务节点没有找到执行人时，自动拒绝该任务
	 */
	public static final String USER_TASK_NOBODY_HANDLER_TO_REFUSE = "TO_REFUSE";

	/**
	 * 拒绝处理策略：结束流程 审批人拒绝后，直接结束整个流程
	 */
	public static final String USER_TASK_REFUSE_TYPE_TO_END = "TO_END";

	/**
	 * 拒绝处理策略：跳转到指定节点 审批人拒绝后，流程跳转到指定的节点继续执行
	 */
	public static final String USER_TASK_REFUSE_TYPE_TO_NODE = "TO_NODE";

	/**
	 * 拒绝处理策略：驳回到发起人 审批人拒绝后，流程跳转回发起人节点，发起人可重新编辑提交
	 */
	public static final String USER_TASK_REFUSE_TYPE_TO_START = "TO_START";

	/**
	 * 流程变量：驳回到发起人标识 用于标记流程实例是否处于驳回待重新提交状态
	 */
	public static final String REJECT_TO_STARTER_VAR = "rejectToStarter";

    /**
     * 流程结束原因：拒绝
     */
    public static final String FINISH_REASON_REFUSE = "0";

    /**
     * 流程结束原因：通过
     */
    public static final String FINISH_REASON_PASS = "1";

    /**
     * 流程结束原因：撤回
     */
    public static final String FINISH_REASON_WITHDRAW = "3";

    /**
     * 流程结束原因：终止
     */
    public static final String FINISH_REASON_TERMINATE = "9";

    /**
     * 流程节点执行人状态：撤回
     */
    public static final int ASSIGN_USER_STATUS_WITHDRAW = 3;

    /**
     * 流程节点执行人状态：终止
     */
    public static final int ASSIGN_USER_STATUS_TERMINATE = 5;

	/**
	 * 多人审批模式：会签 所有审批人都必须同意才能通过，任何一人拒绝则不通过
	 */
	public static final int MULTIPLE_MODE_AL_SAME = 1;

	/**
	 * 多人审批模式：或签 只要有一个人同意即可通过，无需等待其他人审批
	 */
	public static final int MULTIPLE_MODE_ONE = 2;

	/**
	 * 多人审批模式：顺签 按照指定顺序依次审批，前一人通过后才能轮到下一人
	 */
	public static final int MULTIPLE_MODE_ALL_SORT = 3;

	/**
	 * 任务分配类型 定义审批节点的执行人分配方式
	 */
	public static class AssignedTypeClass {

		/**
		 * 指定用户 直接指定具体的用户作为执行人
		 */
		public static final int USER = 1;

		/**
		 * 部门主管 根据发起人或上一节点执行人的部门，找到对应的部门主管作为执行人
		 */
		public static final int LEADER = 2;

		/**
		 * 发起人自己 由流程发起人自己处理该节点
		 */
		public static final int SELF = 5;

		/**
		 * 表单人员 从表单中的用户选择字段获取执行人
		 */
		public static final int FORM_USER = 8;

		/**
		 * 发起人自选 发起流程时由发起人手动选择该节点的执行人
		 */
		public static final int SELF_SELECT = 4;

		/**
		 * 角色 指定拥有特定角色的所有用户作为执行人
		 */
		public static final int ROLE = 3;

		/** 表达式常量值 */
		public static final int EXPRESSION = 10;

	}

	/**
	 * 表单权限类型 定义节点对表单字段的操作权限
	 */
	public static class FormPermClass {

		/**
		 * 隐藏 该字段对当前节点执行人不可见
		 */
		public static final String HIDE = "H";

		/**
		 * 只读 该字段对当前节点执行人可见但不可编辑
		 */
		public static final String READ = "R";

		/**
		 * 编辑 该字段对当前节点执行人可见且可编辑
		 */
		public static final String EDIT = "E";

	}

}
