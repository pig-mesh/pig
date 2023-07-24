package com.pig4cloud.pigx.flow.task.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.RetOps;
import com.pig4cloud.pigx.flow.task.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.task.service.IOrgService;
import com.pig4cloud.pigx.flow.task.utils.DataUtil;
import com.pig4cloud.pigx.flow.task.vo.OrgTreeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrgServiceImpl implements IOrgService {

	private final RemoteUserService remoteUserService;

	private final RemoteRoleService remoteRoleService;

	private final RemoteDeptService remoteDeptService;

	/**
	 * 查询组织架构树
	 * @param deptId 部门id
	 * @param type 只查询部门架构
	 * @param showLeave 是否显示离职员工
	 * @return 组织架构树数据
	 */
	@Override
	public R getOrgTreeData(Long deptId, String type, Boolean showLeave) {
		List<OrgTreeVo> orgs = new LinkedList<>();

		if (StrUtil.equals(type, NodeUserTypeEnum.ROLE.getKey())) {
			// 角色

			List<SysRole> roleList = remoteRoleService.getAllRole().getData();

			for (SysRole role : roleList) {
				OrgTreeVo orgTreeVo = new OrgTreeVo();
				orgTreeVo.setId(role.getRoleId());
				orgTreeVo.setName(role.getRoleName());
				orgTreeVo.setType(NodeUserTypeEnum.ROLE.getKey());
				orgTreeVo.setSelected(false);
				orgs.add(orgTreeVo);
			}

			Dict dict = Dict.create()
				.set("roleList", orgs)
				.set("childDepartments", orgs)
				.set("employees", new ArrayList<>());
			return R.ok(dict);

		}

		Dict dict = Dict.create()
			.set("titleDepartments", new ArrayList<>())
			.set("roleList", new ArrayList<>())
			.set("employees", new ArrayList<>());

		List<SysDept> deptList = remoteDeptService.getAllDept().getData();

		// 查询所有部门及员工
		{
			List deptVoList = new ArrayList();
			for (SysDept dept : deptList) {
				OrgTreeVo orgTreeVo = new OrgTreeVo();
				orgTreeVo.setId(dept.getDeptId());
				orgTreeVo.setName(dept.getName());
				orgTreeVo.setType(NodeUserTypeEnum.DEPT.getKey());
				orgTreeVo.setSelected(false);
				deptVoList.add(orgTreeVo);
			}
			dict.set("childDepartments", deptVoList);
		}
		if (!StrUtil.equals(type, NodeUserTypeEnum.DEPT.getKey())) {

			List userVoList = new ArrayList();

			List<SysUser> userList = remoteUserService.getUserIdListByDeptIdList(CollUtil.toList(deptId)).getData();

			for (SysUser user : userList) {
				OrgTreeVo orgTreeVo = new OrgTreeVo();
				orgTreeVo.setId(user.getUserId());
				orgTreeVo.setName(user.getUsername());
				orgTreeVo.setType(NodeUserTypeEnum.USER.getKey());
				orgTreeVo.setSelected(false);
				orgTreeVo.setAvatar(user.getAvatar());
				userVoList.add(orgTreeVo);
			}
			dict.set("employees", userVoList);
		}

		if (deptId > 0) {
			List<SysDept> allDept = remoteDeptService.getAllDept().getData();
			List<SysDept> depts = DataUtil.selectParentByDept(deptId, allDept);
			dict.set("titleDepartments", CollUtil.reverse(depts));
		}

		return R.ok(dict);
	}

	/**
	 * 模糊搜索用户
	 * @param userName 用户名/拼音/首字母
	 * @return 匹配到的用户
	 */
	@Override
	public R getOrgTreeUser(String userName) {
		return R.ok(RetOps.of(remoteUserService.getUserListByUserName(userName))
			.getData()
			.orElseGet(ArrayList::new)
			.stream()
			.map(user -> {
				OrgTreeVo orgTreeVo = new OrgTreeVo();
				orgTreeVo.setId(user.getUserId());
				orgTreeVo.setName(user.getUsername());
				orgTreeVo.setType(NodeUserTypeEnum.USER.getKey());
				orgTreeVo.setSelected(false);
				orgTreeVo.setAvatar(user.getAvatar());
				return orgTreeVo;
			})
			.collect(Collectors.toList()));
	}

}
