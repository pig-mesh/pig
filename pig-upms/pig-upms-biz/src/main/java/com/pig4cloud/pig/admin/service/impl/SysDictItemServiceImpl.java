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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.constant.UpmsErrorCodes;
import com.pig4cloud.pig.admin.api.dto.SysDictItemSortDTO;
import com.pig4cloud.pig.admin.api.entity.SysDict;
import com.pig4cloud.pig.admin.api.entity.SysDictItem;
import com.pig4cloud.pig.admin.mapper.SysDictItemMapper;
import com.pig4cloud.pig.admin.service.SysDictItemService;
import com.pig4cloud.pig.admin.service.SysDictService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.enums.DictTypeEnum;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 字典项
 *
 * @author lengleng
 * @date 2019/03/19
 */
@Service
@AllArgsConstructor
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

	private final SysDictService dictService;

	/**
	 * 删除字典项
	 * @param id 字典项ID
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public R removeDictItem(Long id) {
		// 根据ID查询字典ID
		SysDictItem dictItem = this.getById(id);
		SysDict dict = dictService.getById(dictItem.getDictId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag())) {
			return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_DICT_DELETE_SYSTEM));
		}
		return R.ok(this.removeById(id));
	}

	/**
	 * 更新字典项
	 * @param item 字典项
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, key = "#item.dictType")
	public R updateDictItem(SysDictItem item) {
		// 查询字典
		SysDict dict = dictService.getById(item.getDictId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag())) {
			return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_DICT_UPDATE_SYSTEM));
		}
		return R.ok(this.updateById(item));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public R updateDictItemSort(SysDictItemSortDTO sortDTO) {
		SysDict dict = dictService.getById(sortDTO.getDictId());
		if (dict == null) {
			return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_DICT_NOT_FOUND));
		}
		if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag())) {
			return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_DICT_UPDATE_SYSTEM));
		}

		List<Long> itemIds = sortDTO.getItemIds();
		Set<Long> uniqueItemIds = new HashSet<>(itemIds);
		if (uniqueItemIds.size() != itemIds.size()) {
			return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_DICT_ITEM_SORT_DUPLICATE));
		}

		List<SysDictItem> sortedItems = this.list(Wrappers.<SysDictItem>lambdaQuery()
			.eq(SysDictItem::getDictId, sortDTO.getDictId())
			.orderByAsc(SysDictItem::getSortOrder)
			.orderByAsc(SysDictItem::getId));
		Map<Long, SysDictItem> itemMap = sortedItems.stream()
			.collect(Collectors.toMap(SysDictItem::getId, Function.identity()));

		if (!itemMap.keySet().containsAll(itemIds)) {
			return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_DICT_ITEM_SORT_SCOPE_INVALID));
		}

		// 兼容分页排序：以当前页字典项在全量列表中的最小位置作为插入点，将整页按新顺序回插
		Map<Long, Integer> itemIndexMap = new HashMap<>(sortedItems.size());
		for (int index = 0; index < sortedItems.size(); index++) {
			itemIndexMap.put(sortedItems.get(index).getId(), index);
		}
		int insertIndex = itemIds.stream().map(itemIndexMap::get).min(Integer::compareTo).orElse(sortedItems.size());

		List<SysDictItem> orderedPageItems = itemIds.stream().map(itemMap::get).toList();
		List<SysDictItem> reorderedItems = new ArrayList<>(sortedItems);
		reorderedItems.removeAll(orderedPageItems);
		reorderedItems.addAll(insertIndex, orderedPageItems);

		List<SysDictItem> updateList = new ArrayList<>(reorderedItems.size());
		for (int index = 0; index < reorderedItems.size(); index++) {
			SysDictItem updateItem = new SysDictItem();
			updateItem.setId(reorderedItems.get(index).getId());
			updateItem.setSortOrder(index);
			updateList.add(updateItem);
		}

		return R.ok(this.updateBatchById(updateList));
	}

}
