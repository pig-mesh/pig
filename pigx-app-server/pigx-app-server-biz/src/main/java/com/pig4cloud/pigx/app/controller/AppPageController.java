package com.pig4cloud.pigx.app.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.app.api.entity.AppPageEntity;
import com.pig4cloud.pigx.app.api.entity.AppTabbarEntity;
import com.pig4cloud.pigx.app.api.enums.PageTypeEnums;
import com.pig4cloud.pigx.app.api.vo.AppDecorateVO;
import com.pig4cloud.pigx.app.service.AppPageService;
import com.pig4cloud.pigx.app.service.AppTabbarService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * App 装修页面后台管理接口。
 * <p>
 * 页面表只保存页面装修组件 JSON，底部 Tabbar 由 {@code app_tabbar} 独立维护；
 * 查询装修页时会聚合 Tabbar，便于后台预览。
 *
 * @author pig
 * @date 2023-06-07 16:32:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/appPage")
@Tag(description = "appPage", name = "页面管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppPageController {

    private final AppPageService pageService;

    private final AppTabbarService tabbarService;

    /**
     * 按页面类型查询后台装修页配置。
     * <p>
     * 返回结构保持页面字段在顶层，并额外携带完整 Tabbar 列表，用于后台装修预览和 Tabbar 设置区初始化。
     *
     * @param pageType 页面类型，见 {@link PageTypeEnums}
     * @return 页面装修数据和完整 Tabbar 列表
     */
    @Operation(summary = "通过页面类型查询", description = "通过页面类型查询")
    @GetMapping("/{pageType}")
    public R getByPageType(@PathVariable("pageType") Integer pageType) {
        AppPageEntity appPage = pageService.getOne(Wrappers.<AppPageEntity>lambdaQuery()
                .eq(AppPageEntity::getPageType, pageType)
                .orderByDesc(AppPageEntity::getCreateTime), false);
        List<AppTabbarEntity> tabbarList = tabbarService
                .list(Wrappers.<AppTabbarEntity>lambdaQuery().orderByAsc(AppTabbarEntity::getSortOrder).orderByAsc(AppTabbarEntity::getId));
        return R.ok(AppDecorateVO.of(appPage, tabbarList));
    }

    /**
     * 保存页面装修数据。
     * <p>
     * 同一页面类型只保留一份最新配置；保存时先删除旧记录，再写入新记录。
     * Tabbar 不随页面一起保存，需通过 {@link AppTabbarController} 单独维护。
     *
     * @param page 页面
     * @return 保存结果
     */
    @Operation(summary = "更新页面", description = "更新页面")
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public R update(@RequestBody AppPageEntity page) {
        List<AppPageEntity> pageEntityList = pageService
                .list(Wrappers.<AppPageEntity>lambdaQuery().eq(AppPageEntity::getPageType, page.getPageType()));

        if (CollUtil.isNotEmpty(pageEntityList)) {
            // app_page 按页面类型覆盖保存，避免同一页面类型存在多份装修数据。
            pageService.removeByIds(pageEntityList.stream().map(AppPageEntity::getId).toList());
        }

        page.setPageName(PageTypeEnums.getNameByType(page.getPageType()));
        return R.ok(pageService.save(page));
    }

}
