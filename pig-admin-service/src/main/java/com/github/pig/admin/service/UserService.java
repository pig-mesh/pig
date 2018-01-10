package com.github.pig.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.github.pig.admin.dto.UserDto;
import com.github.pig.admin.dto.UserInfo;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.common.util.Query;
import com.github.pig.common.vo.UserVo;

/**
 * @author lengleng
 * @date 2017/10/31
 */
public interface UserService extends IService<SysUser> {
    /**
     * 根据用户名查询用户角色信息
     *
     * @param username 用户名
     * @return userVo
     */
    UserVo findUserByUsername(String username);

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param query 查询条件
     * @return
     */
    Page selectWithRolePage(Query query);

    /**
     * 查询用户信息
     *
     * @param userVo 角色名
     * @return userInfo
     */
    UserInfo findUserInfo(UserVo userVo);

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
    Boolean updateUserInfo(UserDto userDto, String username);

    /**
     * 更新指定用户信息
     * @param userDto 用户信息
     * @param username 用户信息
     * @return
     */
    Boolean updateUser(UserDto userDto, String username);

    /**
     * 通过手机号查询用户信息
     * @param mobile 手机号
     * @return 用户信息
     */
    UserVo findUserByMobile(String mobile);

    /**
     * 发送验证码
     * @param mobile 手机号
     * @return true、false
     */
    Boolean sendSmsCode(String mobile);
}
