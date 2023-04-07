/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.act.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.activiti.engine.repository.Model;

import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/25
 */
public interface ModelService {

	/**
	 * 创建流程
	 * @param name
	 * @param key
	 * @param desc
	 * @param category
	 * @return
	 */
	Model create(String name, String key, String desc, String category);

	/**
	 * 分页获取流程
	 * @param params
	 * @return
	 */
	IPage<Model> getModelPage(Map<String, Object> params);

	/**
	 * 删除流程
	 * @param ids
	 * @return
	 */
	Boolean removeModelById(String[] ids);

	/**
	 * 部署流程
	 * @param id
	 * @return
	 */
	Boolean deploy(String id);

}
