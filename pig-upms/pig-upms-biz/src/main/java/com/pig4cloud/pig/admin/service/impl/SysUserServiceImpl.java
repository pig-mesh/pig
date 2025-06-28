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

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.dto.RegisterUserDTO;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.*;
import com.pig4cloud.pig.admin.api.util.ParamResolver;
import com.pig4cloud.pig.admin.api.vo.UserExcelVO;
import com.pig4cloud.pig.admin.api.vo.UserVO;
import com.pig4cloud.pig.admin.mapper.SysUserMapper;
import com.pig4cloud.pig.admin.mapper.SysUserPostMapper;
import com.pig4cloud.pig.admin.mapper.SysUserRoleMapper;
import com.pig4cloud.pig.admin.service.*;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户服务实现类
 *
 * @author lengleng
 * @date 2025/05/30
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

	private final SysMenuService sysMenuService;

	private final SysRoleService sysRoleService;

	private final SysPostService sysPostService;

	private final SysDeptService sysDeptService;

	private final SysUserRoleMapper sysUserRoleMapper;

	private final SysUserPostMapper sysUserPostMapper;

	private final CacheManager cacheManager;

	/**
	 * 保存用户信息
	 * @param userDto 用户数据传输对象
	 * @return 操作是否成功
	 * @throws Exception 事务回滚时抛出异常
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveUser(UserDTO userDto) {
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(userDto, sysUser);
		sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
		sysUser.setCreateBy(userDto.getUsername());
		sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
		baseMapper.insert(sysUser);
		// 保存用户岗位信息
		Optional.ofNullable(userDto.getPost()).ifPresent(posts -> posts.forEach(postId -> {
			SysUserPost userPost = new SysUserPost();
			userPost.setUserId(sysUser.getUserId());
			userPost.setPostId(postId);
			sysUserPostMapper.insert(userPost);
		}));

		// 如果角色为空，赋默认角色
		if (CollUtil.isEmpty(userDto.getRole())) {
			// 获取默认角色编码
			String defaultRole = ParamResolver.getStr("USER_DEFAULT_ROLE");
			// 默认角色
			SysRole sysRole = sysRoleService
				.getOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getRoleCode, defaultRole));
			userDto.setRole(Collections.singletonList(sysRole.getRoleId()));
		}

		// 插入用户角色关系表
		userDto.getRole().forEach(roleId -> {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(sysUser.getUserId());
			userRole.setRoleId(roleId);
			sysUserRoleMapper.insert(userRole);
		});
		return Boolean.TRUE;
	}

	/**
	 * 查询用户全部信息，包括角色和权限
	 * @param query 用户查询条件
	 * @return 包含用户角色和权限的用户信息对象
	 */
	@Override
	public R<UserInfo> getUserInfo(UserDTO query) {
		UserVO dbUser = baseMapper.getUser(query);

		if (dbUser == null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERINFO_EMPTY, query.getUsername()));
		}

		UserInfo userInfo = new UserInfo();
		BeanUtils.copyProperties(dbUser, userInfo);
		// 设置权限列表（menu.permission）
		List<String> permissions = dbUser.getRoleList()
			.stream()
			.map(SysRole::getRoleId)
			.flatMap(roleId -> sysMenuService.findMenuByRoleId(roleId).stream())
			.filter(menu -> StrUtil.isNotEmpty(menu.getPermission()))
			.map(SysMenu::getPermission)
			.toList();
		userInfo.setPermissions(permissions);
		return R.ok(userInfo);
	}

	/**
	 * 分页查询用户信息（包含角色信息）
	 * @param page 分页对象
	 * @param userDTO 查询参数
	 * @return 包含用户和角色信息的分页结果
	 */
	@Override
	public IPage getUsersWithRolePage(Page page, UserDTO userDTO) {
		return baseMapper.getUsersPage(page, userDTO);
	}

	/**
	 * 通过ID查询用户信息
	 * @param id 用户ID
	 * @return 用户信息VO对象
	 */
	@Override
	public UserVO getUserById(Long id) {
		UserDTO query = new UserDTO();
		query.setUserId(id);
		return baseMapper.getUser(query);
	}

	/**
	 * 根据用户ID列表删除用户及相关缓存
	 * @param ids 用户ID数组
	 * @return 删除成功返回true
	 * @throws Exception 事务回滚时抛出异常
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeUserByIds(Long[] ids) {
		List<Long> idList = CollUtil.toList(ids);
		// 删除 spring cache
		Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
		baseMapper.selectByIds(idList).forEach(user -> cache.evictIfPresent(user.getUsername()));

		sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, idList));
		this.removeBatchByIds(idList);
		return Boolean.TRUE;
	}

	/**
	 * 更新用户信息
	 * @param userDto 用户数据传输对象
	 * @return 操作结果，包含更新是否成功
	 */
	@Override
	@CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
	public R<Boolean> updateUserInfo(UserDTO userDto) {
		SysUser sysUser = new SysUser();
		sysUser.setPhone(userDto.getPhone());
		sysUser.setUserId(SecurityUtils.getUser().getId());
		sysUser.setAvatar(userDto.getAvatar());
		sysUser.setNickname(userDto.getNickname());
		sysUser.setName(userDto.getName());
		sysUser.setEmail(userDto.getEmail());
		return R.ok(this.updateById(sysUser));
	}

	/**
	 * 更新用户信息
	 * @param userDto 用户数据传输对象，包含需要更新的用户信息
	 * @return 更新成功返回true
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
	public Boolean updateUser(UserDTO userDto) {
		// 更新用户表信息
		SysUser sysUser = new SysUser();
		BeanUtils.copyProperties(userDto, sysUser);
		sysUser.setUpdateTime(LocalDateTime.now());
		if (StrUtil.isNotBlank(userDto.getPassword())) {
			sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
		}
		this.updateById(sysUser);

		// 更新用户角色表
		if (Objects.nonNull(userDto.getRole())) {
			// 删除用户角色关系
			sysUserRoleMapper
				.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userDto.getUserId()));
			userDto.getRole().forEach(roleId -> {
				SysUserRole userRole = new SysUserRole();
				userRole.setUserId(sysUser.getUserId());
				userRole.setRoleId(roleId);
				sysUserRoleMapper.insert(userRole);
			});
		}

		if (Objects.nonNull(userDto.getPost())) {
			// 删除用户岗位关系
			sysUserPostMapper
				.delete(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, userDto.getUserId()));
			userDto.getPost().forEach(postId -> {
				SysUserPost userPost = new SysUserPost();
				userPost.setUserId(sysUser.getUserId());
				userPost.setPostId(postId);
				sysUserPostMapper.insert(userPost);
			});
		}
		return Boolean.TRUE;
	}

	/**
	 * 查询用户列表并转换为Excel导出格式
	 * @param userDTO 用户查询条件
	 * @return 用户Excel视图对象列表
	 */
	@Override
	public List<UserExcelVO> listUsers(UserDTO userDTO) {
		// 根据数据权限查询全部的用户信息
		List<UserVO> voList = baseMapper.listUsers(userDTO);
		// 转换成execl 对象输出
		return voList.stream().map(userVO -> {
			UserExcelVO excelVO = new UserExcelVO();
			BeanUtils.copyProperties(userVO, excelVO);
			excelVO.setRoleNameList(
					userVO.getRoleList().stream().map(SysRole::getRoleName).collect(Collectors.joining(StrUtil.COMMA)));
			excelVO.setPostNameList(
					userVO.getPostList().stream().map(SysPost::getPostName).collect(Collectors.joining(StrUtil.COMMA)));

			if (Objects.nonNull(userVO.getDept())) {
				excelVO.setDeptName(userVO.getDept().getName());
			}
			return excelVO;
		}).toList();
	}

	/**
	 * 导入用户数据
	 * @param excelVOList Excel数据列表
	 * @param bindingResult 校验结果
	 * @return 导入结果，包含成功或失败信息
	 */
	@Override
	public R importUsers(List<UserExcelVO> excelVOList, BindingResult bindingResult) {
		// 通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();
		List<SysDept> deptList = sysDeptService.list();
		List<SysRole> roleList = sysRoleService.list();
		List<SysPost> postList = sysPostService.list();

		// 执行数据插入操作 组装 UserDto
		for (UserExcelVO excel : excelVOList) {
			// 个性化校验逻辑
			List<SysUser> userList = this.list();

			Set<String> errorMsg = new HashSet<>();
			// 校验用户名是否存在
			if (userList.stream().anyMatch(sysUser -> excel.getUsername().equals(sysUser.getUsername()))) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, excel.getUsername()));
			}

			// 判断输入的部门名称列表是否合法
			Optional<SysDept> deptOptional = deptList.stream()
				.filter(dept -> excel.getDeptName().equals(dept.getName()))
				.findFirst();
			if (deptOptional.isEmpty()) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_DEPT_DEPTNAME_INEXISTENCE, excel.getDeptName()));
			}

			// 判断输入的角色名称列表是否合法
			List<String> roleNameList = StrUtil.split(excel.getRoleNameList(), StrUtil.COMMA);
			List<SysRole> roleCollList = roleList.stream()
				.filter(role -> roleNameList.stream().anyMatch(name -> role.getRoleName().equals(name)))
				.toList();

			if (roleCollList.size() != roleNameList.size()) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_ROLE_ROLENAME_INEXISTENCE, excel.getRoleNameList()));
			}

			// 判断输入的部门名称列表是否合法
			List<String> postNameList = StrUtil.split(excel.getPostNameList(), StrUtil.COMMA);
			List<SysPost> postCollList = postList.stream()
				.filter(post -> postNameList.stream().anyMatch(name -> post.getPostName().equals(name)))
				.toList();

			if (postCollList.size() != postNameList.size()) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_POST_POSTNAME_INEXISTENCE, excel.getPostNameList()));
			}

			// 数据合法情况
			if (CollUtil.isEmpty(errorMsg)) {
				insertExcelUser(excel, deptOptional, roleCollList, postCollList);
			}
			else {
				// 数据不合法情况
				errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
			}

		}

		if (CollUtil.isNotEmpty(errorMessageList)) {
			return R.failed(errorMessageList);
		}
		return R.ok();
	}

	/**
	 * 插入Excel导入的用户信息
	 * @param excel Excel用户数据对象
	 * @param deptOptional 部门信息Optional对象
	 * @param roleCollList 角色列表
	 * @param postCollList 岗位列表
	 */
	private void insertExcelUser(UserExcelVO excel, Optional<SysDept> deptOptional, List<SysRole> roleCollList,
			List<SysPost> postCollList) {
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
		// 插入岗位名称
		List<Long> postIdList = postCollList.stream().map(SysPost::getPostId).toList();
		userDTO.setPost(postIdList);
		// 根据角色名称查询角色ID
		List<Long> roleIdList = roleCollList.stream().map(SysRole::getRoleId).toList();
		userDTO.setRole(roleIdList);
		// 插入用户
		this.saveUser(userDTO);
	}

	/**
	 * 注册用户并赋予默认角色
	 * @param userDto 用户注册信息DTO
	 * @return 注册结果，包含成功或失败状态
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> registerUser(RegisterUserDTO userDto) {
		// 判断用户名是否存在
		SysUser sysUser = this.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, userDto.getUsername()));
		if (sysUser != null) {
			String message = MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, userDto.getUsername());
			return R.failed(message);
		}

		UserDTO user = new UserDTO();
		BeanUtils.copyProperties(userDto, user);
		return R.ok(saveUser(user));
	}

	/**
	 * 锁定用户
	 * @param username 用户名
	 * @return 操作结果，包含是否成功的信息
	 */
	@Override
	@CacheEvict(value = CacheConstants.USER_DETAILS, key = "#username")
	public R<Boolean> lockUser(String username) {
		SysUser sysUser = baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));

		if (Objects.nonNull(sysUser)) {
			sysUser.setLockFlag(CommonConstants.STATUS_LOCK);
			baseMapper.updateById(sysUser);
		}
		return R.ok();
	}

	/**
	 * 修改用户密码
	 * @param userDto 用户信息传输对象，包含用户名、原密码和新密码
	 * @return 操作结果，成功返回R.ok()，失败返回错误信息
	 * @CacheEvict 清除用户详情缓存
	 */
	@Override
	@CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
	public R changePassword(UserDTO userDto) {
		SysUser sysUser = baseMapper.selectById(SecurityUtils.getUser().getId());
		if (Objects.isNull(sysUser)) {
			return R.failed("用户不存在");
		}

		if (StrUtil.isEmpty(userDto.getPassword())) {
			return R.failed("原密码不能为空");
		}

		if (!ENCODER.matches(userDto.getPassword(), sysUser.getPassword())) {
			log.info("原密码错误，修改个人信息失败:{}", userDto.getUsername());
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_UPDATE_PASSWORDERROR));
		}

		if (StrUtil.isEmpty(userDto.getNewpassword1())) {
			return R.failed("新密码不能为空");
		}
		String password = ENCODER.encode(userDto.getNewpassword1());

		this.update(Wrappers.<SysUser>lambdaUpdate()
			.set(SysUser::getPassword, password)
			.eq(SysUser::getUserId, sysUser.getUserId()));
		return R.ok();
	}

	/**
	 * 校验用户密码是否正确
	 * @param password 待校验的密码
	 * @return 校验结果，成功返回R.ok()，失败返回R.failed()
	 */
	@Override
	public R checkPassword(String password) {
		SysUser sysUser = baseMapper.selectById(SecurityUtils.getUser().getId());

		if (!ENCODER.matches(password, sysUser.getPassword())) {
			log.info("原密码错误");
			return R.failed("密码输入错误");
		}
		else {
			return R.ok();
		}
	}

}
