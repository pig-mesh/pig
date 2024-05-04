package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
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
@Inner(value = false)
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final SysUserService userService;

    /**
     * 注册用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @SysLog("注册用户")
    @PostMapping("/user")
    @ConditionalOnProperty(name = "register.user", matchIfMissing = true)
    public R<Boolean> registerUser(@RequestBody UserDTO userDto) {
        return userService.registerUser(userDto);
    }

    /**
     * 重置用户密码
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @SysLog("重置用户密码")
    @PostMapping("/password")
    public R<Boolean> resetUserPassword(@RequestBody UserDTO userDto) {
        return userService.resetUserPassword(userDto);
    }

}
