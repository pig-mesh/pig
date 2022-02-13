/**/
package com.anjiplus.template.gaea.business.modules.accessrole.controller.param;

import lombok.Data;
import java.io.Serializable;
import com.anji.plus.gaea.annotation.Query;
import com.anji.plus.gaea.constant.QueryEnum;
import com.anji.plus.gaea.curd.params.PageParam;
import java.util.List;

import java.util.Date;

/**
* @desc AccessRole 角色管理查询输入类
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:14.136
**/
@Data
public class AccessRoleParam extends PageParam implements Serializable{

    /** 角色编码 */
    @Query(value = QueryEnum.LIKE)
    private String roleCode;

    // 角色名称
    @Query(value = QueryEnum.LIKE)
    private String roleName;

    // 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG
    @Query
    private Integer enableFlag;

}