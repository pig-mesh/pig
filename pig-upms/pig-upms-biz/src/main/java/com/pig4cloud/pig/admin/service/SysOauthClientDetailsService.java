/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
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
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.common.core.util.R;

/**
 * OAuth2客户端详情服务接口
 *
 * @author lengleng
 * @since 2018-05-15
 */
public interface SysOauthClientDetailsService extends IService<SysOauthClientDetails> {

	/**
	 * 根据客户端信息更新客户端详情
	 * @param clientDetails 客户端详情信息
	 * @return 更新结果，成功返回true
	 */
	Boolean updateClientById(SysOauthClientDetails clientDetails);

	/**
	 * 保存客户端信息
	 * @param clientDetails 客户端详细信息
	 * @return 操作是否成功
	 */
	Boolean saveClient(SysOauthClientDetails clientDetails);

	/**
	 * 分页查询OAuth客户端详情
	 * @param page 分页参数
	 * @param query 查询条件
	 * @return 分页查询结果
	 */
	Page getClientPage(Page page, SysOauthClientDetails query);

	/**
	 * 同步客户端缓存
	 * @return 操作结果
	 */
	R syncClientCache();

}
