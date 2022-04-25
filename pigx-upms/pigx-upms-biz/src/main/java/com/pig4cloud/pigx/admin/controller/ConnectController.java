package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.service.ConnectService;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
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

	private final SysDeptService deptService;

	/**
	 * 同步钉钉用户
	 * @return
	 */
	@PostMapping("/sync/ding/user")
	public R syncUser() {
		for (SysDept sysDept : deptService.list()) {
			connectService.syncDingUser(sysDept.getDeptId());
		}
		return R.ok();
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
		return connectService.syncCpUser();
	}

	/**
	 * 同步企微部门
	 * @return
	 */
	@PostMapping("/sync/cp/dept")
	public R syncCpDept() {
		return connectService.syncCpDept();
	}

}
