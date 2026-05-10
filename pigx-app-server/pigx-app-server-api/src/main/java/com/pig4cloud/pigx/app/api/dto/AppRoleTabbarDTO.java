package com.pig4cloud.pigx.app.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * App 角色底部导航配置请求体。
 * <p>
 * 后台保存某个系统角色可见的 App Tabbar 列表。公开 Tabbar 由 {@code app_tabbar.login_flag=0}
 * 控制，不依赖这里的角色授权。
 *
 * @author lengleng
 * @date 2026-05-09
 */
@Data
@Schema(description = "App角色底部导航配置")
public class AppRoleTabbarDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "角色ID不能为空")
    @Schema(description = "系统角色ID")
    private Long roleId;

    /**
     * 该角色可见的登录态 Tabbar ID 列表。
     * <p>
     * 保存时采用覆盖语义，空列表表示清空该角色的 Tabbar 授权。
     */
    @Schema(description = "底部导航ID列表")
    private List<Long> tabbarIds = new ArrayList<>();

}
