package com.anjiplus.template.gaea.business.modules.accessuser.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "修改密码")
@Data
public class UpdatePasswordDto {

    @ApiModelProperty(value = "旧密码密码")
    @NotBlank
    private String oldPassword;

    @ApiModelProperty(value = "密码")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "密码")
    @NotBlank
    private String confirmPassword;
}
