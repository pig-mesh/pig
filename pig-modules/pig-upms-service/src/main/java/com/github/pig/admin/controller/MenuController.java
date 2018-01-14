package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pig.admin.dto.MenuTree;
import com.github.pig.admin.entity.SysMenu;
import com.github.pig.admin.service.SysMenuService;
import com.github.pig.admin.common.util.TreeUtil;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.R;
import com.github.pig.common.vo.MenuVo;
import com.github.pig.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 通过用户名查询用户菜单
     *
     * @param role 角色名称
     * @return 菜单列表
     */
    @GetMapping("/findMenuByRole/{role}")
    public Set<MenuVo> findMenuByRole(@PathVariable String role) {
        return sysMenuService.findMenuByRole(role);
    }

    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/tree")
    public List<MenuTree> getTree() {
        SysMenu condition = new SysMenu();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        return getMenuTree(sysMenuService.selectList(new EntityWrapper<>(condition)), -1);
    }

    /**
     * 返回当前用户树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping("/userTree/")
    public List<Integer> userTree() {
        Set<MenuVo> menus = sysMenuService.findMenuByRole(getRole().get(0));
        List<Integer> menuList = new ArrayList<>();
        for (MenuVo menuVo : menus) {
            menuList.add(menuVo.getMenuId());
        }
        return menuList;
    }

    /**
     * 返回角色的菜单集合
     *
     * @param roleName 角色名称
     * @return 属性集合
     */
    @GetMapping("/roleTree/{roleName}")
    public List<Integer> roleTree(@PathVariable String roleName) {
        Set<MenuVo> menus = sysMenuService.findMenuByRole(roleName);
        List<Integer> menuList = new ArrayList<>();
        for (MenuVo menuVo : menus) {
            menuList.add(menuVo.getMenuId());
        }
        return menuList;
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param id 菜单ID
     * @return 菜单详细信息
     */
    @GetMapping("/{id}")
    public SysMenu menu(@PathVariable Integer id) {
        return sysMenuService.selectById(id);
    }

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return success/false
     */
    @PostMapping
    public R<Boolean> menu(@RequestBody SysMenu sysMenu) {
        return new R<>(sysMenuService.insert(sysMenu));
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return success/false
     * TODO  级联删除下级节点
     */
    @DeleteMapping("/{id}")
    public R<Boolean> menuDel(@PathVariable Integer id) {
        return new R<>(sysMenuService.deleteMenu(id,getRole().get(0)));
    }

    @PutMapping
    public R<Boolean> menuUpdate(@RequestBody SysMenu sysMenu) {
        return new R<>(sysMenuService.updateMenuById(sysMenu,getRole().get(0)));
    }

    private List<MenuTree> getMenuTree(List<SysMenu> menus, int root) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        for (SysMenu menu : menus) {
            node = new MenuTree();
            node.setId(menu.getMenuId());
            node.setParentId(menu.getParentId());
            node.setName(menu.getName());
            node.setUrl(menu.getUrl());
            node.setPath(menu.getPath());
            node.setCode(menu.getPermission());
            node.setLabel(menu.getName());
            node.setComponent(menu.getComponent());
            node.setIcon(menu.getIcon());
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root);
    }
}
