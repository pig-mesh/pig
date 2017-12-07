package com.github.pig.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.github.pig.admin.entity.SysUser;
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
     * @param page    分页对象
     * @param sysUser 查询条件
     * @return
     */
    Page selectWithRolePage(Page<UserVo> page, SysUser sysUser);

    /**
     * 清除用户缓存
     *
     * @param userName 用户名
     */
    void clearCache(String userName);
}
