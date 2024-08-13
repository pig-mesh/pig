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
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    private final SysPostService sysPostService;

    private final SysDeptService sysDeptService;

    private final SysUserRoleMapper sysUserRoleMapper;

    private final SysUserPostMapper sysUserPostMapper;

    private final CacheManager cacheManager;

    /**
     * 保存用户信息
     *
     * @param userDto DTO 对象
     * @return success/fail
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
        Optional.ofNullable(userDto.getPost()).ifPresent(posts -> {
            posts.stream().map(postId -> {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(sysUser.getUserId());
                userPost.setPostId(postId);
                return userPost;
            }).forEach(sysUserPostMapper::insert);
        });

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
        userDto.getRole().stream().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            return userRole;
        }).forEach(sysUserRoleMapper::insert);
        return Boolean.TRUE;
    }

    /**
     * 通过查用户的全部信息
     *
     * @param sysUser 用户
     * @return
     */
    @Override
    public UserInfo findUserInfo(SysUser sysUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        // 设置角色列表 （ID）
        List<Long> roleIds = sysRoleService.findRolesByUserId(sysUser.getUserId())
                .stream()
                .map(SysRole::getRoleId)
                .collect(Collectors.toList());
        userInfo.setRoles(ArrayUtil.toArray(roleIds, Long.class));

        // 设置权限列表（menu.permission）
        Set<String> permissions = new HashSet<>();
        roleIds.forEach(roleId -> {
            List<String> permissionList = sysMenuService.findMenuByRoleId(roleId)
                    .stream()
                    .filter(menu -> StrUtil.isNotEmpty(menu.getPermission()))
                    .map(SysMenu::getPermission)
                    .collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
    @Override
    public IPage getUsersWithRolePage(Page page, UserDTO userDTO) {
        return baseMapper.getUserVosPage(page, userDTO);
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserVO selectUserVoById(Long id) {
        return baseMapper.getUserVoById(id);
    }

    /**
     * 删除用户
     *
     * @param ids 用户ID 列表
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserByIds(Long[] ids) {
        // 删除 spring cache
        List<SysUser> userList = baseMapper.selectBatchIds(CollUtil.toList(ids));
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        for (SysUser sysUser : userList) {
            // 立即删除
            cache.evictIfPresent(sysUser.getUsername());
        }

        sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, CollUtil.toList(ids)));
        this.removeBatchByIds(CollUtil.toList(ids));
        return Boolean.TRUE;
    }

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
            sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userDto.getUserId()));
            userDto.getRole().stream().map(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(sysUser.getUserId());
                userRole.setRoleId(roleId);
                return userRole;
            }).forEach(SysUserRole::insert);
        }


        // 更新用户岗位表
        if (Objects.nonNull(userDto.getPost())) {
            sysUserPostMapper.delete(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, userDto.getUserId()));
            userDto.getPost().stream().map(postId -> {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(sysUser.getUserId());
                userPost.setPostId(postId);
                return userPost;
            }).forEach(SysUserPost::insert);
        }
        return Boolean.TRUE;
    }

    /**
     * 查询全部的用户
     *
     * @param userDTO 查询条件
     * @return list
     */
    @Override
    public List<UserExcelVO> listUser(UserDTO userDTO) {
        // 根据数据权限查询全部的用户信息
        List<UserVO> voList = baseMapper.selectVoList(userDTO);
        // 转换成execl 对象输出
        return voList.stream().map(userVO -> {
            UserExcelVO excelVO = new UserExcelVO();
            BeanUtils.copyProperties(userVO, excelVO);
            String roleNameList = userVO.getRoleList()
                    .stream()
                    .map(SysRole::getRoleName)
                    .collect(Collectors.joining(StrUtil.COMMA));
            excelVO.setRoleNameList(roleNameList);
            String postNameList = userVO.getPostList()
                    .stream()
                    .map(SysPost::getPostName)
                    .collect(Collectors.joining(StrUtil.COMMA));
            excelVO.setPostNameList(postNameList);
            return excelVO;
        }).collect(Collectors.toList());
    }

    /**
     * excel 导入用户, 插入正确的 错误的提示行号
     *
     * @param excelVOList   excel 列表数据
     * @param bindingResult 错误数据
     * @return ok fail
     */
    @Override
    public R importUser(List<UserExcelVO> excelVOList, BindingResult bindingResult) {
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
            boolean exsitUserName = userList.stream()
                    .anyMatch(sysUser -> excel.getUsername().equals(sysUser.getUsername()));

            if (exsitUserName) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, excel.getUsername()));
            }

            // 判断输入的部门名称列表是否合法
            Optional<SysDept> deptOptional = deptList.stream()
                    .filter(dept -> excel.getDeptName().equals(dept.getName()))
                    .findFirst();
            if (!deptOptional.isPresent()) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_DEPT_DEPTNAME_INEXISTENCE, excel.getDeptName()));
            }

            // 判断输入的角色名称列表是否合法
            List<String> roleNameList = StrUtil.split(excel.getRoleNameList(), StrUtil.COMMA);
            List<SysRole> roleCollList = roleList.stream()
                    .filter(role -> roleNameList.stream().anyMatch(name -> role.getRoleName().equals(name)))
                    .collect(Collectors.toList());

            if (roleCollList.size() != roleNameList.size()) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_ROLE_ROLENAME_INEXISTENCE, excel.getRoleNameList()));
            }

            // 判断输入的部门名称列表是否合法
            List<String> postNameList = StrUtil.split(excel.getPostNameList(), StrUtil.COMMA);
            List<SysPost> postCollList = postList.stream()
                    .filter(post -> postNameList.stream().anyMatch(name -> post.getPostName().equals(name)))
                    .collect(Collectors.toList());

            if (postCollList.size() != postNameList.size()) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_POST_POSTNAME_INEXISTENCE, excel.getPostNameList()));
            }

            // 数据合法情况
            if (CollUtil.isEmpty(errorMsg)) {
                insertExcelUser(excel, deptOptional, roleCollList, postCollList);
            } else {
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
     * 插入excel User
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
        List<Long> postIdList = postCollList.stream().map(SysPost::getPostId).collect(Collectors.toList());
        userDTO.setPost(postIdList);
        // 根据角色名称查询角色ID
        List<Long> roleIdList = roleCollList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        userDTO.setRole(roleIdList);
        // 插入用户
        this.saveUser(userDTO);
    }

    /**
     * 注册用户 赋予用户默认角色
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> registerUser(UserDTO userDto) {
        // 判断用户名是否存在
        SysUser sysUser = this.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, userDto.getUsername()));
        if (sysUser != null) {
            String message = MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, userDto.getUsername());
            return R.failed(message);
        }
        return R.ok(saveUser(userDto));
    }

    /**
     * 锁定用户
     *
     * @param username 用户名
     * @return
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

    @Override
    public R checkPassword(String password) {
        SysUser sysUser = baseMapper.selectById(SecurityUtils.getUser().getId());

        if (!ENCODER.matches(password, sysUser.getPassword())) {
            log.info("原密码错误");
            return R.failed("密码输入错误");
        } else {
            return R.ok();
        }
    }

}
