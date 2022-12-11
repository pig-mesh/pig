package com.pig4cloud.pigx.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysTenantMenu;
import com.pig4cloud.pigx.admin.service.SysTenantMenuService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant-menu")
@Api(value = "tenant", tags = "租户菜单管理")
public class SysTenantMenuController {

	private final SysTenantMenuService sysTenantMenuService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysTenantMenu 租户菜单
	 * @return
	 */
	@GetMapping("/page")
	public R getSysTenantPage(Page page, SysTenantMenu sysTenantMenu) {
		return R.ok(sysTenantMenuService.page(page, Wrappers.query(sysTenantMenu)));
	}

	/**
	 * 查询租户信息
	 * @param sysTenantMenu 查询条件
	 * @return 租户信息
	 */
	@GetMapping
	public R getByObj(SysTenantMenu sysTenantMenu) {
		return R.ok(sysTenantMenuService.list(Wrappers.query(sysTenantMenu)));
	}

	/**
	 * 新增租户
	 * @param sysTenantMenu 租户
	 * @return R
	 */
	@SysLog("新增租户")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin_systenantmenu_add')")
	@CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
	public R save(@RequestBody SysTenantMenu sysTenantMenu) {
		return R.ok(sysTenantMenuService.saveTenant(sysTenantMenu));
	}

	/**
	 * 修改租户
	 * @param sysTenantMenu 租户
	 * @return R
	 */
	@SysLog("修改租户")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_systenantmenu_edit')")
	@CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
	public R updateById(@RequestBody SysTenantMenu sysTenantMenu) {
		return R.ok(sysTenantMenuService.updateById(sysTenantMenu));
	}

	/**
	 * 通过id删除租户
	 * @param id id
	 * @return R
	 */
	@SysLog("删除租户")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin_systenantmenu_del')")
	@CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
	public R removeById(@PathVariable Long id) {
		return R.ok(sysTenantMenuService.removeById(id));
	}

	/**
	 * 查询全部有效的租户
	 * @return
	 */
	@Inner(value = false)
	@GetMapping("/list")
	public R list() {
		List<SysTenantMenu> tenants = sysTenantMenuService.list(
				Wrappers.<SysTenantMenu>lambdaQuery().eq(SysTenantMenu::getStatus, CommonConstants.STATUS_NORMAL));
		return R.ok(tenants);
	}

}
