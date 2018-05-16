package com.github.pig.admin.service;


import com.baomidou.mybatisplus.service.IService;
import com.github.pig.admin.model.dto.MenuTree;
import com.github.pig.admin.model.entity.SysMenu;
import com.github.pig.common.vo.MenuVO;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 通过角色名称查询URL 权限
     *
     * @param role 角色名称
     * @return 菜单列表
     */
    List<MenuVO> findMenuByRoleName(String role);

    /**
     * 通过角色获取菜单权限列表
     *
     * @param roles 角色
     * @return 权限列表
     */
    String[] findPermission(String[] roles);

    /**
     * 级联删除菜单
     *
     * @param id   菜单ID
     * @return 成功、失败
     */
    Boolean deleteMenu(Integer id);

    /**
     * 更新菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 成功、失败
     */
    Boolean updateMenuById(SysMenu sysMenu);

    /**
     * 返回角色的菜单
     * @param roleNames 角色
     * @return 菜单列表
     */
    List<MenuTree> findUserMenuTree(List<String> roleNames);

    /**
     * 返回多个角色的菜单
     * @param roleList 角色列表
     * @return 菜单列表
     */
    List<MenuVO> findMenuByRoles(List<String> roleList);
}
