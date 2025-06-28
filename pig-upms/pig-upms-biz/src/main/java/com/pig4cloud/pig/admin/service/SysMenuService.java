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

package com.pig4cloud.pig.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysMenu;
import com.pig4cloud.pig.common.core.util.R;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限服务接口
 * <p>
 * 提供菜单权限相关的服务方法，包括查询、删除、更新和构建菜单树等操作
 * </p>
 *
 * @author lengleng
 * @date 2025/06/27
 */
public interface SysMenuService extends IService<SysMenu> {

	/**
	 * 通过角色编号查询URL 权限
	 * @param roleId 角色ID
	 * @return 菜单列表
	 */
	List<SysMenu> findMenuByRoleId(Long roleId);

	/**
	 * 级联删除菜单
	 * @param id 菜单ID
	 * @return 成功、失败
	 */
	R removeMenuById(Long id);

	/**
	 * 更新菜单信息
	 * @param sysMenu 菜单信息
	 * @return 成功、失败
	 */
	Boolean updateMenuById(SysMenu sysMenu);

	/**
	 * 构建树查询
	 * @param parentId 父级菜单ID
	 * @param menuName 菜单名称
	 * @param type 类型
	 * @return 菜单树
	 */
	List<Tree<Long>> getMenuTree(Long parentId, String menuName, String type);

	/**
	 * 查询菜单
	 * @param all 全部菜单
	 * @param type 类型
	 * @param parentId 父节点ID
	 * @return
	 */
	List<Tree<Long>> filterMenu(Set<SysMenu> all, String type, Long parentId);

}
