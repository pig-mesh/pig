package com.pig4cloud.pigx.admin.api.constant;

/**
 * 岗位编码
 *
 * @author lengleng
 * @date 2024/6/14
 * <p>
 * 'TEAM_LEADER', '部门负责人'
 */

public enum PostCodeEnum {
    /**
     * 部门负责人
     */
    TEAM_LEADER("TEAM_LEADER", "部门负责人");

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String description;

    PostCodeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
