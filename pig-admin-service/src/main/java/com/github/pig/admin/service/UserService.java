package com.github.pig.admin.service;

import com.github.pig.common.vo.UserVo;

/**
 * @author lengleng
 * @date 2017/10/31
 */
public interface UserService {
    /**
     * 根据用户名查询用户角色信息
     *
     * @param username 用户名
     * @return userVo
     */
    UserVo findUserByUsername(String username);
}
