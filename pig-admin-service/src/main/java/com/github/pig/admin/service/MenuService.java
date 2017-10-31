package com.github.pig.admin.service;

import com.github.pig.admin.entity.SysMenu;

import java.util.Set;

/**
 * @author lengleng
 * @date 2017/10/31
 */
public interface MenuService {
    /**
     * 通过角色名称查询URL 权限
     *
     * @param role 角色名称
     * @return 菜单列表
     */
    Set<String> findMenuByRole(String role);
}
