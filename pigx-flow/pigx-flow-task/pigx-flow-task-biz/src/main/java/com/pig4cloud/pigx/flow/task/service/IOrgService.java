package com.pig4cloud.pigx.flow.task.service;

import com.pig4cloud.pigx.common.core.util.R;

/**
 * @author : willian fu
 * @version : 1.0
 */
public interface IOrgService {

	/**
	 * 查询组织架构树
	 * @param deptId 部门id
	 * @param type 只查询部门架构
	 * @param showLeave 是否显示离职员工
	 * @return 组织架构树数据
	 */
	R getOrgTreeData(Long deptId, String type, Boolean showLeave);

	/**
	 * 模糊搜索用户
	 * @param userName 用户名/拼音/首字母
	 * @return 匹配到的用户
	 */
	R getOrgTreeUser(String userName);

}
