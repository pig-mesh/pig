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

package com.pig4cloud.pig.admin.service.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysPublicParam;
import com.pig4cloud.pig.admin.mapper.SysPublicParamMapper;
import com.pig4cloud.pig.admin.service.SysPublicParamService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.enums.DictTypeEnum;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;

/**
 * 系统公共参数服务实现类
 *
 * @author lengleng
 * @date 2025/05/30
 */
@Service
@AllArgsConstructor
public class SysPublicParamServiceImpl extends ServiceImpl<SysPublicParamMapper, SysPublicParam>
		implements SysPublicParamService {

	/**
	 * 根据公共参数key获取对应的value值
	 * @param publicKey 公共参数key
	 * @return 公共参数value，未找到时返回null
	 * @Cacheable 使用缓存，缓存名称为PARAMS_DETAILS，key为publicKey，当结果为null时不缓存
	 */
	@Override
	@Cacheable(value = CacheConstants.PARAMS_DETAILS, key = "#publicKey", unless = "#result == null ")
	public String getParamValue(String publicKey) {
		SysPublicParam sysPublicParam = this.baseMapper
			.selectOne(Wrappers.<SysPublicParam>lambdaQuery().eq(SysPublicParam::getPublicKey, publicKey));

		if (sysPublicParam != null) {
			return sysPublicParam.getPublicValue();
		}
		return null;
	}

	/**
	 * 更新系统公共参数
	 * @param sysPublicParam 系统公共参数对象
	 * @return 操作结果
	 * @see R
	 */
	@Override
	@CacheEvict(value = CacheConstants.PARAMS_DETAILS, key = "#sysPublicParam.publicKey")
	public R updateParam(SysPublicParam sysPublicParam) {
		SysPublicParam param = this.getById(sysPublicParam.getPublicId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(param.getSystemFlag())) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_PARAM_DELETE_SYSTEM));
		}
		return R.ok(this.updateById(sysPublicParam));
	}

	/**
	 * 根据ID列表删除参数
	 * @param publicIds 参数ID数组
	 * @return 操作结果
	 * @see CacheConstants#PARAMS_DETAILS 缓存常量
	 */
	@Override
	@CacheEvict(value = CacheConstants.PARAMS_DETAILS, allEntries = true)
	public R removeParamByIds(Long[] publicIds) {
		List<Long> idList = this.baseMapper.selectByIds(CollUtil.toList(publicIds))
			.stream()
			.filter(p -> !p.getSystemFlag().equals(DictTypeEnum.SYSTEM.getType()))// 系统内置的跳过不能删除
			.map(SysPublicParam::getPublicId)
			.toList();
		return R.ok(this.removeBatchByIds(idList));
	}

	/**
	 * 同步参数缓存
	 * @return 操作结果
	 */
	@Override
	@CacheEvict(value = CacheConstants.PARAMS_DETAILS, allEntries = true)
	public R syncParamCache() {
		return R.ok();
	}

}
