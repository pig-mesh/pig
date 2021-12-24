/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysDept;

import java.util.List;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
public interface SysDeptService extends IService<SysDept> {

	/**
	 * 查询部门树菜单
	 * @return 树
	 */
	List<Tree<Long>> listDeptTrees();

	/**
	 * 查询用户部门树
	 * @return
	 */
	List<Tree<Long>> listCurrentUserDeptTrees();

	/**
	 * 添加信息部门
	 * @param sysDept
	 * @return
	 */
	Boolean saveDept(SysDept sysDept);

	/**
	 * 删除部门
	 * @param id 部门 ID
	 * @return 成功、失败
	 */
	Boolean removeDeptById(Long id);

	/**
	 * 更新部门
	 * @param sysDept 部门信息
	 * @return 成功、失败
	 */
	Boolean updateDeptById(SysDept sysDept);

	/**
	 * 查找指定部门的子部门id列表
	 * @param deptId 部门id
	 * @return List<Long>
	 */
	List<Long> listChildDeptId(Long deptId);

}
