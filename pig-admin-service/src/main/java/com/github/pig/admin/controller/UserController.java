package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.admin.service.UserService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.vo.UserVo;
import com.github.pig.common.web.BaseController;
import org.apache.commons.lang.StringUtils;
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

    @GetMapping
    public String user() {
        return getUser();
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
    @RequestMapping("/userList")
    public Page userList(Integer page, Integer limit, SysUser sysUser) {
        EntityWrapper wrapper = new EntityWrapper();
        if (StringUtils.isNotEmpty(sysUser.getUsername())) {
            wrapper.like("username", sysUser.getUsername());
        }
        return userService.selectPage(new Page<>(page, limit), wrapper);
    }

    /**
     * 添加用户
     *
     * @param sysUser 用户信息
     * @return success/false
     */
    @RequestMapping("/userAdd")
    public Boolean userAdd(@RequestBody SysUser sysUser) {
        sysUser.setCreateTime(new Date());
        sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
        return userService.insert(sysUser);
    }
}
