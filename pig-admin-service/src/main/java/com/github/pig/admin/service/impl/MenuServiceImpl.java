package com.github.pig.admin.service.impl;

import com.github.pig.admin.mapper.SysMenuMapper;
import com.github.pig.admin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    @Cacheable(value = "menu_details", key = "#role +'_menu'")
    public Set<String> findMenuByRole(String role) {
        return sysMenuMapper.findMenuByRoleName(role);
    }
}
