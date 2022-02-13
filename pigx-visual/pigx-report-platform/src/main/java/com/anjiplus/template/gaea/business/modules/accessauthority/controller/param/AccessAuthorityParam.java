/**/
package com.anjiplus.template.gaea.business.modules.accessauthority.controller.param;

import lombok.Data;
import java.io.Serializable;
import com.anji.plus.gaea.annotation.Query;
import com.anji.plus.gaea.constant.QueryEnum;
import com.anji.plus.gaea.curd.params.PageParam;
import java.util.List;

import java.util.Date;

/**
* @desc AccessAuthority 权限管理查询输入类
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:10.009
**/
@Data
public class AccessAuthorityParam extends PageParam implements Serializable{

    /** 父菜单代码 */
    @Query(value = QueryEnum.LIKE)
    private String parentTarget;

    /** 菜单代码 */
    @Query(value = QueryEnum.LIKE)
    private String target;

    /** 菜单名称 */
    @Query(value = QueryEnum.LIKE)
    private String targetName;

    /** 按钮代码 */
    @Query(value = QueryEnum.LIKE)
    private String action;

    /** 按钮名称 */
    @Query(value = QueryEnum.LIKE)
    private String actionName;

    // 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG
    @Query
    private Integer enableFlag;
}