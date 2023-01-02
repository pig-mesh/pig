package com.anjiplus.template.gaea.business.modules.accessuser.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Schema(description = "修改密码")
@Data
public class UpdatePasswordDto {

	@Schema(description = "旧密码密码")
	@NotBlank
	private String oldPassword;

	@Schema(description = "密码")
	@NotBlank
	private String password;

	@Schema(description = "密码")
	@NotBlank
	private String confirmPassword;

}
