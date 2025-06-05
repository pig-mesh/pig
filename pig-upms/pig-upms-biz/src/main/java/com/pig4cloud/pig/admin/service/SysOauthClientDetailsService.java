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
 * 系统OAuth客户端详情服务接口
 * <p>
 * 提供OAuth客户端详情的增删改查及缓存同步功能
 * </p>
 *
 * @author lengleng
 * @since 2018-05-15
 */
public interface SysOauthClientDetailsService extends IService<SysOauthClientDetails> {

	/**
	 * 根据客户端信息更新客户端
	 * @param clientDetails 客户端详细信息
	 * @return 更新是否成功
	 */
	Boolean updateClientById(SysOauthClientDetails clientDetails);

	/**
	 * 保存客户端信息
	 * @param clientDetails 客户端详细信息
	 * @return 保存成功返回true，否则返回false
	 */
	Boolean saveClient(SysOauthClientDetails clientDetails);

	/**
	 * 分页查询客户端信息
	 * @param page 分页参数
	 * @param query 查询条件
	 * @return 分页结果
	 */
	Page queryPage(Page page, SysOauthClientDetails query);

	/**
	 * 同步客户端缓存（清空缓存）
	 * @return 操作结果
	 */
	R syncClientCache();

}
