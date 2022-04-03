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

package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.*;
import com.pig4cloud.pigx.admin.api.vo.UserExcelVO;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.admin.mapper.SysUserMapper;
import com.pig4cloud.pigx.admin.mapper.SysUserPostMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.excel.vo.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	private final SysMenuService sysMenuService;

	private final SysRoleService sysRoleService;

	private final SysDeptService sysDeptService;

	private final SysUserRoleService sysUserRoleService;

	private final SysUserPostMapper sysUserPostMapper;

	/**
	 * 保存用户信息
	 * @param userDto DTO 对象
	 * @return success/fail
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveUser(UserDTO userDto) {
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(userDto, sysUser);
		sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
		sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
		baseMapper.insert(sysUser);
		// 保存用户岗位信息
		userDto.getPost().stream().map(postId -> {
			SysUserPost userPost = new SysUserPost();
			userPost.setUserId(sysUser.getUserId());
			userPost.setPostId(postId);
			return userPost;
		}).forEach(sysUserPostMapper::insert);
		List<SysUserRole> userRoleList = userDto.getRole().stream().map(roleId -> {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(sysUser.getUserId());
			userRole.setRoleId(roleId);
			return userRole;
		}).collect(Collectors.toList());
		return sysUserRoleService.saveBatch(userRoleList);
	}

	/**
	 * 通过查用户的全部信息
	 * @param sysUser 用户
	 * @return
	 */
	@Override
	public UserInfo findUserInfo(SysUser sysUser) {
		UserInfo userInfo = new UserInfo();
		userInfo.setSysUser(sysUser);
		// 设置角色列表 （ID）
		List<Long> roleIds = sysRoleService.findRolesByUserId(sysUser.getUserId()).stream().map(SysRole::getRoleId)
				.collect(Collectors.toList());
		userInfo.setRoles(ArrayUtil.toArray(roleIds, Long.class));

		// 设置权限列表（menu.permission）
		Set<String> permissions = new HashSet<>();
		roleIds.forEach(roleId -> {
			List<String> permissionList = sysMenuService.findMenuByRoleId(roleId).stream()
					.filter(menu -> StrUtil.isNotEmpty(menu.getPermission())).map(SysMenu::getPermission)
					.collect(Collectors.toList());
			permissions.addAll(permissionList);
		});
		userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
		return userInfo;
	}

	/**
	 * 分页查询用户信息（含有角色信息）
	 * @param page 分页对象
	 * @param userDTO 参数列表
	 * @return
	 */
	@Override
	public IPage getUsersWithRolePage(Page page, UserDTO userDTO) {
		return baseMapper.getUserVosPage(page, userDTO, new DataScope());
	}

	/**
	 * 通过ID查询用户信息
	 * @param id 用户ID
	 * @return 用户信息
	 */
	@Override
	public UserVO selectUserVoById(Long id) {
		return baseMapper.getUserVoById(id);
	}

	/**
	 * 删除用户
	 * @param sysUser 用户
	 * @return Boolean
	 */
	@Override
	@CacheEvict(value = CacheConstants.USER_DETAILS, key = "#sysUser.username")
	public Boolean deleteUserById(SysUser sysUser) {
		sysUserRoleService.deleteByUserId(sysUser.getUserId());
		this.removeById(sysUser.getUserId());
		return Boolean.TRUE;
	}

	@Override
	@CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
	public R<Boolean> updateUserInfo(UserDTO userDto) {
		UserVO userVO = baseMapper.getUserVoByUsername(userDto.getUsername());
		if (!ENCODER.matches(userDto.getPassword(), userVO.getPassword())) {
			log.info("原密码错误，修改个人信息失败:{}", userDto.getUsername());
			return R.failed("原密码错误，修改个人信息失败");
		}

		SysUser sysUser = new SysUser();
		if (StrUtil.isNotBlank(userDto.getNewpassword1())) {
			sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
		}
		sysUser.setPhone(userDto.getPhone());
		sysUser.setUserId(userVO.getUserId());
		sysUser.setAvatar(userDto.getAvatar());
		sysUser.setNickname(userDto.getNickname());
		sysUser.setName(userDto.getName());
		sysUser.setEmail(userDto.getEmail());
		return R.ok(this.updateById(sysUser));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
	public Boolean updateUser(UserDTO userDto) {
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(userDto, sysUser);
		sysUser.setUpdateTime(LocalDateTime.now());

		if (StrUtil.isNotBlank(userDto.getPassword())) {
			sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
		}
		this.updateById(sysUser);

		sysUserRoleService
				.remove(Wrappers.<SysUserRole>update().lambda().eq(SysUserRole::getUserId, userDto.getUserId()));
		userDto.getRole().forEach(roleId -> {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(sysUser.getUserId());
			userRole.setRoleId(roleId);
			userRole.insert();
		});
		sysUserPostMapper.delete(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, userDto.getUserId()));
		userDto.getPost().forEach(postId -> {
			SysUserPost userPost = new SysUserPost();
			userPost.setUserId(sysUser.getUserId());
			userPost.setPostId(postId);
			userPost.insert();
		});
		return Boolean.TRUE;
	}

	/**
	 * 查询上级部门的用户信息
	 * @param username 用户名
	 * @return R
	 */
	@Override
	public List<SysUser> listAncestorUsers(String username) {
		SysUser sysUser = this.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));

		SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
		if (sysDept == null) {
			return null;
		}

		Long parentId = sysDept.getParentId();
		return this.list(Wrappers.<SysUser>query().lambda().eq(SysUser::getDeptId, parentId));
	}

	/**
	 * 查询全部的用户
	 * @param userDTO 查询条件
	 * @return list
	 */
	@Override
	public List<UserExcelVO> listUser(UserDTO userDTO) {
		// 根据数据权限查询全部的用户信息
		List<UserVO> voList = baseMapper.selectVoListByScope(userDTO, new DataScope());
		// 转换成execl 对象输出
		List<UserExcelVO> userExcelVOList = voList.stream().map(userVO -> {
			UserExcelVO excelVO = new UserExcelVO();
			BeanUtils.copyProperties(userVO, excelVO);
			String roleNameList = userVO.getRoleList().stream().map(SysRole::getRoleName)
					.collect(Collectors.joining(StrUtil.COMMA));
			excelVO.setRoleNameList(roleNameList);
			return excelVO;
		}).collect(Collectors.toList());
		return userExcelVOList;
	}

	/**
	 * excel 导入用户, 插入正确的 错误的提示行号
	 * @param excelVOList excel 列表数据
	 * @param bindingResult 错误数据
	 * @return ok fail
	 */
	@Override
	public R importUser(List<UserExcelVO> excelVOList, BindingResult bindingResult) {
		// 通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

		// 个性化校验逻辑
		List<SysUser> userList = this.list();
		List<SysDept> deptList = sysDeptService.list();
		List<SysRole> roleList = sysRoleService.list();

		// 执行数据插入操作 组装 UserDto
		for (int i = 0; i < excelVOList.size(); i++) {
			UserExcelVO excel = excelVOList.get(i);
			Set<String> errorMsg = new HashSet<>();
			// 校验用户名是否存在
			boolean exsitUserName = userList.stream()
					.anyMatch(sysUser -> excel.getUsername().equals(sysUser.getUsername()));

			if (exsitUserName) {
				errorMsg.add(String.format("%s 用户名已存在", excel.getUsername()));
			}

			// 判断输入的部门名称列表是否合法
			Optional<SysDept> deptOptional = deptList.stream()
					.filter(dept -> excel.getDeptName().equals(dept.getName())).findFirst();
			if (!deptOptional.isPresent()) {
				errorMsg.add(String.format("%s 部门名称不存在", excel.getDeptName()));
			}

			// 判断输入的角色名称列表是否合法
			List<String> roleNameList = StrUtil.split(excel.getRoleNameList(), StrUtil.COMMA);
			List<SysRole> roleCollList = roleList.stream()
					.filter(role -> roleNameList.stream().anyMatch(name -> role.getRoleName().equals(name)))
					.collect(Collectors.toList());

			if (roleCollList.size() != roleNameList.size()) {
				errorMsg.add(String.format("%s 角色名称不存在", excel.getRoleNameList()));
			}

			// 数据合法情况
			if (CollUtil.isEmpty(errorMsg)) {
				insertExcelUser(excel, deptOptional, roleCollList);
			}
			else {
				// 数据不合法情况
				errorMessageList.add(new ErrorMessage((long) (i + 2), errorMsg));
			}

		}

		if (CollUtil.isNotEmpty(errorMessageList)) {
			return R.failed(errorMessageList);
		}
		return R.ok(null, "用户导入成功，默认密码为手机号");
	}

	/**
	 * 插入excel User
	 */
	private void insertExcelUser(UserExcelVO excel, Optional<SysDept> deptOptional, List<SysRole> roleCollList) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(excel.getUsername());
		userDTO.setPhone(excel.getPhone());
		userDTO.setNickname(excel.getNickname());
		userDTO.setName(excel.getName());
		userDTO.setEmail(excel.getEmail());
		// 批量导入初始密码为手机号
		userDTO.setPassword(userDTO.getPhone());
		// 根据部门名称查询部门ID
		userDTO.setDeptId(deptOptional.get().getDeptId());
		// 根据角色名称查询角色ID
		List<Long> roleIdList = roleCollList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
		userDTO.setRole(roleIdList);
		// 插入用户
		this.saveUser(userDTO);
	}

}
