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

package com.pig4cloud.pig.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.admin.api.entity.SysDeptRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Mapper
public interface SysDeptRelationMapper extends BaseMapper<SysDeptRelation> {

	/**
	 * 删除部门节点关系
	 * @param deptRelation 待删除的某一个部门节点
	 */
	void deleteDeptRelations(SysDeptRelation deptRelation);

	/**
	 * 删除部门节点关系,同时删除所有关联此部门子节点的部门关系
	 * @param id 待删除的部门节点ID
	 */
	void deleteDeptRelationsById(Long id);

	/**
	 * 新增部门节点关系
	 * @param deptRelation 待新增的部门节点关系
	 */
	void insertDeptRelations(SysDeptRelation deptRelation);

}
