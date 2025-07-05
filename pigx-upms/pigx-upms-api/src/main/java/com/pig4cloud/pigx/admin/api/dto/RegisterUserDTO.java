package com.pig4cloud.pigx.admin.api.dto;

import lombok.Data;

/**
 * 注册用户 DTO
 *
 * @author lengleng
 * @date 2024/12/23
 */
@Data
public class RegisterUserDTO {

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 新密码1
	 */
	private String newpassword1;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 租户ID
	 */
	private Long tenantId;

}
