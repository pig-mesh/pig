package com.pig4cloud.pigx.app.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.app.api.entity.AppPageEntity;
import com.pig4cloud.pigx.app.service.AppPageService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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
    public R update(@RequestBody AppPageEntity page) {
        return R.ok(pageService.saveOrUpdate(page));
    }

}
