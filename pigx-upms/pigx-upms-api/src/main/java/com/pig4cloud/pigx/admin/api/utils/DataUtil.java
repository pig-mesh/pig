package com.pig4cloud.pigx.admin.api.utils;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysMenu;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class DataUtil {

	/**
	 * 根据当前部门id，向上查找所有的部门
	 * @param deptId 当前部门id
	 * @param allDeptList 所有部门列表
	 * @return 返回包括自己部门在内的所有父级部门
	 */
	public List<SysDept> selectParentByDept(long deptId, List<SysDept> allDeptList) {
		List<SysDept> list = new ArrayList<>();
		Optional<SysDept> optionalDept = allDeptList.stream().filter(w -> w.getDeptId() == deptId).findFirst();

		optionalDept.ifPresent(dept -> {
			if (dept.getParentId() != null) {
				Long parentId = dept.getParentId();
				List<SysDept> depts = selectParentByDept(parentId, allDeptList);
				list.add(dept);
				list.addAll(depts);
			}
		});

		return list;
	}

	/**
	 * 根据当前部门id，向下查找所有的部门
	 * @param deptId 当前部门id
	 * @param allDeptList 所有部门列表
	 * @return 返回包括自己部门在内的所有子级部门
	 */
	public List<SysDept> selectChildrenByDept(long deptId, List<SysDept> allDeptList) {
		List<SysDept> list = new ArrayList<>();
		Optional<SysDept> optionalDept = allDeptList.stream().filter(w -> w.getDeptId() == deptId).findFirst();

		optionalDept.ifPresent(list::add);

		List<SysDept> collect = allDeptList.stream()
			.filter(w -> w.getParentId() == deptId)
			.collect(Collectors.toList());

		if (CollUtil.isEmpty(collect)) {
			return list;
		}

		collect.forEach(dept -> {
			List<SysDept> depts = selectChildrenByDept(dept.getDeptId(), allDeptList);
			list.addAll(depts);
		});

		return list;
	}

	/**
	 * 根据当前菜单id，向下查找所有的菜单
	 * @param menuId 当前菜单id
	 * @param allMenuList 所有菜单列表
	 * @return 返回包括自己在内的所有子级菜单
	 */
	public List<SysMenu> selectChildrenByMenu(long menuId, List<SysMenu> allMenuList) {
		List<SysMenu> list = new ArrayList<>();
		list.add(allMenuList.stream().filter(w -> w.getMenuId() == menuId).findFirst().get());

		List<SysMenu> collect = allMenuList.stream()
			.filter(w -> w.getParentId() == menuId)
			.collect(Collectors.toList());
		if (CollUtil.isEmpty(collect)) {
			return list;
		}
		collect.forEach(dept -> list.addAll(selectChildrenByMenu(dept.getMenuId(), allMenuList)));
		return list;
	}

}
