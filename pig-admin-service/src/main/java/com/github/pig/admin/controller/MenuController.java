package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pig.admin.dto.MenuTree;
import com.github.pig.admin.entity.SysMenu;
import com.github.pig.admin.service.SysMenuService;
import com.github.pig.admin.util.TreeUtil;
import com.github.pig.common.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private SysMenuService menuService;

    /**
     * 通过用户名查询用户菜单
     *
     * @param role 角色名称
     * @return 菜单列表
     */
    @GetMapping("/findMenuByRole/{role}")
    public Set<MenuVo> findMenuByRole(@PathVariable String role) {
        return menuService.findMenuByRole(role);
    }

    @GetMapping(value = "/tree")
    public List<MenuTree> getTree(String title) {
        return getMenuTree(menuService.selectList(new EntityWrapper<>()), -1);
    }

    private List<MenuTree> getMenuTree(List<SysMenu> menus, int root) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        for (SysMenu menu : menus) {
            node = new MenuTree();
            node.setId(menu.getMenuId());
            node.setParentId(menu.getParentId());
            node.setTitle(menu.getMenuName());
            node.setHref(menu.getUrl());
            node.setPath(menu.getUrl());
            node.setCode(menu.getMenuName());
            node.setLabel(menu.getMenuName());
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root);
    }
}
