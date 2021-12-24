/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.entity.SysDeptRelation;
import com.pig4cloud.pig.admin.mapper.SysDeptRelationMapper;
import com.pig4cloud.pig.admin.service.SysDeptRelationService;
import lombok.RequiredArgsConstructor;
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
 * @since 2019/2/1
 */
@Service
@RequiredArgsConstructor
public class SysDeptRelationServiceImpl extends ServiceImpl<SysDeptRelationMapper, SysDeptRelation>
		implements SysDeptRelationService {

	private final SysDeptRelationMapper sysDeptRelationMapper;

	/**
	 * 维护部门关系
	 * @param sysDept 部门
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveDeptRelation(SysDept sysDept) {
		// 增加部门关系表
		List<SysDeptRelation> relationList = sysDeptRelationMapper.selectList(
				Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getDescendant, sysDept.getParentId()))
				.stream().map(relation -> {
					relation.setDescendant(sysDept.getDeptId());
					return relation;
				}).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(relationList)) {
			this.saveBatch(relationList);
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
	public void removeDeptRelationById(Long id) {
		baseMapper.deleteDeptRelationsById(id);
	}

	/**
	 * 更新部门关系
	 * @param relation
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateDeptRelation(SysDeptRelation relation) {
		baseMapper.deleteDeptRelations(relation);
		baseMapper.insertDeptRelations(relation);
	}

}
