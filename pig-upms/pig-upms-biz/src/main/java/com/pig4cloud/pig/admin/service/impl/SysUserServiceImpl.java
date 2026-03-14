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

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.constant.UpmsErrorCodes;
import com.pig4cloud.pig.admin.api.constant.UserStateEnum;
import com.pig4cloud.pig.admin.api.dto.RegisterUserDTO;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.*;
import com.pig4cloud.pig.admin.api.vo.UserExcelVO;
import com.pig4cloud.pig.admin.api.vo.UserVO;
import com.pig4cloud.pig.admin.mapper.*;
import com.pig4cloud.pig.admin.service.*;
import com.pig4cloud.pig.common.audit.annotation.Audit;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import com.pig4cloud.pig.common.data.resolver.ParamResolver;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;
import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
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
 * @date 2026-02-10
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

    private final SysUserDeptMapper sysUserDeptMapper;

    private final CacheManager cacheManager;

    private final SysDeptMapper sysDeptMapper;

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
        sysUser.setLockFlag(
                StrUtil.isBlank(userDto.getLockFlag()) ? UserStateEnum.NORMAL.getCode() : userDto.getLockFlag());
        sysUser.setCreateBy(userDto.getUsername());
        sysUser.setUpdateBy(userDto.getUsername());
        sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        sysUser.setPasswordModifyTime(LocalDateTime.now());
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

        // 插入用户部门信息
        List<Long> deptIdList = CollUtil.isNotEmpty(userDto.getDeptIds()) ? userDto.getDeptIds()
                : (Objects.nonNull(userDto.getDeptId()) ? Collections.singletonList(userDto.getDeptId()) : null);

        if (CollUtil.isNotEmpty(deptIdList)) {
            // 设置第一个部门为主部门
            sysUser.setDeptId(deptIdList.get(0));
            baseMapper.updateById(sysUser);

            // 插入用户部门关联关系
            deptIdList.forEach(deptId -> {
                SysUserDept sysUserDept = new SysUserDept();
                sysUserDept.setUserId(sysUser.getUserId());
                sysUserDept.setDeptId(deptId);
                sysUserDeptMapper.insert(sysUserDept);
            });
        }

        return Boolean.TRUE;
    }

    /**
     * 根据用户信息查询用户全部信息，包括角色和权限
     *
     * @param userDTO 用户信息DTO
     * @return 包含用户角色和权限的用户信息对象
     */
    @Override
    public R<UserInfo> getUserInfo(UserDTO userDTO) {
        UserVO dbUser = baseMapper.getUserVo(userDTO);

        if (dbUser == null) {
            return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_USERINFO_EMPTY, userDTO.getUsername()));
        }

        // 设置角色列表 （ID）
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(dbUser, userInfo);
        // 设置权限列表（menu.permission）
        List<String> permissions = dbUser.getRoleList()
                .stream()
                .map(SysRole::getRoleId)
                .flatMap(roleId -> sysMenuService.findMenuByRoleId(roleId).stream())
                .map(SysMenu::getPermission)
                .filter(StrUtil::isNotEmpty)
                .toList();
        userInfo.setPermissions(permissions);

        return R.ok(userInfo);
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
        List<SysUser> userList = baseMapper.selectByIds(CollUtil.toList(ids));
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        for (SysUser sysUser : userList) {
            cache.evict(sysUser.getUsername());
        }

        // 删除用户角色
        sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, CollUtil.toList(ids)));
        sysUserDeptMapper.delete(Wrappers.<SysUserDept>lambdaQuery().in(SysUserDept::getUserId, CollUtil.toList(ids)));
        sysUserPostMapper.delete(Wrappers.<SysUserPost>lambdaQuery().in(SysUserPost::getUserId, CollUtil.toList(ids)));
        this.removeBatchByIds(CollUtil.toList(ids));
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public R<Boolean> updateUserInfo(UserDTO userDto) {
        Long userId = SecurityUtils.getUser().getId();
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setAvatar(userDto.getAvatar());
        sysUser.setNickname(userDto.getNickname());
        sysUser.setName(userDto.getName());
        sysUser.setEmail(userDto.getEmail());

        // 校验手机号
        if (StrUtil.isNotBlank(userDto.getPhone())) {
            Object codeObj = RedisUtils.get(CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType()
                    + StringPool.AT + userDto.getPhone());
            if (!StrUtil.equals(userDto.getCode(), codeObj.toString())) {
                return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_APP_SMS_ERROR));
            }
        }

        return R.ok(this.updateById(sysUser));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Audit(name = "用户更新", spel = "@sysUserMapper.selectById(#userDto.userId)")
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
        if (userDto.getRole() != null) {
            sysUserRoleMapper
                    .delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userDto.getUserId()));
            userDto.getRole().stream().map(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(sysUser.getUserId());
                userRole.setRoleId(roleId);
                return userRole;
            }).forEach(SysUserRole::insert);
        }

        // 更新用户岗位表
        if (userDto.getPost() != null) {
            sysUserPostMapper
                    .delete(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, userDto.getUserId()));
            userDto.getPost().stream().map(postId -> {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(sysUser.getUserId());
                userPost.setPostId(postId);
                return userPost;
            }).forEach(SysUserPost::insert);
        }

        // 更新用户部门表
        List<Long> deptIdList = CollUtil.isNotEmpty(userDto.getDeptIds()) ? userDto.getDeptIds()
                : (Objects.nonNull(userDto.getDeptId()) ? Collections.singletonList(userDto.getDeptId()) : null);

        if (CollUtil.isNotEmpty(deptIdList)) {
            // 设置第一个部门为主部门
            sysUser.setDeptId(deptIdList.get(0));
            this.updateById(sysUser);

            // 删除原有部门关联关系
            sysUserDeptMapper
                    .delete(Wrappers.<SysUserDept>lambdaQuery().eq(SysUserDept::getUserId, userDto.getUserId()));

            // 插入新的部门关联关系
            deptIdList.forEach(deptId -> {
                SysUserDept sysUserDept = new SysUserDept();
                sysUserDept.setUserId(sysUser.getUserId());
                sysUserDept.setDeptId(deptId);
                sysUserDept.insert();
            });
        }

        // 同时清理用户失败次数的缓存
        String key = String.format("%s%s:%s", CacheConstants.GLOBALLY
                , CacheConstants.LOGIN_ERROR_TIMES, userDto.getUsername());
        RedisUtils.delete(key);

        return Boolean.TRUE;
    }

    /**
     * 查询上级部门的用户信息
     *
     * @param username 用户名
     * @return R
     */
    @Override
    public List<SysUser> listAncestorUsers(String username) {
        SysUser sysUser = this.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));

        // 查询所属部门
        List<SysDept> sysDeptList = sysDeptMapper.listDeptsByUserId(sysUser.getUserId());
        if (CollUtil.isEmpty(sysDeptList)) {
            return null;
        }

        // 查询用户部门ID
        List<Long> parentDeptList = sysDeptList.stream().map(SysDept::getParentId).toList();
        List<Long> userIdList = sysUserDeptMapper
                .selectList(Wrappers.<SysUserDept>lambdaQuery().in(SysUserDept::getDeptId, parentDeptList))
                .stream()
                .map(SysUserDept::getUserId)
                .toList();
        return this.list(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, userIdList));
    }

    /**
     * 查询全部的用户
     *
     * @param userDTO 查询条件
     * @param ids     ids 用户列表
     * @return list
     */
    @Override
    public List<UserExcelVO> listUser(UserDTO userDTO, Long[] ids) {
        // 根据数据权限查询全部的用户信息
        List<UserVO> voList = baseMapper.getUserVoList(userDTO, ids);
        return voList.stream().map(userVO -> {
            UserExcelVO excelVO = new UserExcelVO();
            BeanUtils.copyProperties(userVO, excelVO);
            // 转换角色名称
            String roleNameList = userVO.getRoleList()
                    .stream()
                    .map(SysRole::getRoleName)
                    .collect(Collectors.joining(StrUtil.COMMA));
            excelVO.setRoleNameList(roleNameList);
            // 转化岗位名称
            String postNameList = userVO.getPostList()
                    .stream()
                    .map(SysPost::getPostName)
                    .collect(Collectors.joining(StrUtil.COMMA));
            excelVO.setPostNameList(postNameList);
            // 转化部门名称
            String deptNameList = userVO.getDeptList()
                    .stream()
                    .map(SysDept::getName)
                    .collect(Collectors.joining(StrUtil.COMMA));
            excelVO.setDeptNameList(deptNameList);
            return excelVO;
        }).toList();
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
                errorMsg.add(MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_USERNAME_EXISTING, excel.getUsername()));
            }

            // 校验手机号是否存在
            boolean exsitPhone = userList.stream().anyMatch(sysUser -> excel.getPhone().equals(sysUser.getPhone()));
            if (exsitPhone) {
                errorMsg.add(MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_PHONE_EXISTING, excel.getPhone()));
            }

            // 判断输入的部门名称列表是否合法
            Optional<SysDept> deptOptional = deptList.stream()
                    .filter(dept -> excel.getDeptNameList().equals(dept.getName()))
                    .findFirst();
            if (deptOptional.isEmpty()) {
                errorMsg.add(MsgUtils.getMessage(UpmsErrorCodes.SYS_DEPT_DEPTNAME_INEXISTENCE, excel.getDeptNameList()));
            }

            // 判断输入的角色名称列表是否合法
            List<String> roleNameList = StrUtil.split(excel.getRoleNameList(), StrUtil.COMMA);
            List<SysRole> roleCollList = roleList.stream()
                    .filter(role -> roleNameList.stream().anyMatch(name -> role.getRoleName().equals(name)))
                    .toList();

            if (roleCollList.size() != roleNameList.size()) {
                errorMsg.add(MsgUtils.getMessage(UpmsErrorCodes.SYS_ROLE_ROLENAME_INEXISTENCE, excel.getRoleNameList()));
            }

            // 判断输入的部门名称列表是否合法
            List<String> postNameList = StrUtil.split(excel.getPostNameList(), StrUtil.COMMA);
            List<SysPost> postCollList = postList.stream()
                    .filter(post -> postNameList.stream().anyMatch(name -> post.getPostName().equals(name)))
                    .toList();

            if (postCollList.size() != postNameList.size()) {
                errorMsg.add(MsgUtils.getMessage(UpmsErrorCodes.SYS_POST_POSTNAME_INEXISTENCE, excel.getPostNameList()));
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
        return R.ok(null, MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_IMPORT_SUCCEED));
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
        userDTO.setLockFlag(excel.getLockFlag());
        // 批量导入初始密码为手机号
        userDTO.setPasswordModifyTime(LocalDateTime.now());
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
     * 注册用户 赋予用户默认角色
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> registerUser(RegisterUserDTO userDto) {
        String feignHeader = WebUtils.getRequest().getHeader(SecurityConstants.FROM);
        // 外部注册校验短信验证码,对 AI 的内部注册跳过验证码处理
        if (!SecurityConstants.FROM_IN.equals(feignHeader)){
            if (StrUtil.isBlank(userDto.getCode())) {
                return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_PARAM_ILLEGAL));
            }
            String codeObj = RedisUtils
                    .get(CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StringPool.AT + userDto.getPhone());
            if (!StrUtil.equals(codeObj, userDto.getCode())) {
                return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_APP_SMS_ERROR));
            }
        }

        // 判断用户名是否存在
        boolean usernameExists = this.exists(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, userDto.getUsername()));
        if (usernameExists) {
            String message = MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_USERNAME_EXISTING, userDto.getUsername());
            return R.failed(message);
        }

        // 判断手机号是否存在
        boolean phoneExists = this.exists(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getPhone, userDto.getPhone()));
        if (phoneExists) {
            String message = MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_PHONE_EXISTING, userDto.getPhone());
            return R.failed(message);
        }

        // 单独的用户避免越权
        UserDTO user = new UserDTO();
        BeanUtils.copyProperties(userDto, user);
        return R.ok(saveUser(user));
    }

    /**
     * 锁定用户
     *
     * @param username 用户名
     * @return 操作结果，包含是否成功
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#username")
    public R<Boolean> lockUser(String username) {
        SysUser sysUser = baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));

        if (Objects.nonNull(sysUser)) {
            sysUser.setLockFlag(UserStateEnum.LOCK.getCode());
            baseMapper.updateById(sysUser);
        }
        return R.ok();
    }

    /**
     * 修改用户密码
     *
     * @param userDto 用户数据传输对象，包含原密码和新密码
     * @return 操作结果
     * @CacheEvict 清除用户详情缓存
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public R changePassword(UserDTO userDto) {
        SysUser user = baseMapper.selectById(SecurityUtils.getUser().getId());
        if (StrUtil.isEmpty(userDto.getPassword())) {
            return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_OLD_PASSWORD_EMPTY));
        }

        if (!ENCODER.matches(userDto.getPassword(), user.getPassword())) {
            log.info("原密码错误，修改个人信息失败:{}", userDto.getUsername());
            return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_UPDATE_PASSWORDERROR));
        }

        if (StrUtil.isEmpty(userDto.getNewpassword1())) {
            return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_NEW_PASSWORD_EMPTY));
        }
        String password = ENCODER.encode(userDto.getNewpassword1());

        this.update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getPassword, password)
                .set(SysUser::getPasswordModifyTime, LocalDateTime.now())
                .eq(SysUser::getUserId, user.getUserId()));
        return R.ok();
    }

    /**
     * 解绑用户第三方登录账号
     *
     * @param type 登录类型，参见LoginTypeEnum枚举
     * @return 操作结果，成功返回R.ok()，失败返回R.failed()
     */
    @Override
    public R unbinding(String type) {
        PigUser user = SecurityUtils.getUser();
        LambdaUpdateWrapper<SysUser> wrapper = null;

        // 微信开放平台 （普通用户扫码登录）
        if (type.equals(LoginTypeEnum.WECHAT.getType())) {
            wrapper = Wrappers.<SysUser>lambdaUpdate()
                    .set(SysUser::getWxOpenid, null)
                    .eq(SysUser::getUserId, user.getId());
            // 码云登录 （方便申请）
        } else if (type.equals(LoginTypeEnum.GITEE.getType())) {
            wrapper = Wrappers.<SysUser>lambdaUpdate()
                    .set(SysUser::getGiteeLogin, null)
                    .eq(SysUser::getUserId, user.getId());
            // 企业微信登录
        } else if (type.equals(LoginTypeEnum.WEIXIN_CP.getType())) {
            wrapper = Wrappers.<SysUser>lambdaUpdate()
                    .set(SysUser::getWxCpUserid, null)
                    .eq(SysUser::getUserId, user.getId());
            // 钉钉登录
        } else if (type.equals(LoginTypeEnum.DINGTALK.getType())) {
            wrapper = Wrappers.<SysUser>lambdaUpdate()
                    .set(SysUser::getWxDingUserid, null)
                    .eq(SysUser::getUserId, user.getId());
        }

        if (Objects.isNull(wrapper)) {
            return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_UNBIND_TYPE_NOT_FOUND));
        }
        this.update(wrapper);
        return R.ok();
    }

    /**
     * 检查用户密码是否正确
     *
     * @param username 用户名
     * @param password 待验证的密码
     * @return 验证结果，成功返回R.ok()，失败返回R.failed()
     */
    @Override
    public R<Boolean> checkPassword(String username, String password) {
        SysUser sysUser = this.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        boolean matches = ENCODER.matches(password, sysUser.getPassword());
        return matches ? R.ok(true) : R.ok(false, MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_PASSWORD_MISMATCH));
    }

    /**
     * 重置用户密码
     *
     * @param userDto 用户信息DTO，包含用户名、原密码和新密码等信息
     * @return 返回操作结果，成功返回true，失败返回错误信息
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public R resetUserPassword(RegisterUserDTO userDto) {
        // 校验密码
        R checkedPassword = checkPassword(userDto.getUsername(), userDto.getPassword());
        if (!checkedPassword.isOk()) {
            return checkedPassword;
        }

        // 新密码校验
        if (StrUtil.equals(userDto.getPassword(), userDto.getNewpassword1())) {
            return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_USER_PASSWORD_SAME));
        }

        // 重置密码
        String password = ENCODER.encode(userDto.getNewpassword1());
        this.update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getPassword, password)
                .set(SysUser::getPasswordModifyTime, LocalDateTime.now())
                .set(SysUser::getPasswordExpireFlag, CommonConstants.STATUS_NORMAL)
                .eq(SysUser::getUsername, userDto.getUsername()));
        return R.ok();
    }

    /**
     * 忘记用户密码
     *
     * @param userDto 用户信息DTO
     * @param code    验证码
     * @return 操作结果，成功返回true
     */
    @Override
    public R<Boolean> forgetUserPassword(RegisterUserDTO userDto, String code) {
        if (StrUtil.isBlank(userDto.getPhone())) {
            return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_PARAM_ILLEGAL));
        }

        String codeObj = RedisUtils
                .get(CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StringPool.AT + userDto.getPhone());
        if (!StrUtil.equals(codeObj, code)) {
            return R.failed(MsgUtils.getMessage(UpmsErrorCodes.SYS_APP_SMS_ERROR));
        }

        String username = lambdaQuery().select(SysUser::getUsername)
                .eq(SysUser::getPhone, userDto.getPhone())
                .one()
                .getUsername();

        // 重置密码
        String password = ENCODER.encode(userDto.getNewpassword1());
        this.update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getPassword, password)
                .set(SysUser::getPasswordModifyTime, LocalDateTime.now())
                .set(SysUser::getPasswordExpireFlag, CommonConstants.STATUS_NORMAL)
                .eq(SysUser::getPhone, userDto.getPhone()));
        cacheManager.getCache(CacheConstants.USER_DETAILS).evict(username);

        return R.ok();
    }

    /**
     * 根据角色ID列表获取用户ID列表
     *
     * @param roleIdList 角色ID列表
     * @return 用户ID列表
     */
    @Override
    public List<Long> listUserIdByRoleIds(List<Long> roleIdList) {
        return sysUserRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getRoleId, roleIdList))
                .stream()
                .map(SysUserRole::getUserId)
                .toList();
    }

    /**
     * 根据部门ID列表获取用户ID列表接口
     *
     * @param deptIdList 部门ID列表
     * @return List<Long> 返回结果对象，包含根据部门ID列表获取到的用户ID列表信息
     */
    @Override
    public List<SysUser> listUserIdByDeptIds(List<Long> deptIdList) {
        List<Long> userList = sysUserDeptMapper
                .selectList(Wrappers.<SysUserDept>lambdaQuery().in(SysUserDept::getDeptId, deptIdList))
                .stream()
                .map(SysUserDept::getUserId)
                .toList();
        return baseMapper.selectList(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, userList));
    }

    /**
     * 根据岗位ID列表获取用户ID列表
     *
     * @param postIdList 岗位ID列表
     * @return 用户ID列表
     */
    @Override
    public List<Long> listUserIdByPostIds(List<Long> postIdList) {
        return sysUserPostMapper.selectList(Wrappers.<SysUserPost>lambdaQuery().in(SysUserPost::getPostId, postIdList))
                .stream()
                .map(SysUserPost::getUserId)
                .toList();
    }

    /**
     * 获取当前用户的部门列表
     *
     * @return 部门列表，主部门排在第一位
     */
    @Override
    public List<SysDept> getUserDeptList() {
        Long userId = SecurityUtils.getUser().getId();

        // 1. 查询用户的所有部门关联
        List<SysUserDept> userDepts = sysUserDeptMapper.selectList(
                Wrappers.<SysUserDept>lambdaQuery()
                        .eq(SysUserDept::getUserId, userId)
        );

        if (CollUtil.isEmpty(userDepts)) {
            return Collections.emptyList();
        }

        // 2. 提取部门ID列表
        List<Long> deptIds = userDepts.stream()
                .map(SysUserDept::getDeptId)
                .toList();

        // 3. 查询部门详细信息
        return sysDeptService.list(
                Wrappers.<SysDept>lambdaQuery()
                        .in(SysDept::getDeptId, deptIds)
        );

    }

    /**
     * 切换用户当前部门
     *
     * @param deptId 部门ID
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateUserDept(Long deptId) {
        PigUser user = SecurityUtils.getUser();
        Long userId = user.getId();
        String username = user.getUsername();

        // 1. 直接更新用户表的主部门ID
        this.update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getDeptId, deptId)
                .eq(SysUser::getUserId, userId));

        // 2. 清除用户缓存，触发权限重新计算
        cacheManager.getCache(CacheConstants.USER_DETAILS).evict(username);

        return R.ok(true, MsgUtils.getMessage(UpmsErrorCodes.SYS_DEPT_SWITCH_SUCCEED));
    }

}
