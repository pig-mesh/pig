package com.pig4cloud.pigx.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.app.api.entity.AppArticleEntity;
import com.pig4cloud.pigx.app.api.entity.AppPageEntity;
import com.pig4cloud.pigx.app.api.entity.AppTabbarEntity;
import com.pig4cloud.pigx.app.mapper.AppArticleMapper;
import com.pig4cloud.pigx.app.mapper.AppPageMapper;
import com.pig4cloud.pigx.app.mapper.AppTabbarMapper;
import com.pig4cloud.pigx.app.service.AppIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * app 页面控制
 *
 * @author lengleng
 * @date 2023/6/8
 */

@Service
@RequiredArgsConstructor
public class AppIndexServiceImpl implements AppIndexService {

	private final AppArticleMapper appArticleMapper;

	private final AppTabbarMapper appTabbarMapper;

	private final AppPageMapper appPageMapper;

	/**
	 * 商城首页 首页数据
	 */
	@Override
	public Map<String, Object> index() {
		Map<String, Object> response = new LinkedHashMap<>();
		AppPageEntity appPageEntity = appPageMapper.selectById(1);
		List<AppArticleEntity> articleList = appArticleMapper
			.selectList(Wrappers.<AppArticleEntity>lambdaQuery().orderByDesc(AppArticleEntity::getSort));
		response.put("pages", appPageEntity);
		response.put("article", articleList);
		return response;
	}

	/**
	 * 配置
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> config() {
		Map<String, Object> response = new LinkedHashMap<>();
		List<AppTabbarEntity> tabbarEntityList = appTabbarMapper.selectList(Wrappers.emptyWrapper());
		response.put("tabbar", tabbarEntityList);
		return response;
	}

	/**
	 * 装修
	 *
	 * @author fzr
	 * @param id 主键
	 * @return Map<String, Object>
	 */
	@Override
	public AppPageEntity decorate(Integer id) {
		return appPageMapper.selectById(id);
	}

}
