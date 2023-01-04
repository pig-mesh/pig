package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.service.ConnectService;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钉钉、微信 互联
 *
 * @author lengleng
 * @date 2022/4/22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/connect")
@Tag(description = "connect", name = "开放互联")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ConnectController {

	private final ConnectService connectService;

	private final SysDeptService deptService;

	/**
	 * 同步钉钉用户
	 * @return
	 */
	@PostMapping("/sync/ding/user")
	@PreAuthorize("@pms.hasPermission('sys_connect_sync')")
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
	@PreAuthorize("@pms.hasPermission('sys_connect_sync')")
	public R syncDept() {
		return R.ok(connectService.syncDingDept());
	}

	/**
	 * 同步企微用户
	 * @return
	 */
	@PostMapping("/sync/cp/user")
	@PreAuthorize("@pms.hasPermission('sys_connect_sync')")
	public R syncCpUser() {
		return connectService.syncCpUser();
	}

	/**
	 * 同步企微部门
	 * @return
	 */
	@PostMapping("/sync/cp/dept")
	@PreAuthorize("@pms.hasPermission('sys_connect_sync')")
	public R syncCpDept() {
		return connectService.syncCpDept();
	}

}
