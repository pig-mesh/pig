package com.pig4cloud.pigx.flow.support.utils;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据处理工具类
 * <p>
 * 提供组织架构数据的递归查询功能，包括部门和菜单的树形结构遍历。
 * 主要用于处理层级关系数据，支持向上查找父级和向下查找子级。
 * <p>
 * 功能特点：
 * 1. 支持部门树的向上和向下遍历
 * 2. 支持菜单树的向下遍历
 * 3. 返回结果包含查询节点本身
 * 4. 使用递归算法实现树形遍历
 *
 * @author pigx
 */
public class DataUtil {

	/**
	 * 根据部门ID向上查找所有父级部门
	 * <p>
	 * 递归查找指定部门的所有上级部门，包括直接上级和间接上级。
	 * 查找顺序从当前部门开始，逐级向上直到根部门。
	 *
	 * @param deptId      当前部门ID
	 * @param allDeptList 所有部门列表
	 * @return 包括自己部门在内的所有父级部门列表，如果部门不存在则返回空列表
	 */
	public static List<SysDept> selectParentByDept(long deptId, List<SysDept> allDeptList) {
		List<SysDept> list = new ArrayList<>();
		SysDept dept = allDeptList.stream().filter(w -> w.getDeptId() == deptId).findAny().orElse(null);
		if (dept == null || dept.getParentId() == null) {
			return list;
		}
		Long parentId = dept.getParentId();
		List<SysDept> depts = selectParentByDept(parentId, allDeptList);
		list.add(dept);
		list.addAll(depts);
		return list;

	}

	/**
	 * 根据部门ID向下查找所有子级部门
	 * <p>
	 * 递归查找指定部门的所有下级部门，包括直接下级和间接下级。
	 * 使用深度优先遍历算法，获取完整的子部门树。
	 *
	 * @param deptId      当前部门ID
	 * @param allDeptList 所有部门列表
	 * @return 包括自己部门在内的所有子级部门列表
	 */
	public static List<SysDept> selectChildrenByDept(long deptId, List<SysDept> allDeptList) {
		List<SysDept> list = new ArrayList<>();
		list.add(allDeptList.stream().filter(w -> w.getDeptId() == deptId).findFirst().get());

		List<SysDept> collect = allDeptList.stream()
			.filter(w -> w.getParentId() == deptId)
                .toList();
		if (CollUtil.isEmpty(collect)) {
			return list;
		}
		for (SysDept dept : collect) {
			List<SysDept> depts = selectChildrenByDept(dept.getDeptId(), allDeptList);
			list.addAll(depts);
		}
		return list;

	}

	/**
	 * 根据菜单ID向下查找所有子级菜单
	 * <p>
	 * 递归查找指定菜单的所有下级菜单，包括直接下级和间接下级。
	 * 主要用于菜单权限管理和菜单树的渲染。
	 *
	 * @param menuId      当前菜单ID
	 * @param allMenuList 所有菜单列表
	 * @return 包括自己在内的所有子级菜单列表
	 */
	public static List<SysMenu> selectChildrenByMenu(long menuId, List<SysMenu> allMenuList) {
		List<SysMenu> list = new ArrayList<>();
		list.add(allMenuList.stream().filter(w -> w.getMenuId() == menuId).findFirst().get());

		List<SysMenu> collect = allMenuList.stream()
			.filter(w -> w.getParentId() == menuId)
                .toList();
		if (CollUtil.isEmpty(collect)) {
			return list;
		}
		for (SysMenu dept : collect) {
			List<SysMenu> depts = selectChildrenByMenu(dept.getMenuId(), allMenuList);
			list.addAll(depts);
		}
		return list;

	}

}
