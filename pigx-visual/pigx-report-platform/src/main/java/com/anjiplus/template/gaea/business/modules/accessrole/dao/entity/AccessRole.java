
package com.anjiplus.template.gaea.business.modules.accessrole.dao.entity;

import lombok.Data;
import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import javax.validation.constraints.*;
import java.util.Date;
/**
* @description 角色管理 entity
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:14.136
**/
@TableName(keepGlobalPrefix=true, value="access_role")
@Data
public class AccessRole extends GaeaBaseEntity {

    /** 角色编码 */
    private String roleCode;

    /** 角色名称 */
    private String roleName;

    /**  0--未删除 1--已删除 DIC_NAME=DEL_FLAG */
    private Integer deleteFlag;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
    private Integer enableFlag;

}
