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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysDeptRelation;
import com.pig4cloud.pigx.admin.api.vo.DeptExcelVo;
import com.pig4cloud.pigx.admin.mapper.SysDeptMapper;
import com.pig4cloud.pigx.admin.service.SysDeptRelationService;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.excel.vo.ErrorMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.*;
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
	public Boolean removeDeptById(Long id) {
		// 级联删除部门
		List<Long> idList = sysDeptRelationService
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
	 * 导出部门
	 * @return
	 */
	@Override
	public List<DeptExcelVo> listExcelVo() {
		List<SysDept> list = this.list();
		List<DeptExcelVo> deptExcelVos = list.stream().map(item -> {
			DeptExcelVo deptExcelVo = new DeptExcelVo();
			deptExcelVo.setName(item.getName());
			Optional<String> first = this.list().stream().filter(it -> item.getParentId().equals(it.getDeptId()))
					.map(SysDept::getName).findFirst();
			deptExcelVo.setParentName(first.orElse("跟部门"));
			deptExcelVo.setSortOrder(item.getSortOrder());
			return deptExcelVo;
		}).collect(Collectors.toList());
		return deptExcelVos;
	}

	@Override
	public R importDept(List<DeptExcelVo> excelVOList, BindingResult bindingResult) {
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

		List<SysDept> deptList = this.list();
		for (DeptExcelVo item : excelVOList) {
			Set<String> errorMsg = new HashSet<>();
			boolean exsitUsername = deptList.stream().anyMatch(sysDept -> item.getName().equals(sysDept.getName()));
			if (exsitUsername) {
				errorMsg.add("部门名称已经存在");
			}
			SysDept one = this.getOne(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getName, item.getParentName()));
			if (item.getParentName().equals("跟部门")) {
				one = new SysDept();
				one.setDeptId(0L);
			}
			if (one == null) {
				errorMsg.add("上级部门不存在");
			}
			if (CollUtil.isEmpty(errorMsg)) {
				SysDept sysDept = new SysDept();
				sysDept.setName(item.getName());
				sysDept.setParentId(one.getDeptId());
				sysDept.setSortOrder(item.getSortOrder());
				this.saveDept(sysDept);
			}
			else {
				// 数据不合法情况
				errorMessageList.add(new ErrorMessage(item.getLineNum(), errorMsg));
			}
		}
		if (CollUtil.isNotEmpty(errorMessageList)) {
			return R.failed(errorMessageList);
		}
		return R.ok(null, "部门导入成功");
	}

	/**
	 * 查询全部部门树
	 * @return 树 部门名称
	 * @param deptName
	 */
	@Override
	public List<Tree<Long>> selectTree(String deptName) {
		// 查询全部部门
		List<SysDept> deptAllList = deptMapper.selectList(
				Wrappers.<SysDept>lambdaQuery().like(StrUtil.isNotBlank(deptName), SysDept::getName, deptName));
		// 查询数据权限内部门
		List<Long> deptOwnIdList = deptMapper.selectListByScope(
				Wrappers.<SysDept>lambdaQuery().like(StrUtil.isNotBlank(deptName), SysDept::getName, deptName),
				DataScope.of()).stream().map(SysDept::getDeptId).collect(Collectors.toList());

		// 权限内部门
		List<TreeNode<Long>> collect = deptAllList.stream()
				.filter(dept -> dept.getDeptId().intValue() != dept.getParentId())
				.sorted(Comparator.comparingInt(SysDept::getSortOrder)).map(dept -> {
					TreeNode<Long> treeNode = new TreeNode();
					treeNode.setId(dept.getDeptId());
					treeNode.setParentId(dept.getParentId());
					treeNode.setName(dept.getName());
					treeNode.setWeight(dept.getSortOrder());
					// 有权限不返回标识
					Map<String, Object> extra = new HashMap<>(8);
					extra.put("isLock", !deptOwnIdList.contains(dept.getDeptId()));
					extra.put("createTime", dept.getCreateTime());
					treeNode.setExtra(extra);
					return treeNode;
				}).collect(Collectors.toList());

		// 模糊查询 不组装树结构 直接返回 表格方便编辑
		if (StrUtil.isNotBlank(deptName)) {
			return collect.stream().map(node -> {
				Tree<Long> tree = new Tree<>();
				tree.putAll(node.getExtra());
				BeanUtils.copyProperties(node, tree);
				return tree;
			}).collect(Collectors.toList());
		}

		return TreeUtil.build(collect, 0L);
	}

}
