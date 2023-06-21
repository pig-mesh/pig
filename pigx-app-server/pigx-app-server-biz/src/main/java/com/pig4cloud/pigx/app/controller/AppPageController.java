package com.pig4cloud.pigx.app.controller;

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
	 * 通过id查询文章资讯
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(pageService.getById(id));
	}

	@Operation(summary = "更新页面", description = "更新页面")
	@PutMapping
	public R update(@RequestBody AppPageEntity page) {
		return R.ok(pageService.updateById(page));
	}

}
