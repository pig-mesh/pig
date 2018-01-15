package com.github.pig.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.common.util.Query;
import com.github.pig.common.vo.UserVo;

import java.util.List;
import java.util.Map;

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
     * @param query  查询条件
     * @param params 参数
     * @return list
     */
    List selectUserVoPage(Query query, Map<String, Object> params);

    /**
     * 通过手机号查询用户信息（含有角色信息）
     *
     * @param mobile 用户名
     * @return userVo
     */
    UserVo selectUserVoByMobile(String mobile);
}