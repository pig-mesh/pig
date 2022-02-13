package com.anjiplus.template.gaea.business.modules.accessrole.dao.entity;

import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.util.Date;

/**
 * @description 角色--菜单--按钮关联关系 entity
 * @author 木子李·De <lide1202@hotmail.com>
 * @date 2019-02-17 08:50:14.136
 **/
@TableName(keepGlobalPrefix=true, value="access_role_authority")
@Data
public class AccessRoleAuthority extends GaeaBaseEntity {

    /** 角色编码 */
    private String roleCode;

    /** 菜单代码 */
    private String target;

    /** 按钮代码 */
    private String action;

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
