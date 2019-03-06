/*
 *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.dto.DeptTree;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.entity.SysDeptRelation;
import com.pig4cloud.pig.admin.api.vo.TreeUtil;
import com.pig4cloud.pig.admin.mapper.SysDeptMapper;
import com.pig4cloud.pig.admin.service.SysDeptRelationService;
import com.pig4cloud.pig.admin.service.SysDeptService;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
	private final SysDeptRelationService sysDeptRelationService;

	/**
	 * 添加信息部门
	 *
	 * @param dept 部门
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveDept(SysDept dept) {
		SysDept sysDept = new SysDept();
		BeanUtils.copyProperties(dept, sysDept);
		this.save(sysDept);
		sysDeptRelationService.saveDeptRelation(sysDept);
		return Boolean.TRUE;
	}


	/**
	 * 删除部门
	 *
	 * @param id 部门 ID
	 * @return 成功、失败
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeDeptById(Integer id) {
		//级联删除部门
		List<Integer> idList = sysDeptRelationService
			.list(Wrappers.<SysDeptRelation>query().lambda()
				.eq(SysDeptRelation::getAncestor, id))
			.stream()
			.map(SysDeptRelation::getDescendant)
			.collect(Collectors.toList());

		if (CollUtil.isNotEmpty(idList)) {
			this.removeByIds(idList);
		}

		//删除部门级联关系
		sysDeptRelationService.removeDeptRelationById(id);
		return Boolean.TRUE;
	}

	/**
	 * 更新部门
	 *
	 * @param sysDept 部门信息
	 * @return 成功、失败
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateDeptById(SysDept sysDept) {
		//更新部门状态
		this.updateById(sysDept);
		//更新部门关系
		SysDeptRelation relation = new SysDeptRelation();
		relation.setAncestor(sysDept.getParentId());
		relation.setDescendant(sysDept.getDeptId());
		sysDeptRelationService.updateDeptRelation(relation);
		return Boolean.TRUE;
	}

	/**
	 * 查询全部部门树
	 *
	 * @return 树
	 */
	@Override
	public List<DeptTree> listDeptTrees() {
		return getDeptTree(this.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 查询用户部门树
	 *
	 * @return
	 */
	@Override
	public List<DeptTree> listCurrentUserDeptTrees() {
		Integer deptId = SecurityUtils.getUser().getDeptId();
		List<Integer> descendantIdList = sysDeptRelationService
			.list(Wrappers.<SysDeptRelation>query().lambda()
				.eq(SysDeptRelation::getAncestor, deptId))
			.stream().map(SysDeptRelation::getDescendant)
			.collect(Collectors.toList());

		List<SysDept> deptList = baseMapper.selectBatchIds(descendantIdList);
		return getDeptTree(deptList);
	}

	/**
	 * 构建部门树
	 *
	 * @param depts 部门
	 * @return
	 */
	private List<DeptTree> getDeptTree(List<SysDept> depts) {
		List<DeptTree> treeList = depts.stream()
			.filter(dept -> !dept.getDeptId().equals(dept.getParentId()))
			.map(dept -> {
				DeptTree node = new DeptTree();
				node.setId(dept.getDeptId());
				node.setParentId(dept.getParentId());
				node.setName(dept.getName());
				return node;
			}).collect(Collectors.toList());
		return TreeUtil.buildByLoop(treeList, 0);
	}
}
