package com.pig4cloud.pigx.app.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.pig4cloud.pigx.app.api.entity.AppArticleCategoryEntity;
import com.pig4cloud.pigx.app.api.entity.AppArticleCollectEntity;
import com.pig4cloud.pigx.app.api.entity.AppArticleEntity;
import com.pig4cloud.pigx.app.mapper.AppArticleCollectMapper;
import com.pig4cloud.pigx.app.mapper.AppArticleMapper;
import com.pig4cloud.pigx.app.service.AppArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 文章资讯服务实现。
 * <p>
 * 负责文章列表关联分类名称、文章详情访问次数递增，以及当前用户收藏状态补充。
 *
 * @author pig
 * @date 2023-06-07 16:32:35
 */
@Service
@RequiredArgsConstructor
public class AppArticleServiceImpl extends ServiceImpl<AppArticleMapper, AppArticleEntity>
		implements AppArticleService {

	private final AppArticleCollectMapper collectMapper;

	/**
     * 获取文章详情，并尝试将阅读数加 1。
     * <p>
     * 如果传入用户 ID，会额外查询该用户是否已收藏当前文章。
     * 阅读数更新使用当前 visit 值作为条件，降低并发覆盖风险。
     *
     * @param id 文章 ID
     * @param userId 当前用户 ID，可为空
     * @return 文章详情
	 */
	@Override
	public AppArticleEntity getArticleAndIncrById(Long id, Long userId) {
		AppArticleEntity appArticleEntity = baseMapper.selectById(id);
        // 登录用户访问详情时，补充当前用户是否已收藏该文章。
		if (Objects.nonNull(userId)) {
			boolean exists = collectMapper.exists(Wrappers.<AppArticleCollectEntity>lambdaQuery()//
				.eq(AppArticleCollectEntity::getArticleId, appArticleEntity.getId())//
				.eq(AppArticleCollectEntity::getUserId, userId));
			appArticleEntity.setCollect(exists);
		}

		Integer nowVisit = appArticleEntity.getVisit();
		appArticleEntity.setVisit(nowVisit + 1);
        // 使用旧 visit 值作为更新条件，避免并发访问时直接覆盖最新阅读数。
		baseMapper.update(appArticleEntity,
				Wrappers.<AppArticleEntity>lambdaQuery()
					.eq(AppArticleEntity::getId, id)
					.eq(AppArticleEntity::getVisit, nowVisit));
		return appArticleEntity;
	}

	/**
     * 分页查询文章列表，并关联分类名称。
     *
	 * @param page 分页参数
	 * @param appArticle 文章查询条件
     * @return 文章分页结果，包含分类名称 cname
	 */
	@Override
	public Page pageAndCname(Page page, AppArticleEntity appArticle) {
		MPJLambdaWrapper<AppArticleEntity> wrapper = new MPJLambdaWrapper<>();
		wrapper.selectAll(AppArticleEntity.class)
			.selectAs(AppArticleCategoryEntity::getName, AppArticleEntity.Fields.cname)
			.leftJoin(AppArticleCategoryEntity.class, AppArticleCategoryEntity::getId, AppArticleEntity::getCid)
			.like(StrUtil.isNotBlank(appArticle.getAuthor()), AppArticleEntity::getAuthor, appArticle.getAuthor())
			.like(StrUtil.isNotBlank(appArticle.getTitle()), AppArticleEntity::getTitle, appArticle.getTitle())
			.eq(Objects.nonNull(appArticle.getCid()), AppArticleEntity::getCid, appArticle.getCid());
		return this.page(page, wrapper);
	}

}
