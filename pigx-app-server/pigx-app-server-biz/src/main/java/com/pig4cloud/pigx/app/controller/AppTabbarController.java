package com.pig4cloud.pigx.app.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.app.api.entity.AppRoleTabbar;
import com.pig4cloud.pigx.app.api.entity.AppTabbarEntity;
import com.pig4cloud.pigx.app.service.AppRoleTabbarService;
import com.pig4cloud.pigx.app.service.AppTabbarService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * App 底部导航后台管理接口。
 * <p>
 * Tabbar 是移动端底部入口的数据源，图标字段保存 Wot UI 图标名；
 * 是否登录可见由 {@code loginFlag} 控制，登录后再叠加角色授权。
 *
 * @author pig
 * @date 2023-06-07 16:32:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/appTabbar")
@Tag(description = "appTabbar", name = "底部导航")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppTabbarController {

    private final AppTabbarService tabbarService;

    private final AppRoleTabbarService roleTabbarService;

    /**
     * 查询全部底部导航。
     * <p>
     * 后台维护页面使用完整列表；移动端运行时请走 {@code /app/index/config}，
     * 由运行时接口按公开/角色授权规则过滤。
     *
     * @return 完整 Tabbar 列表
     */
    @Operation(summary = "查询导航列表", description = "查询导航列表")
    @GetMapping("/list")
    public R list() {
        return R.ok(tabbarService.list(
                Wrappers.<AppTabbarEntity>lambdaQuery().orderByAsc(AppTabbarEntity::getSortOrder).orderByAsc(AppTabbarEntity::getId)));
    }

    /**
     * 批量保存底部导航。
     * <p>
     * 前端提交的是当前完整 Tabbar 列表。这里会删除数据库中已不存在的 Tabbar，
     * 并同步清理角色授权关系，避免 {@code app_role_tabbar} 残留无效 tabbar_id。
     *
     * @param tabbarEntityList 当前完整 Tabbar 配置
     * @return 保存结果
     */
    @Operation(summary = "更新导航", description = "更新导航")
    @PutMapping
    public R update(@RequestBody List<AppTabbarEntity> tabbarEntityList) {
        tabbarEntityList.forEach(tabbar -> {
            if (tabbar.getLoginFlag() == null) {
                tabbar.setLoginFlag("1");
            }
        });
        // 删除不在新增范围的导航菜单
        List<Long> idList = tabbarService.list().stream().map(AppTabbarEntity::getId).toList();
        List<Long> newIdList = tabbarEntityList.stream().map(AppTabbarEntity::getId).toList();

        // 计算需要删除的 ID 列表
        List<Long> idsToRemove = CollUtil.subtractToList(idList, newIdList);

        // 如果有需要删除的菜单，则批量删除
        if (CollUtil.isNotEmpty(idsToRemove)) {
            tabbarService.removeBatchByIds(idsToRemove);
            roleTabbarService.remove(Wrappers.<AppRoleTabbar>lambdaQuery().in(AppRoleTabbar::getTabbarId, idsToRemove));
        }

        return R.ok(tabbarService.saveOrUpdateBatch(tabbarEntityList));
	}

}
