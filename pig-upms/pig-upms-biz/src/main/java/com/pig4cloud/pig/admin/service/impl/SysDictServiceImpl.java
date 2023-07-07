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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysDict;
import com.pig4cloud.pig.admin.api.entity.SysDictItem;
import com.pig4cloud.pig.admin.mapper.SysDictItemMapper;
import com.pig4cloud.pig.admin.mapper.SysDictMapper;
import com.pig4cloud.pig.admin.service.SysDictService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.enums.DictTypeEnum;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典表
 *
 * @author lengleng
 * @date 2019/03/19
 */
@Service
@AllArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

	private final SysDictItemMapper dictItemMapper;

	/**
	 * 根据ID 删除字典
	 * @param ids 字典ID 列表
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public R removeDictByIds(Long[] ids) {

		List<Long> dictIdList = baseMapper.selectBatchIds(CollUtil.toList(ids))
			.stream()
			.filter(sysDict -> !sysDict.getSystemFlag().equals(DictTypeEnum.SYSTEM.getType()))// 系统内置类型不删除
			.map(SysDict::getId)
			.collect(Collectors.toList());

		baseMapper.deleteBatchIds(dictIdList);

		dictItemMapper.delete(Wrappers.<SysDictItem>lambdaQuery().in(SysDictItem::getDictId, dictIdList));
		return R.ok();
	}

	/**
	 * 更新字典
	 * @param dict 字典
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, key = "#dict.dictType")
	public R updateDict(SysDict dict) {
		SysDict sysDict = this.getById(dict.getId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(sysDict.getSystemFlag())) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_DICT_UPDATE_SYSTEM));
		}
		this.updateById(dict);
		return R.ok(dict);
	}

	/**
	 * 同步缓存 （清空缓存）
	 * @return R
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public R syncDictCache() {
		return R.ok();
	}

}
