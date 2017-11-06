package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.dto.UserDto;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.admin.entity.SysUserRole;
import com.github.pig.admin.service.SysUserRoleService;
import com.github.pig.admin.service.UserService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.vo.UserVo;
import com.github.pig.common.web.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author lengleng
 * @date 2017/10/28
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 获取当前用户的用户名
     *
     * @return 用户名
     */
    @GetMapping
    public String user() {
        return getUser();
    }

    /**
     * 通过ID查询当前用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public SysUser user(@PathVariable Integer id) {
        return userService.selectById(id);
    }

    @DeleteMapping("/{id}")
    public Boolean userDel(@PathVariable Integer id){
        SysUser sysUser = userService.selectById(id);
        sysUser.setDelFlag(CommonConstant.STATUS_DEL);
        return userService.updateById(sysUser);
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @PostMapping
    public Boolean user(@RequestBody UserDto userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
        userService.insert(sysUser);
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(sysUser.getUserId());
        userRole.setRoleId(userDto.getRole());
        userRole.insert();
        return Boolean.TRUE;
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return boolean
     */
    @PutMapping
    public Boolean userUpdate(@RequestBody UserDto userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(new Date());
        userService.updateById(sysUser);

        SysUserRole condition = new SysUserRole();
        condition.setUserId(userDto.getUserId());
        SysUserRole sysUserRole = sysUserRoleService.selectOne(new EntityWrapper<>(condition));
        sysUserRole.setRoleId(userDto.getRole());
        sysUserRoleService.update(sysUserRole, new EntityWrapper<>(condition));
        return Boolean.TRUE;
    }

    /**
     * 通过用户名查询用户及其角色信息
     *
     * @param username 用户名
     * @return UseVo 对象
     */
    @RequestMapping("/findUserByUsername/{username}")
    public UserVo findUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    /**
     * 分页查询用户
     *
     * @param page    页码
     * @param limit   每页数量
     * @param sysUser 检索条件
     * @return 用户集合
     */
    @RequestMapping("/userPage")
    public Page userPage(Integer page, Integer limit, SysUser sysUser) {
        return userService.selectWithRolePage(new Page<>(page, limit), sysUser);
    }

}
