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

package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.api.vo.UserExcelVO;
import com.pig4cloud.pig.admin.service.SysUserService;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.util.R;

import com.pig4cloud.pig.common.excel.annotation.RequestExcel;
import com.pig4cloud.pig.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器：提供用户相关的REST接口
 *
 * @author lengleng
 * @date 2026-02-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Tag(description = "user", name = "用户管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysUserController {

    private final SysUserService userService;

    /**
     * 获取指定用户全部信息
     *
     * @return 用户信息
     */
    @Inner
    @GetMapping("/info/{username}")
    public R info(@PathVariable String username) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        return userService.getUserInfo(userDTO);
    }

    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @GetMapping(value = {"/info"})
    public R info() {
        String username = SecurityUtils.getUser().getUsername();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        // 获取用户信息，不返回数据库密码字段
        R<UserInfo> userInfoR = userService.getUserInfo(userDTO);
        if (userInfoR.getData() != null) {
            userInfoR.getData().setPassword(null);
        }
        return userInfoR;
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @GetMapping("/details/{id}")
    public R user(@PathVariable Long id) {
        return R.ok(userService.selectUserVoById(id));
    }

    /**
     * 查询用户信息
     *
     * @param query 查询条件
     * @return 不为空返回用户名
     */
    @Inner(value = false)
    @GetMapping("/details")
    public R getDetails(@ParameterObject SysUser query) {
        // 根据用户名、手机号查询
        SysUser sysUser = userService.getOne(Wrappers.query(query), false);
        return R.ok(sysUser == null ? null : CommonConstants.SUCCESS);
    }

    /**
     * 删除用户信息
     *
     * @param ids ID
     * @return R
     */
    @SysLog("删除用户信息")
    @DeleteMapping
    @HasPermission("sys_user_del")
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    public R userDel(@RequestBody Long[] ids) {
        return R.ok(userService.deleteUserByIds(ids));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @SysLog("添加用户")
    @PostMapping
    @HasPermission("sys_user_add")
    public R user(@RequestBody UserDTO userDto) {
        return R.ok(userService.saveUser(userDto));
    }

    /**
     * 分页查询用户
     *
     * @param page    参数集
     * @param userDTO 查询参数列表
     * @return 用户集合
     */
    @GetMapping("/page")
    @HasPermission("sys_user_view")
    public R getUserPage(@ParameterObject Page page, @ParameterObject UserDTO userDTO) {
        return R.ok(userService.getUsersWithRolePage(page, userDTO));
    }

    /**
     * 管理员更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
    @SysLog("更新用户信息")
    @PutMapping
    @HasPermission("sys_user_edit")
    public R updateUser(@Valid @RequestBody UserDTO userDto) {
        return R.ok(userService.updateUser(userDto));
    }

    /**
     * 修改个人信息 （当前用户）
     *
     * @param userDto userDto
     * @return success/false
     */
    @SysLog("修改个人信息")
    @PutMapping("/personal/edit")
    public R updateUserInfo(@Valid @RequestBody UserDTO userDto) {
        return userService.updateUserInfo(userDto);
    }

    /**
     * 修改个人密码 （当前用户）
     *
     * @param userDto 用户DTO对象，包含需要修改密码的用户信息
     * @return R 返回结果对象，包含修改密码操作的结果信息
     */
    @PutMapping("/personal/password")
    public R updateUserPassword(@RequestBody UserDTO userDto) {
        String username = SecurityUtils.getUser().getUsername();
        userDto.setUsername(username);
        return userService.changePassword(userDto);
    }

    /**
     * @param username 用户名称
     * @return 上级部门用户列表
     */
    @GetMapping("/ancestor/{username}")
    public R listAncestorUsers(@PathVariable String username) {
        return R.ok(userService.listAncestorUsers(username));
    }

    /**
     * 导出excel 表格
     *
     * @param userDTO 查询条件
     * @return
     */
    @ResponseExcel
    @GetMapping("/export")
    @HasPermission("sys_user_export")
    public List export(UserDTO userDTO, Long[] ids) {
        return userService.listUser(userDTO, ids);
    }

    /**
     * 导入用户
     *
     * @param excelVOList   用户列表
     * @param bindingResult 错误信息列表
     * @return R
     */
    @PostMapping("/import")
    @HasPermission("sys_user_export")
    public R importUser(@RequestExcel List<UserExcelVO> excelVOList, BindingResult bindingResult) {
        return userService.importUser(excelVOList, bindingResult);
    }

    /**
     * 锁定指定用户
     *
     * @param username 用户名
     * @return R
     */
    @Inner
    @PutMapping("/lock/{username}")
    public R lockUser(@PathVariable String username) {
        return userService.lockUser(username);
    }

    /**
     * 解绑定接口
     *
     * @param type 需要解绑定的类型
     * @return R 返回结果对象，包含解绑定操作的结果信息
     */
    @PostMapping("/unbinding")
    public R unbinding(String type) {
        return userService.unbinding(type);
    }

    /**
     * 校验密码接口
     *
     * @param password 需要校验的密码
     * @return R 返回结果对象，包含校验密码操作的结果信息
     */
    @PostMapping("/check")
    public R check(String password) {
        return userService.checkPassword(SecurityUtils.getUser().getUsername(), password);
    }

    /**
     * 根据角色ID列表获取用户ID列表接口
     *
     * @param roleIdList 角色ID列表
     * @return R 返回结果对象，包含根据角色ID列表获取到的用户ID列表信息
     */
    @Inner
    @GetMapping("/getUserIdListByRoleIdList")
    public R<List<Long>> getUserIdListByRoleIdList(Long[] roleIdList) {
        return R.ok(userService.listUserIdByRoleIds(CollUtil.toList(roleIdList)));
    }

    /**
     * 根据部门ID列表获取用户ID列表接口
     *
     * @param deptIdList 部门ID列表
     * @return R 返回结果对象，包含根据部门ID列表获取到的用户ID列表信息
     */
    @Inner
    @GetMapping("/getUserIdListByDeptIdList")
    public R<List<SysUser>> getUserIdListByDeptIdList(Long[] deptIdList) {
        return R.ok(userService.listUserIdByDeptIds(CollUtil.toList(deptIdList)));
    }

    /**
     * 根据岗位ID列表获取用户ID列表接口
     *
     * @param postIdList 岗位ID列表
     * @return R 返回结果对象，包含根据岗位ID列表获取到的用户ID列表信息
     */
    @Inner
    @GetMapping("/getUserIdListByPostIdList")
    public R<List<Long>> getUserIdListByPostIdList(Long[] postIdList) {
        return R.ok(userService.listUserIdByPostIds(CollUtil.toList(postIdList)));
    }

    /**
     * 根据用户名获取用户列表
     *
     * @param username 用户名
     * @return 用户列表
     */
    @Inner
    @GetMapping("/getUserListByUserName")
    public R<List<SysUser>> getUserListByUserName(String username) {
        return R.ok(userService.list(Wrappers.<SysUser>lambdaQuery().like(SysUser::getUsername, username)));
    }

    /**
     * 根据IDS获取用户列表
     *
     * @param userIds ID列表
     * @return 用户列表
     */
    @GetMapping("/list")
    public R<List<SysUser>> getUserListByIds(@RequestParam("userIds") List<Long> userIds) {
        return R.ok(userService.list(Wrappers.<SysUser>lambdaQuery().in(SysUser::getUserId, userIds)));
    }

    /**
     * 获取当前用户的部门列表
     *
     * @return 部门列表
     */
    @GetMapping("/personal/dept")
    @Operation(summary = "获取当前用户的部门列表", description = "返回当前登录用户所属的所有部门")
    public R<List<SysDept>> getUserDeptList() {
        return R.ok(userService.getUserDeptList());
    }

    /**
     * 切换当前用户的部门
     *
     * @param userDTO 部门ID
     * @return 操作结果
     */
    @PutMapping("/personal/dept/update")
    @Operation(summary = "切换当前部门", description = "切换当前用户的工作部门")
    public R<Boolean> updateUserDept(@RequestBody UserDTO userDTO) {
        return userService.updateUserDept(userDTO.getDeptId());
    }

}
