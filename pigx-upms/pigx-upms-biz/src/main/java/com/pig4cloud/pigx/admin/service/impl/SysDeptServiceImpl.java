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
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysDeptRelation;
import com.pig4cloud.pigx.admin.mapper.SysDeptMapper;
import com.pig4cloud.pigx.admin.service.SysDeptRelationService;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

	private final SysDeptRelationService sysDeptRelationService;

	private final SysDeptMapper deptMapper;

	/**
	 * 添加信息部门
	 * @param dept 部门
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveDept(SysDept dept) {
		SysDept sysDept = new SysDept();
		BeanUtils.copyProperties(dept, sysDept);
		this.save(sysDept);
		sysDeptRelationService.insertDeptRelation(sysDept);
		return Boolean.TRUE;
	}

	/**
	 * 删除部门
	 * @param id 部门 ID
	 * @return 成功、失败
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeDeptById(Integer id) {
		// 级联删除部门
		List<Integer> idList = sysDeptRelationService
				.list(Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getAncestor, id)).stream()
				.map(SysDeptRelation::getDescendant).collect(Collectors.toList());

		if (CollUtil.isNotEmpty(idList)) {
			this.removeByIds(idList);
		}

		// 删除部门级联关系
		sysDeptRelationService.deleteAllDeptRealtion(id);
		return Boolean.TRUE;
	}

	/**
	 * 更新部门
	 * @param sysDept 部门信息
	 * @return 成功、失败
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateDeptById(SysDept sysDept) {
		// 更新部门状态
		this.updateById(sysDept);
		// 更新部门关系
		SysDeptRelation relation = new SysDeptRelation();
		relation.setAncestor(sysDept.getParentId());
		relation.setDescendant(sysDept.getDeptId());
		sysDeptRelationService.updateDeptRealtion(relation);
		return Boolean.TRUE;
	}

	/**
	 * 查询全部部门树
	 * @return 树
	 */
	@Override
	public List<Tree<Integer>> selectTree() {
		// 查询全部部门
		List<SysDept> deptAllList = deptMapper.selectList(Wrappers.emptyWrapper());
		// 查询数据权限内部门
		List<Integer> deptOwnIdList = deptMapper.selectListByScope(Wrappers.emptyWrapper(), new DataScope()).stream()
				.map(SysDept::getDeptId).collect(Collectors.toList());

		// 权限内部门
		List<TreeNode<Integer>> collect = deptAllList.stream()
				.filter(dept -> dept.getDeptId().intValue() != dept.getParentId())
				.sorted(Comparator.comparingInt(SysDept::getSort)).map(dept -> {
					TreeNode<Integer> treeNode = new TreeNode();
					treeNode.setId(dept.getDeptId());
					treeNode.setParentId(dept.getParentId());
					treeNode.setName(dept.getName());
					treeNode.setWeight(dept.getSort());
					// 有权限不返回标识
					Map<String, Object> extra = new HashMap<>(8);
					extra.put("isLock", !deptOwnIdList.contains(dept.getDeptId()));
					extra.put("createTime", dept.getCreateTime());
					treeNode.setExtra(extra);
					return treeNode;
				}).collect(Collectors.toList());

		return TreeUtil.build(collect, 0);
	}

}
