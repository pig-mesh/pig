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

package com.pig4cloud.pigx.app.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.app.api.entity.AppMenu;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
public interface AppMenuService extends IService<AppMenu> {

	/**
	 * 通过角色编号查询URL 权限
	 * @param roleId 角色ID
	 * @return 菜单列表
	 */
	List<AppMenu> findMenuByRoleId(Long roleId);

	/**
	 * 级联删除菜单
	 * @param id 菜单ID
	 * @return 成功、失败
	 */
	R removeMenuById(Long id);

	/**
	 * 更新菜单信息
	 * @param appMenu 菜单信息
	 * @return 成功、失败
	 */
	Boolean updateMenuById(AppMenu appMenu);

	/**
	 * 构建树
	 * @param parentId 父节点ID
	 * @param menuName 菜单名称
	 * @return
	 */
	List<Tree<Long>> treeMenu(Long parentId, String menuName);

	/**
	 * 查询菜单
	 * @param voSet
	 * @param parentId
	 * @return
	 */
	List<Tree<Long>> filterMenu(Set<AppMenu> voSet, String type, Long parentId);

}
