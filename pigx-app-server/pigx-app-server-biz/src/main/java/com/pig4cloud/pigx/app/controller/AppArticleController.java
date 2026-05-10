package com.pig4cloud.pigx.app.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.app.api.entity.AppArticleEntity;
import com.pig4cloud.pigx.app.service.AppArticleService;
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
 * 文章资讯
 *
 * @author pig
 * @date 2023-06-07 16:32:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/appArticle")
@Tag(description = "appArticle", name = "文章资讯管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppArticleController {

	private final AppArticleService appArticleService;

	/**
     * 分页查询文章资讯。
     * <p>
     * 返回文章列表时会关联分类名称，供 App 资讯列表和后台管理复用。
     *
	 * @param page 分页对象
	 * @param appArticle 文章资讯
     * @return 文章分页结果
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@Inner(value = false)
	@GetMapping("/page")
	public R getAppArticlePage(@ParameterObject Page page, @ParameterObject AppArticleEntity appArticle) {
		return R.ok(appArticleService.pageAndCname(page, appArticle));
	}

	/**
     * 查询文章详情。
     * <p>
     * 会递增访问次数；如果传入 userId，则额外返回该用户是否收藏当前文章。
     *
     * @param id 文章 ID
     * @param userId 用户 ID，可为空
     * @return 文章详情
	 */
	@Inner(value = false)
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/details/{id}/{userId}")
	public R getById(@PathVariable("id") Long id, @PathVariable(required = false) Long userId) {
		return R.ok(appArticleService.getArticleAndIncrById(id, userId));
	}

	/**
	 * 新增文章资讯
	 * @param appArticle 文章资讯
	 * @return R
	 */
	@Operation(summary = "新增文章资讯", description = "新增文章资讯")
	@SysLog("新增文章资讯")
	@PostMapping
	@HasPermission("app_appArticle_add")
	public R save(@RequestBody AppArticleEntity appArticle) {
		return R.ok(appArticleService.save(appArticle));
	}

	/**
	 * 修改文章资讯
	 * @param appArticle 文章资讯
	 * @return R
	 */
	@Operation(summary = "修改文章资讯", description = "修改文章资讯")
	@SysLog("修改文章资讯")
	@PutMapping
	@HasPermission("app_appArticle_edit")
	public R updateById(@RequestBody AppArticleEntity appArticle) {
		return R.ok(appArticleService.updateById(appArticle));
	}

	/**
	 * 通过id删除文章资讯
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除文章资讯", description = "通过id删除文章资讯")
	@SysLog("通过id删除文章资讯")
	@DeleteMapping
	@HasPermission("app_appArticle_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(appArticleService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 * @param appArticle 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("app_appArticle_export")
	public List<AppArticleEntity> export(AppArticleEntity appArticle) {
		return appArticleService.list(Wrappers.query(appArticle));
	}

}
