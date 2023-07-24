package com.pig4cloud.pigx.flow.task.utils;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataUtil {

	/**
	 * 根据当前部门id，向上查找所有的部门
	 * @param deptId
	 * @param allDeptList
	 * @return 返回包括自己部门在内的所有父级部门
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
	 * 根据当前部门id，向下查找所有的部门
	 * @param deptId
	 * @param allDeptList
	 * @return 返回包括自己部门在内的所有子级部门
	 */
	public static List<SysDept> selectChildrenByDept(long deptId, List<SysDept> allDeptList) {
		List<SysDept> list = new ArrayList<>();
		list.add(allDeptList.stream().filter(w -> w.getDeptId() == deptId).findFirst().get());

		List<SysDept> collect = allDeptList.stream()
			.filter(w -> w.getParentId() == deptId)
			.collect(Collectors.toList());
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
	 * 根据当前菜单id，向下查找所有的菜单
	 * @param menuId
	 * @param allMenuList
	 * @return 返回包括自己在内的所有子级菜单
	 */
	public static List<SysMenu> selectChildrenByMenu(long menuId, List<SysMenu> allMenuList) {
		List<SysMenu> list = new ArrayList<>();
		list.add(allMenuList.stream().filter(w -> w.getMenuId() == menuId).findFirst().get());

		List<SysMenu> collect = allMenuList.stream()
			.filter(w -> w.getParentId() == menuId)
			.collect(Collectors.toList());
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
