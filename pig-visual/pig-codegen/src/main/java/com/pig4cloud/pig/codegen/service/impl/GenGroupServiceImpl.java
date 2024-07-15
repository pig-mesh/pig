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
package com.pig4cloud.pig.codegen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.entity.GenGroupEntity;
import com.pig4cloud.pig.codegen.entity.GenTemplateGroupEntity;
import com.pig4cloud.pig.codegen.mapper.GenGroupMapper;
import com.pig4cloud.pig.codegen.service.GenGroupService;
import com.pig4cloud.pig.codegen.service.GenTemplateGroupService;
import com.pig4cloud.pig.codegen.util.vo.GroupVO;
import com.pig4cloud.pig.codegen.util.vo.TemplateGroupDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 模板分组
 *
 * @author PIG
 * @date 2023-02-21 20:01:53
 */
@Slf4j
@Service
@AllArgsConstructor
public class GenGroupServiceImpl extends ServiceImpl<GenGroupMapper, GenGroupEntity> implements GenGroupService {

	private final GenTemplateGroupService genTemplateGroupService;

	/**
	 * 新增模板分组
	 * @param genTemplateGroup
	 */
	@Override
	public void saveGenGroup(TemplateGroupDTO genTemplateGroup) {
		// 1.保存group
		GenGroupEntity groupEntity = new GenGroupEntity();
		BeanUtil.copyProperties(genTemplateGroup, groupEntity);
		baseMapper.insert(groupEntity);
		// 2.保存关系
		List<GenTemplateGroupEntity> goals = new LinkedList<>();
		for (Long TemplateId : genTemplateGroup.getTemplateId()) {
			GenTemplateGroupEntity templateGroup = new GenTemplateGroupEntity();
			templateGroup.setTemplateId(TemplateId).setGroupId(groupEntity.getId());
			goals.add(templateGroup);
		}
		genTemplateGroupService.saveBatch(goals);

	}

	/**
	 * 按照ids删除
	 * @param ids groupIds
	 */
	@Override
	public void delGroupAndTemplate(Long[] ids) {
		// 删除分组
		this.removeBatchByIds(CollUtil.toList(ids));
		// 删除关系
		genTemplateGroupService
			.remove(Wrappers.<GenTemplateGroupEntity>lambdaQuery().in(GenTemplateGroupEntity::getGroupId, ids));
	}

	/**
	 * 按照id查询
	 * @param id
	 * @return
	 */
	@Override
	public GroupVO getGroupVoById(Long id) {
		return baseMapper.getGroupVoById(id);
	}

	/**
	 * 根据id更新
	 * @param groupVo
	 */
	@Override
	public void updateGroupAndTemplateById(GroupVO groupVo) {
		// 1.更新自身
		GenGroupEntity groupEntity = new GenGroupEntity();
		BeanUtil.copyProperties(groupVo, groupEntity);
		this.updateById(groupEntity);
		// 2.更新模板
		// 2.1根据id删除之前的模板
		genTemplateGroupService.remove(
				Wrappers.<GenTemplateGroupEntity>lambdaQuery().eq(GenTemplateGroupEntity::getGroupId, groupVo.getId()));
		// 2.2根据ids创建新的模板分组赋值
		List<GenTemplateGroupEntity> goals = new LinkedList<>();
		for (Long templateId : groupVo.getTemplateId()) {
			goals.add(new GenTemplateGroupEntity().setGroupId(groupVo.getId()).setTemplateId(templateId));
		}
		genTemplateGroupService.saveBatch(goals);
	}

}
