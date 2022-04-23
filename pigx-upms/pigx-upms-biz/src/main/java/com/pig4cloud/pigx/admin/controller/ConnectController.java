package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.service.ConnectService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钉钉、微信 互联
 *
 * @author lengleng
 * @date 2022/4/22
 */
@Inner(value = false)
@RestController
@RequiredArgsConstructor
@RequestMapping("/connect")
@Api(value = "connect", tags = "开放互联")
public class ConnectController {

	private final ConnectService connectService;

	/**
	 * 同步钉钉用户
	 * @return
	 */
	@PostMapping("/sync/ding/user/{deptId}")
	public R syncUser(@PathVariable("deptId") Long deptId) {
		return R.ok(connectService.syncDingUser(deptId));
	}

	/**
	 * 同步钉钉角色
	 * @return
	 */
	@PostMapping("/sync/ding/role")
	public R syncRole() {
		return R.ok(connectService.syncDingRole());
	}

	/**
	 * 同步钉钉部门
	 * @return
	 */
	@PostMapping("/sync/ding/dept")
	public R syncDept() {
		return R.ok(connectService.syncDingDept());
	}

	/**
	 * 同步企微用户
	 * @return
	 */
	@PostMapping("/sync/cp/user")
	public R syncCpUser() {
		return R.ok();
	}

	/**
	 * 同步企微角色
	 * @return
	 */
	@PostMapping("/sync/cp/role")
	public R syncCpRole() {
		return R.ok();
	}

	/**
	 * 同步企微部门
	 * @return
	 */
	@PostMapping("/sync/cp/dept")
	public R syncCpDept() {
		return R.ok();
	}

}
