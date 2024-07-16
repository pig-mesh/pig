package com.pig4cloud.pigx.codegen.util;

/**
 * 代码生成模板中能使用的变量名称
 *
 * @author lengleng
 * @date 2024/7/16
 */
public class DataModelConstants {
    // 基本信息
    public static final String IS_SPRING_BOOT_3 = "isSpringBoot3";
    public static final String SYNC_MENU_ID = "syncMenuId";
    public static final String DB_TYPE = "dbType";
    public static final String PACKAGE = "package";
    public static final String PACKAGE_PATH = "packagePath";
    public static final String VERSION = "version";

    // 模块和功能名称
    public static final String MODULE_NAME = "moduleName";
    public static final String MODULE_NAME_UPPER_FIRST = "ModuleName";
    public static final String FUNCTION_NAME = "functionName";
    public static final String FUNCTION_NAME_UPPER_FIRST = "FunctionName";

    // 布局和样式
    public static final String FORM_LAYOUT = "formLayout";
    public static final String STYLE = "style";

    // 作者和时间
    public static final String AUTHOR = "author";
    public static final String DATETIME = "datetime";
    public static final String DATE = "date";

    // 导入列表
    public static final String IMPORT_LIST = "importList";

    // 表信息
    public static final String TABLE_NAME = "tableName";
    public static final String TABLE_COMMENT = "tableComment";
    public static final String CLASS_NAME = "className";
    public static final String CLASS_NAME_UPPER_FIRST = "ClassName";
    public static final String FIELD_LIST = "fieldList";

    // 路径
    public static final String BACKEND_PATH = "backendPath";
    public static final String FRONTEND_PATH = "frontendPath";

    // 子表信息
    public static final String CHILD_FIELD_LIST = "childFieldList";
    public static final String CHILD_TABLE_NAME = "childTableName";
    public static final String MAIN_FIELD = "mainField";
    public static final String CHILD_FIELD = "childField";
    public static final String CHILD_CLASS_NAME_UPPER_FIRST = "ChildClassName";
    public static final String CHILD_CLASS_NAME = "childClassName";
    public static final String IS_CHILD_TENANT = "isChildTenant";

    // 租户信息
    public static final String IS_TENANT = "isTenant";
}
