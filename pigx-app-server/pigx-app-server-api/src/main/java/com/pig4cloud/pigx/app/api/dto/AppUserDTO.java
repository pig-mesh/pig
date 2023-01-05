package com.pig4cloud.pigx.app.api.dto;

import com.pig4cloud.pigx.app.api.entity.AppUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@ApiModel(value = "APP用户传输对象")
@EqualsAndHashCode(callSuper = true)
public class AppUserDTO extends AppUser {

	/**
	 * 角色ID
	 */
	@ApiModelProperty(value = "角色id集合")
	private List<Long> role;

	/**
	 * 新密码
	 */
	@ApiModelProperty(value = "新密码")
	private String newpassword1;

}
