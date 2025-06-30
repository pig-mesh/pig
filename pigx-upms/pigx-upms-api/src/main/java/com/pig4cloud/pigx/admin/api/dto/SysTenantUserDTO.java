package com.pig4cloud.pigx.admin.api.dto;

import lombok.Data;

/**
 * 系统租户用户数据传输对象
 *
 * @author lengleng
 * @date 2025/06/30
 */
@Data
public class SysTenantUserDTO {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 用户ID数组
     */
    private Long[] userIds;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 部门ID
     */
    private Long deptId;
}
