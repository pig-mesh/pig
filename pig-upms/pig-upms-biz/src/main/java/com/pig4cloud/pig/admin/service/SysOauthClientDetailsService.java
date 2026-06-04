/*
 *
 *      Copyright (c) 2018-2026, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.dto.SysOauthClientDetailsDTO;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.common.core.util.R;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-05-15
 */
public interface SysOauthClientDetailsService extends IService<SysOauthClientDetails> {

	/**
	 * 更新 OAuth 客户端配置，并失效客户端详情缓存。
	 * @param clientDetailsDTO 客户端配置传输对象，必须包含主键和客户端ID
	 * @return 更新是否成功
	 */
	Boolean updateClientById(SysOauthClientDetailsDTO clientDetailsDTO);

	/**
	 * 新增 OAuth 客户端配置，并失效对应客户端详情缓存。
	 * @param clientDetailsDTO 客户端配置传输对象，必须包含客户端ID、密钥和授权范围
	 * @return 新增是否成功
	 */
	Boolean saveClient(SysOauthClientDetailsDTO clientDetailsDTO);

	/**
	 * 分页查询 OAuth 客户端配置。
	 * @param page 分页参数
	 * @param query 客户端查询条件，可为空字段表示不参与过滤
	 * @return 客户端配置分页数据
	 */
	Page queryPage(Page page, SysOauthClientDetails query);

	/**
	 * 清空客户端详情缓存，下次认证访问时重新查库。
	 * @return 操作结果
	 */
	R syncClientCache();

	/**
	 * 按主键批量删除客户端并失效客户端缓存。
	 * @param ids 主键ID 列表
	 * @return 是否成功
	 */
	Boolean removeClientByIds(Long[] ids);

}
