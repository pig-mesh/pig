package com.github.pig.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.admin.entity.SysRoleMenu;
import com.github.pig.admin.mapper.SysRoleMenuMapper;
import com.github.pig.admin.service.SysRoleMenuService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    @Override
    @CacheEvict(value = "menu_details", key = "#role + '_menu'")
    public Boolean insertRoleMenus(String role, Integer roleId, Integer[] menuIds) {
        SysRoleMenu condition = new SysRoleMenu();
        condition.setRoleId(roleId);
        this.delete(new EntityWrapper<>(condition));

        List<SysRoleMenu> roleMenuList = new ArrayList<>();
        for (Integer menuId : menuIds) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        return this.insertBatch(roleMenuList);
    }
}
