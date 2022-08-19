package com.anjiplus.template.gaea.business.modules.accessuser.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel(value = "用户登录")
@Data
public class GaeaUserDto {

    @ApiModelProperty(value = "登录名")
    @NotBlank
    private String loginName;

    @ApiModelProperty(value = "密码")
    @NotBlank
    private String password;

    /** 真实用户 */
    private String realName;

    /** 登录成功后的 */
    private String token;

    /** 用户所拥有的权限合集 */
    private List<String> authorities;
}
