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

package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.dto.SysOauthClientDetailsDTO;
import com.pig4cloud.pigx.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pigx.admin.config.ClientDetailsInitRunner;
import com.pig4cloud.pigx.admin.mapper.SysOauthClientDetailsMapper;
import com.pig4cloud.pigx.admin.service.SysOauthClientDetailsService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
	 * 根据客户端信息
	 * @param clientDetailsDTO
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientDetailsDTO.clientId")
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateClientById(SysOauthClientDetailsDTO clientDetailsDTO) {
		this.insertOrUpdate(clientDetailsDTO);
		return Boolean.TRUE;
	}

	/**
	 * 添加客户端
	 * @param clientDetailsDTO
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveClient(SysOauthClientDetailsDTO clientDetailsDTO) {
		this.insertOrUpdate(clientDetailsDTO);
		return Boolean.TRUE;
	}

	/**
	 * 插入或更新客户端对象
	 * @param clientDetailsDTO
	 * @return
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
		// 更新Redis
		SpringContextHolder.publishEvent(new ClientDetailsInitRunner.ClientDetailsInitEvent(clientDetails));
		return clientDetails;
	}

	/**
	 * 分页查询客户端信息
	 * @param page
	 * @param query
	 * @return
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
		}).collect(Collectors.toList());

		// 构建dto page 对象
		Page<SysOauthClientDetailsDTO> dtoPage = new Page<>(page.getCurrent(), page.getSize(), selectPage.getTotal());
		dtoPage.setRecords(collect);
		return dtoPage;
	}

	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, allEntries = true)
	public R syncClientCache() {
		// 更新Redis
		SpringContextHolder.publishEvent(new ClientDetailsInitRunner.ClientDetailsInitEvent(this));
		return R.ok();
	}

}
