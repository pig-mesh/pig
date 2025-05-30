package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.admin.api.dto.RegisterUserDTO;
import com.pig4cloud.pig.admin.service.SysUserService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.Inner;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lengleng
 * @date 2022/3/30
 * <p>
 * 客户端注册功能 register.user = false
 */
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@Tag(description = "register", name = "注册用户管理模块")
@ConditionalOnProperty(name = "register.user", matchIfMissing = true)
public class SysRegisterController {

	private final SysUserService userService;

	/**
	 * 注册用户
	 * @param registerUserDTO 注册用户 DTO
	 * @return {@link R }<{@link Boolean }>
	 */
	@Inner(value = false)
	@SysLog("注册用户")
	@PostMapping("/user")
	public R<Boolean> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
		return userService.registerUser(registerUserDTO);
	}

}
