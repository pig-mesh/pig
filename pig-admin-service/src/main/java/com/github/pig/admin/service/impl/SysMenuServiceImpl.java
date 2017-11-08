package com.github.pig.admin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.admin.entity.SysMenu;
import com.github.pig.admin.mapper.SysMenuMapper;
import com.github.pig.admin.service.SysMenuService;
import com.github.pig.common.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    //@Cacheable(value = "menu_details", key = "#role +'_menu'")
    public Set<MenuVo> findMenuByRole(String role) {
        return sysMenuMapper.findMenuByRoleName(role);
    }
}
