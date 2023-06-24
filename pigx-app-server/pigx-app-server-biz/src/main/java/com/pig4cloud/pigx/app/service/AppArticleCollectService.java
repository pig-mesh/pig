package com.pig4cloud.pigx.app.service;

import com.github.yulichang.base.MPJBaseService;
import com.pig4cloud.pigx.app.api.entity.AppArticleCollectEntity;

public interface AppArticleCollectService extends MPJBaseService<AppArticleCollectEntity> {

	Boolean saveArticleCollect(AppArticleCollectEntity appArticleCollect);

}
