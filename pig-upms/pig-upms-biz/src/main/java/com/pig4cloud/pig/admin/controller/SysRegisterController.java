package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.admin.api.dto.RegisterUserDTO;
import com.pig4cloud.pig.admin.service.SysUserService;
import com.pig4cloud.pig.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.data.resolver.ParamResolver;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.Inner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

/**
 * @author lengleng
 * @date 2022/3/30
 * <p>
 * 客户端注册功能 register.user = false
 */
@Inner(value = false)
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class SysRegisterController {

	private final SysUserService userService;

	/**
	 * 注册用户
	 * @param userDto 用户信息
	 * @return success/false
	 */
	@SysLog("注册用户")
	@PostMapping("/user")
	@ConditionalOnProperty(name = "register.user", matchIfMissing = true)
	public R<Boolean> registerUser(@RequestBody RegisterUserDTO userDto) {
		if (isDisabled("SITE_REGISTER_ENABLE")) {
			return R.failed("系统已关闭注册功能");
		}
		return userService.registerUser(userDto);
	}

	/**
	 * 重置用户密码
	 * @param userDto 用户信息
	 * @return success/false
	 */
	@SysLog("重置用户密码")
	@PostMapping("/password")
	public R<Boolean> resetUserPassword(@RequestBody RegisterUserDTO userDto) {
		if (isDisabled("SITE_RESET_PASSWORD")) {
			return R.failed("系统已关闭重置密码功能");
		}
		return userService.resetUserPassword(userDto);
	}

	/**
	 * 找回密码
	 * @param userDto 用户信息
	 * @param code 验证码
	 * @return success/false
	 */
	@SysLog("找回用户密码")
	@PostMapping("/forget/{code}")
	public R<Boolean> forgetUserPassword(@RequestBody RegisterUserDTO userDto, @PathVariable String code) {
		if (isDisabled("SITE_RESET_PASSWORD")) {
			return R.failed("系统已关闭找回密码功能");
		}
		return userService.forgetUserPassword(userDto, code);
	}

	/**
	 * 网站配置开关未开启（参数缺失也视为关闭）
	 * @param key 参数键
	 * @return true 关闭
	 */
	private boolean isDisabled(String key) {
		return !YesNoEnum.YES.getCode().equals(ParamResolver.getStr(key, YesNoEnum.NO.getCode()));
	}

}
