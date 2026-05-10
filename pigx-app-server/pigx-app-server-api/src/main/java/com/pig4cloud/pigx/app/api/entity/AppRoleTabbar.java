package com.pig4cloud.pigx.app.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * App 角色底部导航关联实体，以 (role_id, tabbar_id, tenant_id) 为复合主键。
 * <p>
 * 这里的 roleId 是系统角色 {@code sys_role.role_id}。运行时不会跨库查询 {@code sys_user_role}，
 * 而是从登录 token 的角色权限中解析 roleId，再回到本表查询可见 Tabbar。
 *
 * @author lengleng
 * @date 2026-05-09
 */
@Data
@TenantTable
@TableName("app_role_tabbar")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "App角色底部导航")
public class AppRoleTabbar extends Model<AppRoleTabbar> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "系统角色ID")
    private Long roleId;

    @Schema(description = "底部导航ID")
    private Long tabbarId;

    @Schema(description = "租户ID", hidden = true)
    private Long tenantId;

}
