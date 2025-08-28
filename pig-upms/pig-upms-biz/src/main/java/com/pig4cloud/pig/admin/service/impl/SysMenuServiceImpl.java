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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysMenu;
import com.pig4cloud.pig.admin.api.entity.SysRoleMenu;
import com.pig4cloud.pig.admin.mapper.SysMenuMapper;
import com.pig4cloud.pig.admin.mapper.SysRoleMenuMapper;
import com.pig4cloud.pig.admin.service.SysMenuService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.MenuTypeEnum;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

/**
 * 菜单权限表服务实现类
 *
 * @author lengleng
 * @date 2025/05/30
 */
@Service
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

	private final SysRoleMenuMapper sysRoleMenuMapper;

	/**
	 * 根据角色ID查询菜单列表
	 * @param roleId 角色ID
	 * @return 菜单列表，如果结果为空则不会被缓存
	 * @see CacheConstants#MENU_DETAILS
	 */
	@Override
	@Cacheable(value = CacheConstants.MENU_DETAILS, key = "#roleId", unless = "#result.isEmpty()")
	public List<SysMenu> findMenuByRoleId(Long roleId) {
		return baseMapper.listMenusByRoleId(roleId);
	}

	/**
	 * 根据ID删除菜单
	 * @param id 菜单ID
	 * @return 删除结果
	 * @throws Exception 事务回滚时抛出异常
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
	public R removeMenuById(Long id) {
		// 查询父节点为当前节点的节点
		List<SysMenu> menuList = this.list(Wrappers.<SysMenu>query().lambda().eq(SysMenu::getParentId, id));
		if (CollUtil.isNotEmpty(menuList)) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_MENU_DELETE_EXISTING));
		}

		sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getMenuId, id));
		// 删除当前菜单及其子菜单
		return R.ok(this.removeById(id));
	}

	/**
	 * 根据ID更新菜单信息
	 * @param sysMenu 菜单实体对象
	 * @return 更新是否成功
	 */
	@Override
	@CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
	public Boolean updateMenuById(SysMenu sysMenu) {
		return this.updateById(sysMenu);
	}

	/**
	 * 构建菜单树结构
	 * @param parentId 父节点ID，为空时使用默认根节点
	 * @param menuName 菜单名称，支持模糊查询
	 * @param type 菜单类型
	 * @return 菜单树结构列表，模糊查询时返回平铺列表
	 */
	@Override
	public List<Tree<Long>> getMenuTree(Long parentId, String menuName, String type) {
		Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;

		List<TreeNode<Long>> collect = baseMapper
			.selectList(Wrappers.<SysMenu>lambdaQuery()
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
	 * 根据类型和父节点ID过滤菜单并构建树形结构
	 * @param all 全部菜单集合
	 * @param type 菜单类型
	 * @param parentId 父节点ID，为空时使用根节点ID
	 * @return 构建好的菜单树形结构列表
	 */
	@Override
	public List<Tree<Long>> filterMenu(Set<SysMenu> all, String type, Long parentId) {
		List<TreeNode<Long>> collect = all.stream().filter(menuTypePredicate(type)).map(getNodeFunction()).toList();

		Long parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
		return TreeUtil.build(collect, parent);
	}

	/**
	 * 获取将SysMenu转换为TreeNode<Long>的函数
	 * @return 转换函数，将SysMenu对象转换为TreeNode<Long>对象
	 */
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
			extra.put(SysMenu.Fields.path, menu.getPath());
			extra.put(SysMenu.Fields.menuType, menu.getMenuType());
			extra.put(SysMenu.Fields.permission, menu.getPermission());
			extra.put(SysMenu.Fields.sortOrder, menu.getSortOrder());

			// 适配 vue3
			Map<String, Object> meta = new HashMap<>();
			meta.put("title", menu.getName());
			meta.put("isLink", menu.getPath() != null && menu.getPath().startsWith("http") ? menu.getPath() : "");
			meta.put("isHide", !BooleanUtil.toBooleanObject(menu.getVisible()));
			meta.put("isKeepAlive", BooleanUtil.toBooleanObject(menu.getKeepAlive()));
			meta.put("isAffix", false);
			meta.put("isIframe", BooleanUtil.toBooleanObject(menu.getEmbedded()));
			meta.put(SysMenu.Fields.icon, menu.getIcon());
			// 增加英文
			meta.put(SysMenu.Fields.enName, menu.getEnName());

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
