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

package com.pig4cloud.pigx.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.vo.DeptExcelVO;
import com.pig4cloud.pigx.admin.api.vo.OrgTreeVO;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
public interface SysDeptService extends IService<SysDept> {

	/**
	 * 查询部门树菜单
	 * @param deptName 部门名称
	 * @return 树
	 */
	List<Tree<Long>> selectTree(String deptName, Long parentId);

	/**
	 * 删除部门
	 * @param id 部门 ID
	 * @return 成功、失败
	 */
	Boolean removeDeptById(Long id);

	List<DeptExcelVO> listExcelVo();

	R importDept(List<DeptExcelVO> excelVOList, BindingResult bindingResult);

	/**
	 * 获取部门的所有后代部门列表
	 * @param deptId 部门ID
	 * @return 后代部门列表
	 */
	List<SysDept> listDescendant(Long deptId);

	/**
	 * 获取部门负责人
	 * @param deptId deptId
	 * @return user id list
	 */
	List<Long> listDeptLeader(Long deptId);

	/**
	 * 查询全部部门包含用户
	 * @param parentDeptId 父部门ID
	 * @param type 查询类型
	 */
	Map<String, Object> listOrgTree(Long parentDeptId, String type);

	/**
	 * 根据用户名模糊搜索用户
	 * @param username 用户名
	 * @return List user
	 */
	List<OrgTreeVO> getOrgTreeUser(String username);

}
