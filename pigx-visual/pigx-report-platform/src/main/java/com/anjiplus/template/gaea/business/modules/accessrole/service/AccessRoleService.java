
package com.anjiplus.template.gaea.business.modules.accessrole.service;

import com.anji.plus.gaea.bean.ResponseBean;
import com.anjiplus.template.gaea.business.modules.accessrole.controller.dto.AccessRoleDto;
import com.anjiplus.template.gaea.business.modules.accessrole.dao.entity.AccessRole;
import com.anjiplus.template.gaea.business.modules.accessrole.controller.param.AccessRoleParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.anji.plus.gaea.curd.service.GaeaBaseService;

import java.util.Map;

/**
* @desc AccessRole 角色管理服务接口
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:14.136
**/
public interface AccessRoleService extends GaeaBaseService<AccessRoleParam, AccessRole> {

    /** 查询某角色的权限树
     * @param roleCode 被操作的对象
     * @param operator 当前登录者
     * @return
     */
    Map getAuthorityTree(String roleCode, String operator);


    /** 保存角色的权限
     * @param accessRoleDto
     * @return
     */
    Boolean saveAuthorityTree(AccessRoleDto accessRoleDto);
}