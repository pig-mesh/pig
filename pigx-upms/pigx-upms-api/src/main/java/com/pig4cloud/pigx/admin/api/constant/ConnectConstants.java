package com.pig4cloud.pigx.admin.api.constant;

/**
 * 第三方连接常量类
 * <p>
 * 用于定义第三方平台(如钉钉)连接相关的字段名称和常量值
 *
 * @author lengleng
 * @date 2026/01/10
 */
public interface ConnectConstants {

	/**
	 * JSON字段名: 手机号
	 */
	String FIELD_MOBILE = "mobile";

	/**
	 * JSON字段名: 头像
	 */
	String FIELD_AVATAR = "avatar";

	/**
	 * JSON字段名: 昵称
	 */
	String FIELD_NICKNAME = "nickname";

	/**
	 * JSON字段名: 姓名
	 */
	String FIELD_NAME = "name";

	/**
	 * JSON字段名: 邮箱
	 */
	String FIELD_EMAIL = "email";

	/**
	 * JSON字段名: 部门ID
	 */
	String FIELD_DEPT_ID = "dept_id";

	/**
	 * JSON字段名: 父部门ID
	 */
	String FIELD_PARENT_ID = "parent_id";

	/**
	 * JSON字段名: 部门顺序列表
	 */
	String FIELD_DEPT_ORDER_LIST = "dept_order_list";

	/**
	 * JSON字段名: 用户ID列表
	 */
	String FIELD_USERID_LIST = "userid_list";

	/**
	 * JSON字段名: 返回结果
	 */
	String FIELD_RESULT = "result";

	/**
	 * 语言代码: 简体中文
	 */
	String LANGUAGE_ZH_CN = "zh_CN";

	/**
	 * 钉钉根部门ID
	 */
	Long ROOT_DEPT_ID = 1L;

}
