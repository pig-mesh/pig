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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysDeptRelation;
import com.pig4cloud.pigx.admin.mapper.SysDeptRelationMapper;
import com.pig4cloud.pigx.admin.service.SysDeptRelationService;
import lombok.AllArgsConstructor;
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
 * @since 2018-02-12
 */
@Service
@AllArgsConstructor
public class SysDeptRelationServiceImpl extends ServiceImpl<SysDeptRelationMapper, SysDeptRelation>
		implements SysDeptRelationService {

	private final SysDeptRelationMapper sysDeptRelationMapper;

	/**
	 * 维护部门关系
	 * @param sysDept 部门
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertDeptRelation(SysDept sysDept) {
		// 增加部门关系表
		List<SysDeptRelation> relationList = sysDeptRelationMapper.selectList(
				Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getDescendant, sysDept.getParentId()))
				.stream().map(relation -> {
					relation.setDescendant(sysDept.getDeptId());
					return relation;
				}).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(relationList)) {
			relationList.forEach(ele -> baseMapper.insert(ele));
		}

		// 自己也要维护到关系表中
		SysDeptRelation own = new SysDeptRelation();
		own.setDescendant(sysDept.getDeptId());
		own.setAncestor(sysDept.getDeptId());
		sysDeptRelationMapper.insert(own);
	}

	/**
	 * 通过ID删除部门关系
	 * @param id
	 */
	@Override
	public void deleteAllDeptRealtion(Long id) {
		baseMapper.deleteDeptRelationsByDeptId(id);
	}

	/**
	 * 更新部门关系
	 * @param relation
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateDeptRealtion(SysDeptRelation relation) {
		baseMapper.deleteDeptRelations(relation);
		baseMapper.insertDeptRelations(relation);
	}

}
