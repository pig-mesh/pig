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
package com.pig4cloud.pigx.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.app.api.entity.AppMenu;
import com.pig4cloud.pigx.app.api.entity.AppRoleMenu;
import com.pig4cloud.pigx.app.mapper.AppMenuMapper;
import com.pig4cloud.pigx.app.mapper.AppRoleMenuMapper;
import com.pig4cloud.pigx.app.service.AppMenuService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.enums.MenuTypeEnum;
import com.pig4cloud.pigx.common.core.exception.ErrorCodes;
import com.pig4cloud.pigx.common.core.util.MsgUtils;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 菜单权限表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@Service
@AllArgsConstructor
public class AppMenuServiceImpl extends ServiceImpl<AppMenuMapper, AppMenu> implements AppMenuService {

	private final AppRoleMenuMapper appRoleMenuMapper;

	@Override
	public List<AppMenu> findMenuByRoleId(Long roleId) {
		return baseMapper.listMenusByRoleId(roleId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
	public R removeMenuById(Long id) {
		// 查询父节点为当前节点的节点
		List<AppMenu> menuList = this.list(Wrappers.<AppMenu>query().lambda().eq(AppMenu::getParentId, id));
		if (CollUtil.isNotEmpty(menuList)) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_MENU_DELETE_EXISTING));
		}

		appRoleMenuMapper.delete(Wrappers.<AppRoleMenu>query().lambda().eq(AppRoleMenu::getMenuId, id));
		// 删除当前菜单及其子菜单
		return R.ok(this.removeById(id));
	}

	@Override
	public Boolean updateMenuById(AppMenu appMenu) {
		return this.updateById(appMenu);
	}

	/**
	 * 构建树查询 1. 不是懒加载情况，查询全部 2. 是懒加载，根据parentId 查询 2.1 父节点为空，则查询ID -1
	 * @param parentId 父节点ID
	 * @param menuName 菜单名称
	 * @return
	 */
	@Override
	public List<Tree<Long>> treeMenu(Long parentId, String menuName) {
		Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;

		List<TreeNode<Long>> collect = baseMapper
				.selectList(
						Wrappers.<AppMenu>lambdaQuery().like(StrUtil.isNotBlank(menuName), AppMenu::getName, menuName)
								.orderByAsc(AppMenu::getSortOrder))
				.stream().map(getNodeFunction()).collect(Collectors.toList());

		// 模糊查询 不组装树结构 直接返回 表格方便编辑
		if (StrUtil.isNotBlank(menuName)) {
			return collect.stream().map(node -> {
				Tree<Long> tree = new Tree<>();
				tree.putAll(node.getExtra());
				BeanUtils.copyProperties(node, tree);
				return tree;
			}).collect(Collectors.toList());
		}

		return TreeUtil.build(collect, parent);
	}

	/**
	 * 查询菜单
	 * @param all 全部菜单
	 * @param type 类型
	 * @param parentId 父节点ID
	 * @return
	 */
	@Override
	public List<Tree<Long>> filterMenu(Set<AppMenu> all, String type, Long parentId) {
		List<TreeNode<Long>> collect = all.stream().filter(menuTypePredicate(type)).map(getNodeFunction())
				.collect(Collectors.toList());
		Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
		return TreeUtil.build(collect, parent);
	}

	@NotNull
	private Function<AppMenu, TreeNode<Long>> getNodeFunction() {
		return menu -> {
			TreeNode<Long> node = new TreeNode<>();
			node.setId(menu.getMenuId());
			node.setName(menu.getName());
			node.setParentId(menu.getParentId());
			node.setWeight(menu.getSortOrder());
			// 扩展属性
			Map<String, Object> extra = new HashMap<>();
			extra.put("path", menu.getPath());
			extra.put("menuType", menu.getMenuType());
			extra.put("permission", menu.getPermission());
			extra.put("label", menu.getName());
			extra.put("sortOrder", menu.getSortOrder());
			extra.put("visible", menu.getVisible());
			node.setExtra(extra);
			return node;
		};
	}

	/**
	 * menu 类型断言
	 * @param type 类型
	 * @return Predicate
	 */
	private Predicate<AppMenu> menuTypePredicate(String type) {
		return vo -> {
			if (MenuTypeEnum.TOP_MENU.getDescription().equals(type)) {
				return MenuTypeEnum.TOP_MENU.getType().equals(vo.getMenuType());
			}
			// 其他查询 左侧 + 顶部
			return !MenuTypeEnum.BUTTON.getType().equals(vo.getMenuType());
		};
	}

}
