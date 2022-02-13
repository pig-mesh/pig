
package com.anjiplus.template.gaea.business.modules.accessuser.dao.entity;

import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
* @description 用户--角色关联关系 entity
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:11.902
**/
@TableName(keepGlobalPrefix=true, value="access_user_role")
@Data
public class AccessUserRole extends GaeaBaseEntity {

    /**  登录名 */
    private String loginName;

    /** 角色编码 */
    private String roleCode;

    @TableField(exist = false)
    private String createBy;

    @TableField(exist = false)
    private Date createTime;

    @TableField(exist = false)
    private String updateBy;

    @TableField(exist = false)
    private Date updateTime;

    @TableField(exist = false)
    private Integer version;
}
