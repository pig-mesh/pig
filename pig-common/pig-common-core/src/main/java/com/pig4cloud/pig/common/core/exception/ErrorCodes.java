package com.pig4cloud.pig.common.core.exception;

/**
 * 错误编码
 *
 * @author lengleng
 * @date 2022/3/30
 */
public interface ErrorCodes {

	/**
	 * 系统编码错误
	 */
	String SYS_PARAM_CONFIG_ERROR = "sys.param.config.error";

	/**
	 * 系统内置参数不能删除
	 */
	String SYS_PARAM_DELETE_SYSTEM = "sys.param.delete.system";

	/**
	 * 用户已存在
	 */
	String SYS_USER_EXISTING = "sys.user.existing";

	/**
	 * 用户名已存在
	 */
	String SYS_USER_USERNAME_EXISTING = "sys.user.username.existing";

	/**
	 * 用户原密码错误，修改失败
	 */
	String SYS_USER_UPDATE_PASSWORDERROR = "sys.user.update.passwordError";

	/**
	 * 用户信息为空
	 */
	String SYS_USER_USERINFO_EMPTY = "sys.user.userInfo.empty";

	/**
	 * 获取当前用户信息失败
	 */
	String SYS_USER_QUERY_ERROR = "sys.user.query.error";

	/**
	 * 部门名称不存在
	 */
	String SYS_DEPT_DEPTNAME_INEXISTENCE = "sys.dept.deptName.inexistence";

	/**
	 * 岗位名称不存在
	 */
	String SYS_POST_POSTNAME_INEXISTENCE = "sys.post.postName.inexistence";

	/**
	 * 岗位名称或编码已经存在
	 */
	String SYS_POST_NAMEORCODE_EXISTING = "sys.post.nameOrCode.existing";

	/**
	 * 角色名称不存在
	 */
	String SYS_ROLE_ROLENAME_INEXISTENCE = "sys.role.roleName.inexistence";

	/**
	 * 角色名或角色编码已经存在
	 */
	String SYS_ROLE_NAMEORCODE_EXISTING = "sys.role.nameOrCode.existing";

	/**
	 * 菜单存在下级节点 删除失败
	 */
	String SYS_MENU_DELETE_EXISTING = "sys.menu.delete.existing";

	/**
	 * 系统内置字典不允许删除
	 */
	String SYS_DICT_DELETE_SYSTEM = "sys.dict.delete.system";

	/**
	 * 系统内置字典不能修改
	 */
	String SYS_DICT_UPDATE_SYSTEM = "sys.dict.update.system";

	/**
	 * 验证码发送频繁
	 */
	String SYS_APP_SMS_OFTEN = "sys.app.sms.often";

	/**
	 * 验证码错误
	 */
	String SYS_APP_SMS_ERROR = "sys.app.sms.error";

	/**
	 * 手机号未注册
	 */
	String SYS_APP_PHONE_UNREGISTERED = "sys.app.phone.unregistered";

}
