package com.pig4cloud.pigx.app.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.app.api.entity.AppArticleCategoryEntity;
import com.pig4cloud.pigx.app.service.AppArticleCategoryService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章分类表
 *
 * @author pig
 * @date 2023-06-07 16:28:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/appArticleCategory")
@Tag(description = "appArticleCategory", name = "文章分类表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppArticleCategoryController {

	private final AppArticleCategoryService appArticleCategoryService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param appArticleCategory 文章分类表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("app_appArticleCategory_view")
	public R getAppArticleCategoryPage(@ParameterObject Page page,
			@ParameterObject AppArticleCategoryEntity appArticleCategory) {
		LambdaQueryWrapper<AppArticleCategoryEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StrUtil.isNotBlank(appArticleCategory.getName()), AppArticleCategoryEntity::getName,
				appArticleCategory.getName());
		return R.ok(appArticleCategoryService.page(page, wrapper));
	}

	/**
	 * 通过id查询文章分类表
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	@HasPermission("app_appArticleCategory_view")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(appArticleCategoryService.getById(id));
	}

	/**
	 * 通过查询文章分类
	 * @return R
	 */
	@Operation(summary = "查询文章分类", description = "查询文章分类")
	@Inner(value = false)
	@GetMapping("/list")
	public R list() {
		return R.ok(appArticleCategoryService.list());
	}

	/**
	 * 新增文章分类表
	 * @param appArticleCategory 文章分类表
	 * @return R
	 */
	@Operation(summary = "新增文章分类表", description = "新增文章分类表")
	@SysLog("新增文章分类表")
	@PostMapping
	@HasPermission("app_appArticleCategory_add")
	public R save(@RequestBody AppArticleCategoryEntity appArticleCategory) {
		return R.ok(appArticleCategoryService.save(appArticleCategory));
	}

	/**
	 * 修改文章分类表
	 * @param appArticleCategory 文章分类表
	 * @return R
	 */
	@Operation(summary = "修改文章分类表", description = "修改文章分类表")
	@SysLog("修改文章分类表")
	@PutMapping
	@HasPermission("app_appArticleCategory_edit")
	public R updateById(@RequestBody AppArticleCategoryEntity appArticleCategory) {
		return R.ok(appArticleCategoryService.updateById(appArticleCategory));
	}

	/**
	 * 通过id删除文章分类表
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除文章分类表", description = "通过id删除文章分类表")
	@SysLog("通过id删除文章分类表")
	@DeleteMapping
	@HasPermission("app_appArticleCategory_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(appArticleCategoryService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 * @param appArticleCategory 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("app_appArticleCategory_export")
	public List<AppArticleCategoryEntity> export(AppArticleCategoryEntity appArticleCategory) {
		return appArticleCategoryService.list(Wrappers.query(appArticleCategory));
	}

}
