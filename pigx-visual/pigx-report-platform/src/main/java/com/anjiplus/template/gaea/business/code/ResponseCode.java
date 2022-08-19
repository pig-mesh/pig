package com.anjiplus.template.gaea.business.code;

/**
 * 响应码
 * @author lr
 * @since 2021-02-22
 */
public interface ResponseCode {

    String NOT_NULL = "field.not.null";
    String NOT_EMPTY = "field.not.empty";
    String MIN = "field.min";
    String MAX = "field.max";
    String DICT_ERROR = "field.dict.error";

    String USER_PASSWORD_ERROR = "User.password.error";
    /**
     * 用户名或者密码不正确
     */
    String LOGIN_ERROR = "login.error";

    /**
     * 新密码不能和原密码一致
     */
    String USER_PASSWORD_CONFIG_PASSWORD_CANOT_EQUAL = "user.password.config.password.canot.equal";

    /**
     * 密码和确认密码不一致
     */
    String USER_INCONSISTENT_PASSWORD_ERROR = "user.inconsistent.password.error";

    /**
     * 旧密码不正确
     */
    String USER_OLD_PASSWORD_ERROR = "user.old.password.error";



    /**
     * 用户token过期
     */
    String USER_TOKEN_EXPIRED = "User.token.expired";

    /**
     * 字典项重复
     */
    String DICT_ITEM_REPEAT = "Dict.item.code.exist";

    /**
     * 数字字典国际化标识不能为null
     */
    String DICT_CODE_LOCALE_NULL = "500-00002";

    /**
     * 参数为空
     */
    String PARAM_IS_NULL = "Rule.execute.param.null";

    /**
     * 规则编译不通过
     */
    String RULE_CONTENT_COMPILE_ERROR = "Rule.content.compile.error";

    /**
     * 规则执行不通过
     */
    String RULE_CONTENT_EXECUTE_ERROR = "Rule.content.execute.error";

    /**
     * 规则编码已存在
     */
    String RULE_CODE_EXIST = "Rule.code.exist";

    /**
     * 对应规则内容不存在
     */
    String RULE_CONTENT_NOT_EXIST = "Rule.content.not.exist";

    /**
     * 对应规则字段值不存在
     */
    String RULE_FIELDS_NOT_EXIST = "Rule.fields.not.exist";

    /**
     * 规则字段必填
     */
    String RULE_FIELD_VALUE_IS_REQUIRED = "Rule.field.value.is.required";

    /**
     * 规则字段值类型错误
     */
    String RULE_FIELD_VALUE_TYPE_ERROR = "Rule.field.value.type.error";

    /**
     * 规则参数校验不通过
     */
    String RULE_FIELDS_CHECK_ERROR = "Rule.fields.check.error";
    /**
     * 组件未加载
     */
    String COMPONENT_NOT_LOAD = "Component.load.check.error";

    String AUTH_PASSWORD_NOTSAME = "1001";
    String OLD_PASSWORD_ERROR = "1003";
    String USER_ONTEXIST_ORGINFO = "1004";
    String USER_ONTEXIST_ROLEINFO = "1005";
    String MENU_TABLE_CODE_EXIST = "1006";
    String USER_CODE_ISEXIST = "1007";
    String ROLE_CODE_ISEXIST = "1008";
    String MENU_CODE_ISEXIST = "1009";
    String ORG_CODE_ISEXIST = "1010";
    String SEARCHNAME_ISEXIST = "1011";
    String SETTINGNAME_ISEXIST = "1012";
    String DICCODE_ISEXIST = "1013";
    String DEVICEID_LENGTH = "1014";
    String USERINFO_EMPTY = "1015";
    String FILE_EMPTY_FILENAME = "2001";
    String FILE_SUFFIX_UNSUPPORTED = "2002";
    String FILE_UPLOAD_ERROR = "2003";
    String FILE_ONT_EXSIT = "2004";
    String LIST_IS_EMPTY = "2005";
    String PUSHCODE_NEED_UNIQUE = "3001";
    String RECEIVER_IS_EMPTY = "3002";
    String DATA_SOURCE_CONNECTION_FAILED = "4001";
    String DATA_SOURCE_TYPE_DOES_NOT_MATCH_TEMPORARILY = "4002";
    String EXECUTE_SQL_ERROR = "4003";
    String INCOMPLETE_PARAMETER_REPLACEMENT_VALUES = "4004";
    String EXECUTE_JS_ERROR = "4005";
    String ANALYSIS_DATA_ERROR = "4006";
    String REPORT_CODE_ISEXIST = "4007";
    String SET_CODE_ISEXIST = "4008";
    String SOURCE_CODE_ISEXIST = "4009";
    String CLASS_NOT_FOUND = "4010";
    String EXECUTE_GROOVY_ERROR = "4011";

    String REPORT_SHARE_LINK_INVALID = "report.share.link.invalid";

}
