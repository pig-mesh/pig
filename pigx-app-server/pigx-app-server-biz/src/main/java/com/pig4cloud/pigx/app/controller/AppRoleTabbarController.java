package com.pig4cloud.pigx.app.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.app.api.dto.AppRoleTabbarDTO;
import com.pig4cloud.pigx.app.api.entity.AppTabbarEntity;
import com.pig4cloud.pigx.app.service.AppRoleTabbarService;
import com.pig4cloud.pigx.app.service.AppTabbarService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * App 角色底部导航授权管理接口。
 * <p>
 * 该接口只维护「系统角色 -> App Tabbar」的授权关系。角色本身仍来自 upms 的 {@code sys_role}，
 * App 端运行时根据登录 token 中的角色 ID 读取 {@code app_role_tabbar}，不跨库 join {@code sys_user_role}。
 *
 * @author lengleng
 * @date 2026-05-09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/role-tabbar")
@Tag(description = "role-tabbar", name = "App角色底部导航授权")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppRoleTabbarController {

    private final RemoteRoleService remoteRoleService;

    private final AppTabbarService appTabbarService;

    private final AppRoleTabbarService appRoleTabbarService;

    /**
     * 查询可授权的系统角色列表。
     * <p>
     * 角色来源于 upms 远程接口，前端可按角色名称做轻量过滤，用于角色 Tabbar 授权页面。
     *
     * @param roleName 角色名称关键字，可为空
     * @return 系统角色列表
     */
    @GetMapping("/roles")
    @HasPermission("app_role_tabbar_view")
    public R<List<SysRole>> roles(@RequestParam(required = false) String roleName) {
        R<List<SysRole>> roles = remoteRoleService.getAllRole();
        if (!roles.isOk() || roles.getData() == null || StrUtil.isBlank(roleName)) {
            return roles;
        }
        return R.ok(roles.getData()
                .stream()
                .filter(role -> StrUtil.containsIgnoreCase(role.getRoleName(), roleName))
                .toList());
    }

    /**
     * 查询可授权的 App 底部导航列表。
     * <p>
     * 返回完整 Tabbar 列表，公开 Tabbar 前端可固定勾选，登录可见 Tabbar 由角色授权控制。
     *
     * @return App Tabbar 列表
     */
    @GetMapping("/tabbar")
    @HasPermission("app_role_tabbar_view")
    public R<List<AppTabbarEntity>> tabbars() {
        return R.ok(appTabbarService.list(Wrappers.<AppTabbarEntity>lambdaQuery()
                .orderByAsc(AppTabbarEntity::getSortOrder)));
    }

    /**
     * 查询指定角色已授权的 Tabbar ID。
     *
     * @param roleId 系统角色 ID
     * @return 已授权的 Tabbar ID 列表
     */
    @GetMapping("/{roleId}")
    @HasPermission("app_role_tabbar_view")
    public R<List<Long>> roleTabbars(@PathVariable Long roleId) {
        return R.ok(appRoleTabbarService.listTabbarIdsByRoleId(roleId));
    }

    /**
     * 保存角色 Tabbar 授权。
     * <p>
     * 保存采用覆盖语义：先清空该角色原有授权，再写入本次提交的 Tabbar ID 列表。
     *
     * @param dto 角色 Tabbar 授权请求
     * @return 保存结果
     */
    @PutMapping
    @HasPermission("app_role_tabbar_edit")
    public R<Boolean> saveRoleTabbars(@Valid @RequestBody AppRoleTabbarDTO dto) {
        return R.ok(appRoleTabbarService.saveRoleTabbars(dto));
    }

}
