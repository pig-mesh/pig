package com.pig4cloud.pig.admin.api.constant;

/**
 * UPMS 模块错误编码
 *
 * @author lengleng
 * @date 2022/3/30
 */
public interface UpmsErrorCodes {

    // === 用户相关 ===
    String SYS_USER_USERNAME_EXISTING = "sys.user.username.existing";

    String SYS_USER_PHONE_EXISTING = "sys.user.phone.existing";

    String SYS_USER_UPDATE_PASSWORDERROR = "sys.user.update.passwordError";

    String SYS_USER_OLD_PASSWORD_EMPTY = "sys.user.oldPassword.empty";

    String SYS_USER_NEW_PASSWORD_EMPTY = "sys.user.newPassword.empty";

    String SYS_USER_UNBIND_TYPE_NOT_FOUND = "sys.user.unbind.type.notFound";

    String SYS_USER_PASSWORD_MISMATCH = "sys.user.password.mismatch";

    String SYS_USER_PASSWORD_SAME = "sys.user.password.same";

    String SYS_USER_USERINFO_EMPTY = "sys.user.userInfo.empty";

    String SYS_USER_QUERY_ERROR = "sys.user.query.error";

    String SYS_USER_IMPORT_SUCCEED = "sys.user.import.succeed";

    // === 部门相关 ===
    String SYS_DEPT_DEPTNAME_INEXISTENCE = "sys.dept.deptName.inexistence";

    String SYS_DEPT_IMPORT_SUCCEED = "sys.dept.import.succeed";

    String SYS_DEPT_SWITCH_SUCCEED = "sys.dept.switch.succeed";

    // === 岗位相关 ===
    String SYS_POST_POSTNAME_INEXISTENCE = "sys.post.postName.inexistence";

    String SYS_POST_NAMEORCODE_EXISTING = "sys.post.nameOrCode.existing";

    // === 角色相关 ===
    String SYS_ROLE_ROLENAME_INEXISTENCE = "sys.role.roleName.inexistence";

    String SYS_ROLE_NAMEORCODE_EXISTING = "sys.role.nameOrCode.existing";

    // === 菜单相关 ===
    String SYS_MENU_DELETE_EXISTING = "sys.menu.delete.existing";

    String SYS_MENU_NOT_FOUND = "sys.menu.notFound";

    String SYS_MENU_HOME_SET_SUCCEED = "sys.menu.home.set.succeed";

    String SYS_MENU_HOME_CANCEL_SUCCEED = "sys.menu.home.cancel.succeed";

    String SYS_MENU_BUTTON_CANNOT_HOME = "sys.menu.button.cannot.home";

    // === 字典相关 ===
    String SYS_DICT_DELETE_SYSTEM = "sys.dict.delete.system";

    String SYS_DICT_UPDATE_SYSTEM = "sys.dict.update.system";

    // === 参数相关 ===
    String SYS_PARAM_CONFIG_ERROR = "sys.param.config.error";

    String SYS_PARAM_DELETE_SYSTEM = "sys.param.delete.system";

    String SYS_PARAM_ILLEGAL = "sys.param.illegal";

    // === 消息相关 ===
    String SYS_MESSAGE_SMS_CHANNEL_MISSING = "sys.message.sms.channel.missing";

    String SYS_MESSAGE_CHANNEL_MISSING = "sys.message.channel.missing";

    // === 小程序相关 ===
    String SYS_MINIAPP_LOGIN_FAILED = "sys.miniapp.login.failed";

    // === 系统缓存 ===
    String SYS_SYSTEM_CACHE_FETCH_ERROR = "sys.system.cache.fetch.error";

    // === 企业微信相关 ===
    String SYS_CONNECT_CP_DEPT_SYNC_ERROR = "sys.connect.cp.dept.sync.error";

    String SYS_CONNECT_CP_USER_SYNC_ERROR = "sys.connect.cp.user.sync.error";

    // === 短信验证相关 (在 UPMS 消息和用户服务中使用) ===
    String SYS_APP_SMS_OFTEN = "sys.app.sms.often";

    String SYS_APP_SMS_ERROR = "sys.app.sms.error";

    String SYS_APP_PHONE_UNREGISTERED = "sys.app.phone.unregistered";

}
