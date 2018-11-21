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

package com.github.pig.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.github.pig.admin.model.dto.UserDTO;
import com.github.pig.admin.model.dto.UserInfo;
import com.github.pig.admin.model.entity.SysUser;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.common.vo.UserVO;

/**
 * @author lengleng
 * @date 2017/10/31
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 根据用户名查询用户角色信息
     *
     * @param username 用户名
     * @return userVo
     */
    UserVO findUserByUsername(String username);

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param query 查询条件
     * @param userVO
     * @return
     */
    Page selectWithRolePage(Query query, UserVO userVO);

    /**
     * 查询用户信息
     *
     * @param userVo 角色名
     * @return userInfo
     */
    UserInfo findUserInfo(UserVO userVo);

    /**
     * 保存验证码
     *  @param randomStr 随机串
     * @param imageCode 验证码*/
    void saveImageCode(String randomStr, String imageCode);

    /**
     * 删除用户
     * @param sysUser 用户
     * @return boolean
     */
    Boolean deleteUserById(SysUser sysUser);

    /**
     * 更新当前用户基本信息
     * @param userDto 用户信息
     * @param username 用户名
     * @return Boolean
     */
    R<Boolean> updateUserInfo(UserDTO userDto, String username);

    /**
     * 更新指定用户信息
     * @param userDto 用户信息
     * @return
     */
    Boolean updateUser(UserDTO userDto);

    /**
     * 通过手机号查询用户信息
     * @param mobile 手机号
     * @return 用户信息
     */
    UserVO findUserByMobile(String mobile);

    /**
     * 发送验证码
     * @param mobile 手机号
     * @return R
     */
    R<Boolean> sendSmsCode(String mobile);

    /**
     * 通过openId查询用户
     * @param openId openId
     * @return 用户信息
     */
    UserVO findUserByOpenId(String openId);

    /**
     * 通过ID查询用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    UserVO selectUserVoById(Integer id);
}
