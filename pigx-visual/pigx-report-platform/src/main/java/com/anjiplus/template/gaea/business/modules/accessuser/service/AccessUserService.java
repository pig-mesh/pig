
package com.anjiplus.template.gaea.business.modules.accessuser.service;

import com.anjiplus.template.gaea.business.modules.accessuser.controller.dto.AccessUserDto;
import com.anjiplus.template.gaea.business.modules.accessuser.controller.dto.GaeaUserDto;
import com.anjiplus.template.gaea.business.modules.accessuser.controller.dto.UpdatePasswordDto;
import com.anjiplus.template.gaea.business.modules.accessuser.dao.entity.AccessUser;
import com.anjiplus.template.gaea.business.modules.accessuser.controller.param.AccessUserParam;
import com.anji.plus.gaea.curd.service.GaeaBaseService;

import java.util.Map;

/**
* @desc AccessUser 用户管理服务接口
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:11.902
**/
public interface AccessUserService extends GaeaBaseService<AccessUserParam, AccessUser> {

    /** 获取用户的角色树
     * @param loginName 被操作的对象
     * @param operator 当前登录者
     * @return
     */
    Map getRoleTree(String loginName, String operator);


    /** 保存用户的角色树
     * @param accessUserDto
     * @return
     */
    Boolean saveRoleTree(AccessUserDto accessUserDto);

    /** 重置密码
     * @param gaeaUserDto
     * @return
     */
    Boolean resetPassword(GaeaUserDto gaeaUserDto);

    /** 用户登录
     * @param gaeaUserDto
     * @return
     */
    GaeaUserDto login(GaeaUserDto gaeaUserDto);

    /**
     * 修改密码
     * @param dto
     * @return
     */
    Boolean updatePassword(UpdatePasswordDto dto);
}
