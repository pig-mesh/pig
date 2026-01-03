package com.pig4cloud.pigx.app.api.enums;

/**
 * App Server 模块错误编码
 *
 * @author lengleng
 * @date 2022/3/30
 */
public interface AppErrorCodes {

	// === App 用户相关 ===
	String APP_USER_USERINFO_EMPTY = "app.user.userInfo.empty";

	String APP_USER_USERNAME_EXISTING = "app.user.username.existing";

	String APP_USER_IMPORT_SUCCEED = "app.user.import.succeed";

	String APP_USER_QUERY_ERROR = "app.user.query.error";

	// === App 角色相关 ===
	String APP_ROLE_ROLENAME_INEXISTENCE = "app.role.roleName.inexistence";

	String APP_ROLE_NAMEORCODE_EXISTING = "app.role.nameOrCode.existing";

	// === App 短信相关 ===
	String APP_SMS_OFTEN = "app.sms.often";

}
