/*
 *
 *      Copyright (c) 2018-2026, lengleng All rights reserved.
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
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.constant.UpmsErrorCodes;
import com.pig4cloud.pig.admin.api.entity.SysI18nEntity;
import com.pig4cloud.pig.admin.api.entity.SysMenu;
import com.pig4cloud.pig.admin.api.entity.SysRoleMenu;
import com.pig4cloud.pig.admin.mapper.SysMenuMapper;
import com.pig4cloud.pig.admin.mapper.SysRoleMenuMapper;
import com.pig4cloud.pig.admin.service.SysI18nService;
import com.pig4cloud.pig.admin.service.SysMenuService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.MenuTypeEnum;
import com.pig4cloud.pig.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

	private final SysRoleMenuMapper sysRoleMenuMapper;

	private final SysI18nService sysI18nService;

	@Override
	@Cacheable(value = CacheConstants.MENU_DETAILS, key = "#roleId", unless = "#result.isEmpty()")
	public List<SysMenu> findMenuByRoleId(Long roleId) {
		return baseMapper.listMenusByRoleId(roleId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
	public R removeMenuById(Long id) {
		// 1. 检查菜单是否存在
		SysMenu menu = this.getById(id);
		if (menu == null) {
			return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_MENU_NOT_FOUND));
		}

		// 2. 递归收集当前菜单及所有子孙菜单的ID
		List<Long> menuIdsToDelete = new ArrayList<>();
		collectMenuIds(id, menuIdsToDelete);

		// 3. 批量删除所有角色-菜单关联
		if (CollUtil.isNotEmpty(menuIdsToDelete)) {
			sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().in(SysRoleMenu::getMenuId, menuIdsToDelete));
		}

		// 4. 批量逻辑删除菜单
		return R.ok(this.removeByIds(menuIdsToDelete));
	}

	/**
	 * 递归收集菜单ID及其所有子孙菜单ID
	 * @param menuId 菜单ID
	 * @param menuIds 收集结果列表
	 */
	private void collectMenuIds(Long menuId, List<Long> menuIds) {
		menuIds.add(menuId);

		List<SysMenu> children = this.list(Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getParentId, menuId));

		if (CollUtil.isNotEmpty(children)) {
			for (SysMenu child : children) {
				collectMenuIds(child.getMenuId(), menuIds);
			}
		}
	}

	@Override
	@CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
	public Boolean updateMenuById(SysMenu sysMenu) {
		return this.updateById(sysMenu);
	}

	/**
	 * 构建树查询 1. 不是懒加载情况，查询全部 2. 是懒加载，根据parentId 查询 2.1 父节点为空，则查询ID -1
	 * @param parentId 父节点ID
	 * @param menuName 菜单名称
	 * @return
	 */
	@Override
	public List<Tree<Long>> treeMenu(Long parentId, String menuName, String type) {
		Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;

		List<TreeNode<Long>> collect = baseMapper
			.selectList(Wrappers.<SysMenu>lambdaQuery()
				.eq(Objects.nonNull(parentId), SysMenu::getParentId, parentId)
				.like(StrUtil.isNotBlank(menuName), SysMenu::getName, menuName)
				.eq(StrUtil.isNotBlank(type), SysMenu::getMenuType, type)
				.orderByAsc(SysMenu::getSortOrder))
			.stream()
			.map(getNodeFunction())
			.toList();

		// 模糊查询 不组装树结构 直接返回 表格方便编辑
		if (StrUtil.isNotBlank(menuName)) {
			return collect.stream().map(node -> {
				Tree<Long> tree = new Tree<>();
				tree.putAll(node.getExtra());
				BeanUtils.copyProperties(node, tree);
				return tree;
			}).toList();
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
	public List<Tree<Long>> filterMenu(Set<SysMenu> all, String type, Long parentId) {
		List<SysI18nEntity> list = sysI18nService.list();
		List<TreeNode<Long>> collect = all.stream().filter(menuTypePredicate(type)).peek(item -> {
			Optional<SysI18nEntity> first = list.stream().filter(it -> it.getZhCn().equals(item.getName())).findFirst();
			first.ifPresent(sysI18nEntity -> item.setName(sysI18nEntity.getName()));
		}).map(getNodeFunction()).toList();

		Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
		return TreeUtil.build(collect, parent);
	}

	@NotNull
	private Function<SysMenu, TreeNode<Long>> getNodeFunction() {
		return menu -> {
			TreeNode<Long> node = new TreeNode<>();
			node.setId(menu.getMenuId());
			node.setName(menu.getName());
			node.setParentId(menu.getParentId());
			node.setWeight(menu.getSortOrder());
			// 扩展属性
			Map<String, Object> extra = new HashMap<>();
			extra.put("path", menu.getPath());
			extra.put("componentPath", menu.getComponent());
			extra.put("menuType", menu.getMenuType());
			extra.put("permission", menu.getPermission());
			extra.put("sortOrder", menu.getSortOrder());

			// 适配 vue3
			Map<String, Object> meta = new HashMap<>();
			meta.put("title", menu.getName());
			meta.put("isLink", menu.getPath() != null && menu.getPath().startsWith("http") ? menu.getPath() : "");
			meta.put("isHide", !BooleanUtil.toBooleanObject(menu.getVisible()));
			meta.put("isKeepAlive", BooleanUtil.toBooleanObject(menu.getKeepAlive()));
			meta.put("isAffix", false);
			meta.put("isIframe", BooleanUtil.toBooleanObject(menu.getEmbedded()));
			meta.put("icon", menu.getIcon());

			extra.put("meta", meta);
			node.setExtra(extra);
			return node;
		};
	}

	/**
	 * menu 类型断言
	 * @param type 类型
	 * @return Predicate
	 */
	private Predicate<SysMenu> menuTypePredicate(String type) {
		return vo -> {
			if (MenuTypeEnum.TOP_MENU.getDescription().equals(type)) {
				return MenuTypeEnum.TOP_MENU.getType().equals(vo.getMenuType());
			}
			// 其他查询 左侧 + 顶部
			return !MenuTypeEnum.BUTTON.getType().equals(vo.getMenuType());
		};
	}

}
