
package com.anjiplus.template.gaea.business.modules.accessrole.controller;

import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.annotation.AccessKey;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.bean.TreeNode;
import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anji.plus.gaea.holder.UserContentHolder;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.anji.plus.gaea.utils.GaeaUtils;
import com.anjiplus.template.gaea.business.modules.accessrole.dao.entity.AccessRole;
import com.anjiplus.template.gaea.business.modules.accessrole.service.AccessRoleService;
import com.anjiplus.template.gaea.business.modules.accessrole.controller.dto.AccessRoleDto;
import com.anjiplus.template.gaea.business.modules.accessrole.controller.param.AccessRoleParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.List;
import java.util.Map;

/**
* @desc 角色管理 controller
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:14.136
**/
@RestController
@Api(tags = "角色管理管理")
@RequestMapping("/accessRole")
@Permission(code = "roleManage", name = "角色管理")
public class AccessRoleController extends GaeaBaseController<AccessRoleParam, AccessRole, AccessRoleDto> {

    @Autowired
    private AccessRoleService accessRoleService;

    @Override
    public GaeaBaseService<AccessRoleParam, AccessRole> getService() {
        return accessRoleService;
    }

    @Override
    public AccessRole getEntity() {
        return new AccessRole();
    }

    @Override
    public AccessRoleDto getDTO() {
        return new AccessRoleDto();
    }

    /**
     * 获取角色的 菜单按钮树 一级菜单 二级菜单 三级按钮
     * @return
     */
    @Permission( code = "grantAuthority", name = "分配权限")
    @GetMapping("/authorityTree/{roleCode}")
    public ResponseBean authorityTree(@PathVariable("roleCode")String roleCode){
        String operator = UserContentHolder.getContext().getUsername();
        Map map = accessRoleService.getAuthorityTree(roleCode, operator);
        return responseSuccessWithData(map);
    }

    /**
     * 保存角色的权限
     * @return
     */
    @Permission( code = "grantAuthority", name = "分配权限")
    @PostMapping("/saveAuthorityTree")
    public ResponseBean saveAuthorityTree(@RequestBody AccessRoleDto dto){
        Boolean data = accessRoleService.saveAuthorityTree(dto);
        return responseSuccessWithData(data);
    }
}