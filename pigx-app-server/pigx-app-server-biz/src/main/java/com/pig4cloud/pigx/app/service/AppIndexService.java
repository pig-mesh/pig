package com.pig4cloud.pigx.app.service;

import com.pig4cloud.pigx.app.api.entity.AppPageEntity;

import java.util.Map;

/**
 * app 页面控制
 *
 * @author lengleng
 * @date 2023/6/8
 */
public interface AppIndexService {

	/**
	 * 首页
	 */
	Map<String, Object> index();

	/**
	 * 配置
	 * @return Map<String, Object>
	 */
	Map<String, Object> config();

	/**
	 * 装修
	 * @param id 装修ID
	 * @return Map<String, Object>
	 */
	AppPageEntity decorate(Integer id);

}
