
package com.anjiplus.template.gaea.business.modules.accessauthority.controller.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.anji.plus.gaea.annotation.Query;
import com.anji.plus.gaea.constant.QueryEnum;
import com.anji.plus.gaea.curd.dto.GaeaBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import com.anji.plus.gaea.annotation.Formatter;
import lombok.Data;

/**
*
* @description 权限管理 dto
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:10.009
**/
@Data
public class AccessAuthorityDto extends GaeaBaseDTO implements Serializable {
    /** 父菜单代码 */
    @ApiModelProperty(value = "父菜单代码")
    private String parentTarget;

    /** 目标菜单 */
    @ApiModelProperty(value = "菜单代码")
    @NotEmpty(message = "6002")
    private String target;

    /** 目标菜单名称 */
    @ApiModelProperty(value = "菜单名称")
    @NotEmpty(message = "6002")
    private String targetName;

    /** 目标按钮 */
    @ApiModelProperty(value = "按钮代码")
    @NotEmpty(message = "6002")
    private String action;

    /** 目标按钮名称 */
    @ApiModelProperty(value = "按钮名称")
    @NotEmpty(message = "6002")
    private String actionName;

    /**  0--未删除 1--已删除 DIC_NAME=DEL_FLAG */
    @ApiModelProperty(value = " 0--未删除 1--已删除 DIC_NAME=DEL_FLAG")
    private Integer deleteFlag;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
    @ApiModelProperty(value = "0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG")
    @NotNull(message = "6002")
    private Integer enableFlag;

    @ApiModelProperty(value = "sort")
    private Integer sort;
}