package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pig.admin.entity.SysRole;
import com.github.pig.admin.service.SysRoleService;
import com.github.pig.common.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lengleng
 * @date 2017/11/5
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/roleList")
    public List<SysRole> roleList() {
        SysRole condition = new SysRole();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        return sysRoleService.selectList(new EntityWrapper<>(condition));
    }
}
