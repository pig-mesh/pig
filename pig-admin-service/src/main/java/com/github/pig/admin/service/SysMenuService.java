package com.github.pig.admin.service;


import com.baomidou.mybatisplus.service.IService;
import com.github.pig.admin.entity.SysMenu;
import com.github.pig.common.vo.MenuVo;

import java.util.Set;

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
    Set<MenuVo> findMenuByRole(String role);
}
