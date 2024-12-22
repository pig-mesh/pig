package com.pig4cloud.pigx.app.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.app.api.entity.AppPageEntity;
import com.pig4cloud.pigx.app.api.enums.PageTypeEnums;
import com.pig4cloud.pigx.app.service.AppPageService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 页面管理
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

    /**
     * 查询文章资讯
     *
     * @param pageType pageType
     * @return R
     */
    @Operation(summary = "通过页面类型查询", description = "通过页面类型查询")
    @GetMapping("/{pageType}")
    public R getByPageType(@PathVariable("pageType") Long pageType) {
        AppPageEntity appPage = pageService.getOne(Wrappers.<AppPageEntity>lambdaQuery().eq(AppPageEntity::getPageType, pageType));
        return R.ok(appPage);
    }

    /**
     * 更新
     *
     * @param page 页面
     * @return {@link R }
     */
    @Operation(summary = "更新页面", description = "更新页面")
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public R update(@RequestBody AppPageEntity page) {
        List<AppPageEntity> pageEntityList = pageService
                .list(Wrappers.<AppPageEntity>lambdaQuery().eq(AppPageEntity::getPageType, page.getPageType()));

        if (CollUtil.isNotEmpty(pageEntityList)) {
            // 删掉原来类型的页面数据
            pageService.removeByIds(pageEntityList.stream().map(AppPageEntity::getId).collect(Collectors.toList()));
        }

        page.setPageName(PageTypeEnums.getNameByType(page.getPageType()));
        return R.ok(pageService.save(page));
    }

}
