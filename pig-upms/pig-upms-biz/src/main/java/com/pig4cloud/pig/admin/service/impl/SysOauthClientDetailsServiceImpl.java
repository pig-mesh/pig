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

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.dto.SysOauthClientDetailsDTO;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.admin.mapper.SysOauthClientDetailsMapper;
import com.pig4cloud.pig.admin.service.SysOauthClientDetailsService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-05-15
 */
@Service
@RequiredArgsConstructor
public class SysOauthClientDetailsServiceImpl extends ServiceImpl<SysOauthClientDetailsMapper, SysOauthClientDetails>
		implements SysOauthClientDetailsService {

	/**
	 * 更新 OAuth 客户端配置，并清空客户端详情缓存。
	 * <p>
	 * 编辑时允许调整客户端ID，清空全部客户端缓存可以避免旧 clientId 对应的缓存继续生效。
	 * @param clientDetailsDTO 客户端配置传输对象，必须包含主键和客户端ID
	 * @return 更新是否成功
	 */
	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateClientById(SysOauthClientDetailsDTO clientDetailsDTO) {
		this.insertOrUpdate(clientDetailsDTO);
		return Boolean.TRUE;
	}

	/**
	 * 新增 OAuth 客户端配置，并失效对应客户端详情缓存。
	 * @param clientDetailsDTO 客户端配置传输对象，必须包含客户端ID、密钥和授权范围
	 * @return 新增是否成功
	 */
	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientDetailsDTO.clientId")
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveClient(SysOauthClientDetailsDTO clientDetailsDTO) {
		this.insertOrUpdate(clientDetailsDTO);
		return Boolean.TRUE;
	}

	/**
	 * 插入或更新客户端对象，并把页面上的验证码、加密和在线数量开关写回扩展信息。
	 * @param clientDetailsDTO 客户端配置传输对象
	 * @return 已持久化的客户端实体
	 */
	private SysOauthClientDetails insertOrUpdate(SysOauthClientDetailsDTO clientDetailsDTO) {
		// copy dto 对象
		SysOauthClientDetails clientDetails = new SysOauthClientDetails();
		BeanUtils.copyProperties(clientDetailsDTO, clientDetails);

		// 获取扩展信息,插入开关相关
		String information = clientDetailsDTO.getAdditionalInformation();
		JSONObject informationObj = JSONUtil.parseObj(information)
			.set(CommonConstants.CAPTCHA_FLAG, clientDetailsDTO.getCaptchaFlag())
			.set(CommonConstants.ENC_FLAG, clientDetailsDTO.getEncFlag())
			.set(CommonConstants.ONLINE_QUANTITY, clientDetailsDTO.getOnlineQuantity());
		clientDetails.setAdditionalInformation(informationObj.toString());

		// 更新数据库
		saveOrUpdate(clientDetails);
		return clientDetails;
	}

	/**
	 * 分页查询客户端信息，并把扩展信息中的开关字段展开为 DTO 字段。
	 * @param page 分页参数
	 * @param query 客户端查询条件
	 * @return 客户端配置分页数据
	 */
	@Override
	public Page queryPage(Page page, SysOauthClientDetails query) {
		Page<SysOauthClientDetails> selectPage = baseMapper.selectPage(page, Wrappers.query(query));

		// 处理扩展字段组装dto
		List<SysOauthClientDetailsDTO> collect = selectPage.getRecords().stream().map(details -> {
			String information = details.getAdditionalInformation();
			String captchaFlag = JSONUtil.parseObj(information).getStr(CommonConstants.CAPTCHA_FLAG);
			String encFlag = JSONUtil.parseObj(information).getStr(CommonConstants.ENC_FLAG);
			String onlineQuantity = JSONUtil.parseObj(information).getStr(CommonConstants.ONLINE_QUANTITY);
			SysOauthClientDetailsDTO dto = new SysOauthClientDetailsDTO();
			BeanUtils.copyProperties(details, dto);
			dto.setCaptchaFlag(captchaFlag);
			dto.setEncFlag(encFlag);
			dto.setOnlineQuantity(onlineQuantity);
			return dto;
		}).toList();

		// 构建dto page 对象
		Page<SysOauthClientDetailsDTO> dtoPage = new Page<>(page.getCurrent(), page.getSize(), selectPage.getTotal());
		dtoPage.setRecords(collect);
		return dtoPage;
	}

	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, allEntries = true)
	public R syncClientCache() {
		// 清空客户端缓存，下次访问时重新查库
		return R.ok();
	}

	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, allEntries = true)
	public Boolean removeClientByIds(Long[] ids) {
		return removeBatchByIds(CollUtil.toList(ids));
	}

}
