package com.pig4cloud.pigx.app.api.dto;

import com.pig4cloud.pigx.app.api.entity.AppUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Schema(description = "APP用户传输对象")
@EqualsAndHashCode(callSuper = true)
public class AppUserDTO extends AppUser {

	/**
	 * 角色ID
	 */
	@Schema(description = "角色id集合")
	private List<Long> role;

	/**
	 * 新密码
	 */
	@Schema(description = "新密码")
	private String newpassword1;

	@Schema(description = "验证码")
	private String mobileCode;

}
