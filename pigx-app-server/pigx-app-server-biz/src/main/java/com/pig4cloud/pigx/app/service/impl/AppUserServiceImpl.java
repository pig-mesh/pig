/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.pig4cloud.pigx.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.app.api.dto.AppUserDTO;
import com.pig4cloud.pigx.app.api.dto.AppUserInfo;
import com.pig4cloud.pigx.app.api.entity.AppRole;
import com.pig4cloud.pigx.app.api.entity.AppUser;
import com.pig4cloud.pigx.app.api.entity.AppUserRole;
import com.pig4cloud.pigx.app.api.enums.AppErrorCodes;
import com.pig4cloud.pigx.app.api.vo.AppUserExcelVO;
import com.pig4cloud.pigx.app.api.vo.AppUserVO;
import com.pig4cloud.pigx.app.mapper.AppUserMapper;
import com.pig4cloud.pigx.app.service.AppRoleService;
import com.pig4cloud.pigx.app.service.AppUserRoleService;
import com.pig4cloud.pigx.app.service.AppUserService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pigx.common.core.constant.enums.UserTypeEnum;
import com.pig4cloud.pigx.common.core.util.MsgUtils;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.cache.RedisUtils;
import com.pig4cloud.pigx.common.excel.vo.ErrorMessage;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * app用户表服务实现类
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements AppUserService {

    /**
     * 密码编码器，用于密码加密
     */
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * 用户角色关联服务
     */
    private final AppUserRoleService appUserRoleService;

    /**
     * 角色服务
     */
    private final AppRoleService appRoleService;

    /**
     * 缓存管理器
     */
    private final CacheManager cacheManager;


    /**
     * 更新用户信息
     *
     * @param userDTO 用户数据传输对象
     * @return 更新结果
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS_MINI, key = "#userDTO.username")
    public Boolean updateUser(AppUserDTO userDTO) {
        // 创建用户实体并复制属性
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(userDTO, appUser);

        // 如果密码不为空，进行加密处理
        if (StrUtil.isNotBlank(userDTO.getPassword())) {
            appUser.setPassword(ENCODER.encode(userDTO.getPassword()));
        }
        // 更新用户基本信息
        this.updateById(appUser);

        // 删除用户与角色的关联关系
        appUserRoleService.remove(Wrappers.<AppUserRole>lambdaQuery().eq(AppUserRole::getUserId, appUser.getUserId()));
        // 重新建立用户与角色的关联关系
        userDTO.getRole().forEach(roleId -> {
            AppUserRole appUserRole = new AppUserRole();
            appUserRole.setRoleId(roleId);
            appUserRole.setUserId(userDTO.getUserId());
            appUserRole.insert();
        });

        // 清除以手机号为key的用户缓存
        AppUser appUserInfo = baseMapper.selectById(userDTO.getUserId());
        cacheManager.getCache(CacheConstants.USER_DETAILS_MINI).evict(appUserInfo.getPhone());
        return Boolean.TRUE;
    }

    /**
     * 新增用户
     *
     * @param userDTO 用户数据传输对象
     * @return 保存结果
     */
    @Override
    public Boolean saveUser(AppUserDTO userDTO) {
        // 创建用户实体并复制属性
        AppUser appUser = new AppUser();
        BeanUtils.copyProperties(userDTO, appUser);
        // 设置删除标志为正常状态
        appUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        // 对密码进行加密
        appUser.setPassword(ENCODER.encode(userDTO.getPassword()));
        // 插入用户记录
        baseMapper.insert(appUser);

        // 如果角色列表不为空，建立用户与角色的关联关系
        if (CollUtil.isNotEmpty(userDTO.getRole())) {
            List<AppUserRole> userRoleList = userDTO.getRole().stream().map(roleId -> {
                AppUserRole userRole = new AppUserRole();
                userRole.setUserId(appUser.getUserId());
                userRole.setRoleId(roleId);
                return userRole;
            }).toList();
            // 批量保存用户角色关联
            appUserRoleService.saveBatch(userRoleList);
        }
        return Boolean.TRUE;
    }

    /**
     * 查询全部用户（用于Excel导出）
     *
     * @param appUser 查询条件
     * @return 用户Excel视图对象列表
     */
    @Override
    public List<AppUserExcelVO> listUser(AppUserDTO appUser) {
        // 查询所有用户
        List<AppUser> appUsers = baseMapper.selectList(null);
        // 将用户实体转换为Excel视图对象
        List<AppUserExcelVO> appUserExcelVOS = appUsers.stream().map(item -> {
            AppUserExcelVO appUserExcelVO = new AppUserExcelVO();
            BeanUtils.copyProperties(item, appUserExcelVO);
            return appUserExcelVO;
        }).toList();
        return appUserExcelVOS;
    }

    /**
     * 分页查询用户（包含角色信息）
     *
     * @param page       分页参数
     * @param appUserDTO 查询条件
     * @return 分页结果
     */
    @Override
    public IPage getUsersWithRolePage(Page page, AppUserDTO appUserDTO) {
        return baseMapper.getUserVosPage(page, appUserDTO);
    }

    /**
     * 查询用户详细信息（包含角色和权限）
     *
     * @param user 用户对象
     * @return 用户信息对象
     */
    @Override
    public AppUserInfo findUserInfo(AppUser user) {
        // 创建用户信息对象
        AppUserInfo info = new AppUserInfo();
        info.setAppUser(user);

        // 设置角色列表（ID数组）
        List<Long> roleIds = appRoleService.findRolesByUserId(user.getUserId())
                .stream()
                .map(AppRole::getRoleId)
                .toList();
        info.setRoles(ArrayUtil.toArray(roleIds, Long.class));

        // 设置权限列表（暂为空）
        Set<String> permissions = new HashSet<>();
        info.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return info;
    }

    /**
     * 更新用户个人信息
     *
     * @param userDTO 用户数据传输对象
     * @return 操作结果
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS_MINI, key = "#userDTO.username")
    public R updateUserInfo(AppUserDTO userDTO) {
        // 获取当前登录用户
        PigxUser user = SecurityUtils.getUser();

        // C端客户修改手机号需要验证短信验证码
        if (UserTypeEnum.TOC.getStatus().equals(user.getUserType()) && StrUtil.isNotBlank(userDTO.getPhone())) {
            // 构建验证码缓存key
            String key = CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.APPSMS.getType() + StringPool.AT
                    + userDTO.getPhone();
            // 获取缓存中的验证码
            String codeObj = RedisUtils.get(key);

            // 验证码不匹配，返回错误
            if (!userDTO.getMobileCode().equals(codeObj)) {
                return R.failed("验证码错误");
            }
        }

        // 更新密码（如果提供了新密码）
        if (StrUtil.isNotBlank(userDTO.getPassword())) {
            userDTO.setPassword(ENCODER.encode(userDTO.getPassword()));
        }

        // 获取原用户信息并更新
        AppUser appUser = baseMapper.selectById(userDTO.getUserId());
        BeanUtils.copyProperties(userDTO, appUser);

        // 清除以手机号为key的用户缓存
        cacheManager.getCache(CacheConstants.USER_DETAILS_MINI).evict(user.getPhone());
        return R.ok(this.updateById(appUser));
    }

    /**
     * 根据用户ID查询用户视图对象
     *
     * @param userId 用户ID
     * @return 用户视图对象
     */
    @Override
    public AppUserVO selectUserVoById(Long userId) {
        return baseMapper.getUserVoById(userId);
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户ID数组
     * @return 删除结果
     */
    @Override
    public Boolean deleteAppUserByIds(Long[] ids) {
        // 获取缓存对象
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS_MINI);
        // 清除用户缓存
        for (AppUser appUser : baseMapper.selectByIds(CollUtil.toList(ids))) {
            cache.evict(appUser.getUsername());
        }

        // 删除用户角色关联关系
        this.appUserRoleService
                .remove(Wrappers.<AppUserRole>lambdaQuery().in(AppUserRole::getUserId, CollUtil.toList(ids)));

        // 删除用户记录
        this.removeBatchByIds(CollUtil.toList(ids));

        return Boolean.TRUE;
    }

    /**
     * 注册APP用户
     *
     * @param appUser 用户数据传输对象
     * @return 操作结果
     */
    @Override
    public R registerAppUser(AppUserDTO appUser) {
        // 检查手机号是否已被注册
        List<AppUser> appUserList = baseMapper
                .selectList(Wrappers.<AppUser>lambdaQuery().eq(AppUser::getPhone, appUser.getPhone()));

        if (CollUtil.isNotEmpty(appUserList)) {
            return R.failed("手机号已注册，请使用验证码直接登录");
        }

        // 验证短信验证码
        String key = CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.APPSMS.getType() + StringPool.AT
                + appUser.getPhone();
        String codeObj = RedisUtils.get(key);

        if (!appUser.getMobileCode().equals(codeObj)) {
            return R.failed("验证码错误");
        }

        // 创建用户并保存
        AppUser app = new AppUser();
        BeanUtils.copyProperties(appUser, app);
        appUser.setUsername(app.getPhone()); // 默认使用手机号作为用户名
        appUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        appUser.setPassword(ENCODER.encode(appUser.getPassword()));
        baseMapper.insert(appUser);
        return null;
    }

    /**
     * 导入用户（从Excel导入）
     *
     * @param excelVOList   Excel数据列表
     * @param bindingResult 数据校验结果
     * @return 导入结果
     */
    @Override
    public R importUser(List<AppUserExcelVO> excelVOList, BindingResult bindingResult) {
        // 获取通用校验失败的数据
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        // 获取现有用户和角色列表，用于校验
        List<AppUser> userList = this.list();
        List<AppRole> roleList = appRoleService.list();

        // 遍历Excel数据进行处理
        for (AppUserExcelVO excel : excelVOList) {
            Set<String> errorMsg = new HashSet<>();

            // 校验用户名是否已存在
            boolean exsitUserName = userList.stream()
                    .anyMatch(sysUser -> excel.getUsername().equals(sysUser.getUsername()));

            if (exsitUserName) {
                errorMsg.add(MsgUtils.getMessage(AppErrorCodes.APP_USER_USERNAME_EXISTING, excel.getUsername()));
            }

            // 校验角色名称是否合法
            List<String> roleNameList = StrUtil.split(excel.getRoleNameList(), StrUtil.COMMA);
            List<AppRole> roleCollList = roleList.stream()
                    .filter(role -> roleNameList.stream().anyMatch(name -> role.getRoleName().equals(name)))
                    .toList();

            if (roleCollList.size() != roleNameList.size()) {
                errorMsg.add(MsgUtils.getMessage(AppErrorCodes.APP_ROLE_ROLENAME_INEXISTENCE, excel.getRoleNameList()));
            }

            // 数据合法时执行插入
            if (CollUtil.isEmpty(errorMsg)) {
                insertExcelUser(excel, roleCollList);
            } else {
                // 记录不合法数据的错误信息
                errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
            }
        }

        // 存在错误信息时返回失败
        if (CollUtil.isNotEmpty(errorMessageList)) {
            return R.failed(errorMessageList);
        }
        return R.ok(null, MsgUtils.getMessage(AppErrorCodes.APP_USER_IMPORT_SUCCEED));
    }

    /**
     * 插入Excel导入的用户数据
     *
     * @param excel        Excel用户数据
     * @param roleCollList 角色列表
     */
    private void insertExcelUser(AppUserExcelVO excel, List<AppRole> roleCollList) {
        // 创建用户DTO并设置基本信息
        AppUserDTO userDTO = new AppUserDTO();
        userDTO.setUsername(excel.getUsername());
        userDTO.setPhone(excel.getPhone());
        userDTO.setNickname(excel.getNickname());
        userDTO.setName(excel.getName());
        userDTO.setEmail(excel.getEmail());
        // 批量导入时初始密码设置为手机号
        userDTO.setPassword(userDTO.getPhone());

        // 获取角色ID列表
        List<Long> roleIdList = roleCollList.stream().map(AppRole::getRoleId).toList();
        userDTO.setRole(roleIdList);

        // 保存用户
        this.saveUser(userDTO);
    }
}
