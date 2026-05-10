package com.pig4cloud.pigx.app.controller;

import com.pig4cloud.pigx.app.service.AppIndexService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * App 运行时页面接口。
 * <p>
 * 这里的接口主要给移动端调用，返回首页、装修页、底部 Tabbar 等运行时数据。
 * 部分接口允许匿名访问，具体可见方法上的 {@link Inner} 配置。
 *
 * @author lengleng
 * @date 2023/6/8
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/index")
@Tag(description = "App 页面控制", name = "app index")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppIndexController {

    private final AppIndexService indexService;

    /**
     * 获取首页基础数据。
     * <p>
     * 当前返回首页装修页记录和资讯列表，适合旧版首页一次性拉取基础内容。
     *
     * @return 首页页面数据和资讯列表
     */
    @Inner(value = false)
    @GetMapping("/index")
    public R index() {
        return R.ok(indexService.index());
    }

    /**
     * 获取移动端底部导航配置。
     * <p>
     * 未登录时只返回公开 Tabbar；登录后返回公开 Tabbar + 当前用户角色授权的 Tabbar。
     *
     * @return 当前访问身份可见的 Tabbar 列表
     */
    @Inner(value = false)
    @GetMapping("/config")
    public R config() {
        return R.ok(indexService.config());
    }

    /**
     * 获取指定页面类型的装修数据。
     * <p>
     * 返回页面装修 JSON，同时聚合当前访问身份可见的 Tabbar，方便移动端一次请求完成页面渲染。
     *
     * @param pageType 页面类型
     * @return 装修页聚合数据
     */
    @Inner(value = false)
    @GetMapping("/decorate")
    public R decorate(@Validated @RequestParam("id") Integer pageType) {
        return R.ok(indexService.decorate(pageType));
    }

    /**
     * 获取工作台页面装修数据。
     * <p>
     * 工作台内部按钮权限复用 B 端权限体系，这里不再按 App 专属菜单做二次过滤。
     *
     * @return 工作台页面数据
     */
    @GetMapping("/workbench")
    public R workbench() {
        return R.ok(indexService.workbench());
    }

}
