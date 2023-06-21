package com.pig4cloud.pigx.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.app.api.entity.AppArticleEntity;

public interface AppArticleService extends IService<AppArticleEntity> {

	/**
	 * 获取文章并使阅读数+1
	 * @param id id
	 * @return
	 */
	AppArticleEntity getArticleAndIncrById(Long id, Long userId);

}
