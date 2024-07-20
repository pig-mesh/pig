package com.pig4cloud.pigx.app.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.pig4cloud.pigx.app.api.entity.AppArticleCollectEntity;
import com.pig4cloud.pigx.app.api.entity.AppArticleEntity;
import com.pig4cloud.pigx.app.service.AppArticleCollectService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章收藏表
 *
 * @author pig
 * @date 2023-06-16 14:33:41
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/collect")
@Tag(description = "collect", name = "文章收藏表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppArticleCollectController {

	private final AppArticleCollectService appArticleCollectService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param appArticleCollect 文章收藏表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getAppArticleCollectPage(@ParameterObject Page page) {
		MPJLambdaWrapper<AppArticleCollectEntity> wrapper = new MPJLambdaWrapper<AppArticleCollectEntity>()
			.selectAll(AppArticleCollectEntity.class)
			.select(AppArticleEntity::getTitle)
			.leftJoin(AppArticleEntity.class, AppArticleEntity::getId, AppArticleCollectEntity::getArticleId)
			.eq(AppArticleCollectEntity::getUserId, SecurityUtils.getUser().getId());
		return R.ok(appArticleCollectService.selectJoinListPage(page, AppArticleCollectEntity.class, wrapper));
	}

	/**
	 * 通过id查询文章收藏表
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(appArticleCollectService.getById(id));
	}

	/**
	 * 新增文章收藏表
	 * @param appArticleCollect 文章收藏表
	 * @return R
	 */
	@Operation(summary = "新增文章收藏表", description = "新增文章收藏表")
	@SysLog("新增文章收藏表")
	@PostMapping
	public R save(@RequestBody AppArticleCollectEntity appArticleCollect) {
		return R.ok(appArticleCollectService.saveArticleCollect(appArticleCollect));
	}

	/**
	 * 修改文章收藏表
	 * @param appArticleCollect 文章收藏表
	 * @return R
	 */
	@Operation(summary = "修改文章收藏表", description = "修改文章收藏表")
	@SysLog("修改文章收藏表")
	@PutMapping
	public R updateById(@RequestBody AppArticleCollectEntity appArticleCollect) {
		return R.ok(appArticleCollectService.updateById(appArticleCollect));
	}

	/**
	 * 通过id删除文章收藏表
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除文章收藏表", description = "通过id删除文章收藏表")
	@SysLog("通过id删除文章收藏表")
	@DeleteMapping
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(appArticleCollectService.removeBatchByIds(CollUtil.toList(ids)));
	}

	@Operation(summary = "通过文章id删除文章收藏表", description = "通过文章id删除文章收藏表")
	@SysLog("通过文章id删除文章收藏表")
	@DeleteMapping("/{articleId}")
	public R removeByArticleId(@PathVariable String articleId) {
		Long id = SecurityUtils.getUser().getId();
		return R.ok(appArticleCollectService.remove(Wrappers.<AppArticleCollectEntity>lambdaQuery()
			.eq(AppArticleCollectEntity::getUserId, id)
			.eq(AppArticleCollectEntity::getArticleId, articleId)));
	}

	/**
	 * 导出excel 表格
	 * @param appArticleCollect 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("app_collect_export")
	public List<AppArticleCollectEntity> export(AppArticleCollectEntity appArticleCollect) {
		return appArticleCollectService.list(Wrappers.query(appArticleCollect));
	}

}
