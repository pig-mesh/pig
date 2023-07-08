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

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.vo.DeptExcelVo;
import com.pig4cloud.pig.admin.mapper.SysDeptMapper;
import com.pig4cloud.pig.admin.service.SysDeptService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;
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

	private final SysDeptMapper deptMapper;

	/**
	 * 删除部门
	 * @param id 部门 ID
	 * @return 成功、失败
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeDeptById(Long id) {
		// 级联删除部门
		List<Long> idList = this.listDescendant(id).stream().map(SysDept::getDeptId).collect(Collectors.toList());

		Optional.ofNullable(idList).filter(CollUtil::isNotEmpty).ifPresent(this::removeByIds);

		return Boolean.TRUE;
	}

	/**
	 * 查询全部部门树
	 * @param deptName
	 * @return 树 部门名称
	 */
	@Override
	public List<Tree<Long>> selectTree(String deptName) {
		// 查询全部部门
		List<SysDept> deptAllList = deptMapper
			.selectList(Wrappers.<SysDept>lambdaQuery().like(StrUtil.isNotBlank(deptName), SysDept::getName, deptName));

		// 权限内部门
		List<TreeNode<Long>> collect = deptAllList.stream()
			.filter(dept -> dept.getDeptId().intValue() != dept.getParentId())
			.sorted(Comparator.comparingInt(SysDept::getSortOrder))
			.map(dept -> {
				TreeNode<Long> treeNode = new TreeNode();
				treeNode.setId(dept.getDeptId());
				treeNode.setParentId(dept.getParentId());
				treeNode.setName(dept.getName());
				treeNode.setWeight(dept.getSortOrder());
				// 有权限不返回标识
				Map<String, Object> extra = new HashMap<>(8);
				extra.put("createTime", dept.getCreateTime());
				treeNode.setExtra(extra);
				return treeNode;
			})
			.collect(Collectors.toList());

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
			Optional<String> first = this.list()
				.stream()
				.filter(it -> item.getParentId().equals(it.getDeptId()))
				.map(SysDept::getName)
				.findFirst();
			deptExcelVo.setParentName(first.orElse("根部门"));
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
			if (item.getParentName().equals("根部门")) {
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
				baseMapper.insert(sysDept);
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
	 * 查询所有子节点 （包含当前节点）
	 * @param deptId 部门ID 目标部门ID
	 * @return ID
	 */
	@Override
	public List<SysDept> listDescendant(Long deptId) {
		// 查询全部部门
		List<SysDept> allDeptList = baseMapper.selectList(Wrappers.emptyWrapper());

		// 递归查询所有子节点
		List<SysDept> resDeptList = new ArrayList<>();
		recursiveDept(allDeptList, deptId, resDeptList);

		// 添加当前节点
		resDeptList.addAll(allDeptList.stream()
			.filter(sysDept -> deptId.equals(sysDept.getDeptId()))
			.collect(Collectors.toList()));
		return resDeptList;
	}

	/**
	 * 递归查询所有子节点。
	 * @param allDeptList 所有部门列表
	 * @param parentId 父部门ID
	 * @param resDeptList 结果集合
	 */
	private void recursiveDept(List<SysDept> allDeptList, Long parentId, List<SysDept> resDeptList) {
		// 使用 Stream API 进行筛选和遍历
		allDeptList.stream().filter(sysDept -> sysDept.getParentId().equals(parentId)).forEach(sysDept -> {
			resDeptList.add(sysDept);
			recursiveDept(allDeptList, sysDept.getDeptId(), resDeptList);
		});
	}

}
