package com.pig4cloud.pigx.app.service;

import com.github.yulichang.base.MPJBaseService;
import com.pig4cloud.pigx.app.api.entity.AppArticleCollectEntity;

/**
 * 文章收藏服务。
 * <p>
 * 收藏记录以当前登录用户为维度保存，同一用户对同一文章只允许收藏一次。
 */
public interface AppArticleCollectService extends MPJBaseService<AppArticleCollectEntity> {

    /**
     * 保存当前用户的文章收藏。
     *
     * @param appArticleCollect 收藏请求，需包含文章 ID
     * @return true=保存成功，false=已收藏或未保存
     */
	Boolean saveArticleCollect(AppArticleCollectEntity appArticleCollect);

}
