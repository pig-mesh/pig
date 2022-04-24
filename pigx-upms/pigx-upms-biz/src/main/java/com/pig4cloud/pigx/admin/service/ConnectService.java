package com.pig4cloud.pigx.admin.service;

/**
 * @author lengleng
 * @date 2022/4/22
 * <p>
 * 互联平台
 */
public interface ConnectService {

	/**
	 * 同步钉钉角色
	 */
	Boolean syncDingRole();

	/**
	 * 同步钉钉部门
	 */
	Boolean syncDingDept();

	/**
	 * 同步钉钉用户
	 */
	Boolean syncDingUser(Long deptId);

}
