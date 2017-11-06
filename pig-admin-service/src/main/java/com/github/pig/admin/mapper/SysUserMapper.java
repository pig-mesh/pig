package com.github.pig.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.common.vo.UserVo;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过用户名查询用户信息（含有角色信息）
     *
     * @param username 用户名
     * @return userVo
     */
    UserVo selectUserVoByUsername(String username);

    /**
     * 分页查询用户信息（含角色）
     *
     * @param page
     * @param sysUser
     * @return
     */
    List selectUserVoPage(Page<UserVo> page, SysUser sysUser);
}