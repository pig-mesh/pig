package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.admin.api.dto.SiteConfigDTO;
import com.pig4cloud.pig.admin.service.SysSiteConfigService;
import com.pig4cloud.pig.admin.service.SysSystemInfoService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
@Tag(description = "system", name = "系统监控")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysSystemInfoController {

	private final SysSystemInfoService sysSystemInfoService;

	private final SysSiteConfigService sysSiteConfigService;

	/**
	 * 缓存监控
	 * @return R<Object>
	 */
	@HasPermission("sys_cache_view")
	@GetMapping("/cache")
	public R<Map<String, Object>> cache() {
		return sysSystemInfoService.getCacheInfo();
	}

	/**
	 * Clarity 站点监控数据
	 * @return R<?>
	 */
	@HasPermission("sys_clarity_view")
	@GetMapping("/clarity")
	public R<?> clarity(@RequestParam(defaultValue = "1") Integer numOfDays) {
		return sysSystemInfoService.getClarityData(numOfDays);
	}

	/**
	 * 聚合配置（i18n + site 配置）- 公开接口
	 */
	@Inner(false)
	@GetMapping("/config")
	public R<Map<String, Object>> config() {
		return R.ok(sysSiteConfigService.getAggregatedConfig());
	}

	/**
	 * 获取网站配置（管理端用）
	 */
	@HasPermission("sys_site_config_view")
	@GetMapping("/site-config")
	public R<SiteConfigDTO> getSiteConfig() {
		return R.ok(sysSiteConfigService.getSiteConfig());
	}

	/**
	 * 更新网站配置
	 */
	@HasPermission("sys_site_config_edit")
	@PutMapping("/site-config")
	public R<Void> updateSiteConfig(@RequestBody SiteConfigDTO dto) {
		sysSiteConfigService.updateSiteConfig(dto);
		return R.ok();
	}

	/**
	 * 刷新网站配置缓存（清空 Redis）
	 */
	@HasPermission("sys_site_config_edit")
	@DeleteMapping("/site-config/refresh")
	public R<Void> refreshSiteConfig() {
		sysSiteConfigService.refreshCache();
		return R.ok();
	}

}
