package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.entity.SysRole;
import com.github.pig.admin.entity.SysRoleMenu;
import com.github.pig.admin.service.SysRoleMenuService;
import com.github.pig.admin.service.SysRoleService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2017/11/5
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 通过ID查询角色信息
     *
     * @param id ID
     * @return 角色信息
     */
    @GetMapping("/{id}")
    public SysRole role(@PathVariable Integer id) {
        return sysRoleService.selectById(id);
    }

    /**
     * 添加角色
     *
     * @param sysRole 角色信息
     * @return success、false
     */
    @PostMapping
    public R<Boolean> role(@RequestBody SysRole sysRole) {
        return new R<>(sysRoleService.insert(sysRole));
    }

    /**
     * 修改角色
     *
     * @param sysRole 角色信息
     * @return success/false
     */
    @PutMapping
    public R<Boolean> roleUpdate(@RequestBody SysRole sysRole) {
        return new R<>(sysRoleService.updateById(sysRole));
    }

    @DeleteMapping("/{id}")
    public R<Boolean> roleDel(@PathVariable Integer id) {
        SysRole sysRole = sysRoleService.selectById(id);
        sysRole.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(sysRoleService.updateById(sysRole));
    }

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    @GetMapping("/roleList")
    public List<SysRole> roleList() {
        SysRole condition = new SysRole();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        return sysRoleService.selectList(new EntityWrapper<>(condition));

    }

    /**
     * 分页查询角色信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @RequestMapping("/rolePage")
    public Page rolePage(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return sysRoleService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 更新角色菜单
     *
     * @param roleId  角色ID
     * @param menuIds 菜单结合
     * @return success、false
     */
    @PutMapping("/roleMenuUpd")
    public R<Boolean> roleMenuUpd(Integer roleId, @RequestParam("menuIds[]") Integer[] menuIds) {
        SysRole sysRole = sysRoleService.selectById(roleId);
        return new R<>(sysRoleMenuService.insertRoleMenus(sysRole.getRoleCode(), roleId, menuIds));
    }
}
