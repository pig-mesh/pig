/*
 *
 *      Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.dto.RegisterUserDTO;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.api.vo.UserExcelVO;
import com.pig4cloud.pig.admin.api.vo.UserVO;
import com.pig4cloud.pig.common.core.util.R;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * @author lengleng
 * @date 2026-02-10
 */
public interface SysUserService extends IService<SysUser> {

	/**
	 * 查询用户信息
	 * @param userDTO 用户数据传输对象
	 * @return 用户信息对象
	 */
	R<UserInfo> getUserInfo(UserDTO userDTO);

	/**
	 * 分页查询用户信息（含有角色信息）
	 * @param page 分页对象
	 * @param userDTO 参数列表
	 * @return
	 */
	IPage getUsersWithRolePage(Page page, UserDTO userDTO);

	/**
	 * 删除用户
	 * @param ids 用户
	 * @return boolean
	 */
	Boolean deleteUserByIds(Long[] ids);

	/**
	 * 更新当前用户基本信息
	 * @param userDto 用户信息
	 * @return Boolean
	 */
	R<Boolean> updateUserInfo(UserDTO userDto);

	/**
	 * 更新指定用户信息
	 * @param userDto 用户信息DTO对象
	 * @return 更新操作是否成功
	 */
	Boolean updateUser(UserDTO userDto);

	/**
	 * 通过ID查询用户信息
	 * @param id 用户ID
	 * @return 用户信息
	 */
	UserVO selectUserVoById(Long id);

	/**
	 * 查询上级部门的用户信息
	 * @param username 用户名
	 * @return R
	 */
	List<SysUser> listAncestorUsers(String username);

	/**
	 * 保存用户信息
	 * @param userDto DTO 对象
	 * @return success/fail
	 */
	Boolean saveUser(UserDTO userDto);

	/**
	 * 查询全部的用户
	 * @param userDTO 查询条件
	 * @param ids 目标列表
	 * @return list
	 */
	List<UserExcelVO> listUser(UserDTO userDTO, Long[] ids);

	/**
	 * excel 导入用户
	 * @param excelVOList excel 列表数据
	 * @param bindingResult 错误数据
	 * @return ok fail
	 */
	R importUser(List<UserExcelVO> excelVOList, BindingResult bindingResult);

	/**
	 * 注册用户
	 * @param userDto 用户信息
	 * @return success/false
	 */
	R<Boolean> registerUser(RegisterUserDTO userDto);

	/**
	 * 锁定用户
	 * @param username 用户名
	 * @return R
	 */
	R<Boolean> lockUser(String username);

	/**
	 * 修改用户密码
	 * @param userDto 包含用户信息的DTO对象
	 * @return 操作结果
	 */
	R changePassword(UserDTO userDto);

	/**
	 * 解绑社交登录
	 * @param type 社交登录类型
	 * @return R
	 */
	R unbinding(String type);

	/**
	 * 校验密码
	 * @param username 用户名
	 * @param password 密码
	 * @return R
	 */
	R checkPassword(String username, String password);

	/**
	 * 根据角色ID列表获取用户ID列表接口
	 * @param roleIdList 角色ID列表
	 * @return List<Long> 返回结果对象，包含根据角色ID列表获取到的用户ID列表信息
	 */
	List<Long> listUserIdByRoleIds(List<Long> roleIdList);

	/**
	 * 根据部门ID列表获取用户ID列表接口
	 * @param deptIdList 部门ID列表
	 * @return List<Long> 返回结果对象，包含根据部门ID列表获取到的用户ID列表信息
	 */
	List<SysUser> listUserIdByDeptIds(List<Long> deptIdList);

	/**
	 * 根据岗位ID列表获取用户ID列表
	 * @param postIdList 岗位ID列表
	 * @return List<Long> 用户ID列表
	 */
	List<Long> listUserIdByPostIds(List<Long> postIdList);

	/**
	 * 重置用户密码
	 * @param userDto 用户信息DTO
	 * @return 操作结果，包含是否成功的布尔值
	 */
	R<Boolean> resetUserPassword(RegisterUserDTO userDto);

	/**
	 * 找回用户密码
	 * @param userDto 用户信息DTO
	 * @param code 验证码
	 * @return 操作结果，包含是否成功的布尔值
	 */
	R<Boolean> forgetUserPassword(RegisterUserDTO userDto, String code);

	/**
	 * 获取当前用户的部门列表
	 * @return 部门列表
	 */
	List<SysDept> getUserDeptList();

	/**
	 * 切换用户当前部门
	 * @param deptId 部门ID
	 * @return 操作结果
	 */
	R<Boolean> updateUserDept(Long deptId);

}
