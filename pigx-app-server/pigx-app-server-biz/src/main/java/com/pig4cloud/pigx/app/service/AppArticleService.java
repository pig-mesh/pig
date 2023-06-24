package com.pig4cloud.pigx.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.app.api.entity.AppArticleEntity;

public interface AppArticleService extends IService<AppArticleEntity> {

	/**
	 * 获取文章并使阅读数+1
	 * @param id id
	 * @return
	 */
	AppArticleEntity getArticleAndIncrById(Long id, Long userId);

	/**
	 * 分页查询文章列表 包含分类名称
	 * @param page 分页参数
	 * @param appArticle 文章查询条件
	 * @return
	 */
	Page pageAndCname(Page page, AppArticleEntity appArticle);

}
