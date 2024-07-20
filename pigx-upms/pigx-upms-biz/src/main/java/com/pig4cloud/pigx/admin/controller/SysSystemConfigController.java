package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysSystemConfigEntity;
import com.pig4cloud.pigx.admin.service.SysSystemConfigService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置
 *
 * @author pig
 * @date 2024-07-14 20:58:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system-config")
@Tag(description = "sysSystemConfig", name = "系统配置管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysSystemConfigController {

    private final SysSystemConfigService sysSystemConfigService;

    /**
     * 分页查询
     *
     * @param page            分页对象
     * @param sysSystemConfig 系统配置
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page")
    @HasPermission("sys_message_view")
    public R getSysSystemConfigPage(@ParameterObject Page page, @ParameterObject SysSystemConfigEntity sysSystemConfig) {
        return sysSystemConfigService.pageSystemConfig(page, sysSystemConfig);
    }


    /**
     * 通过条件查询系统配置
     *
     * @param sysSystemConfig 查询条件
     * @return R  对象列表
     */
    @Operation(summary = "通过条件查询", description = "通过条件查询对象")
    @GetMapping("/details")
    @HasPermission("sys_message_view")
    public R getDetails(@ParameterObject SysSystemConfigEntity sysSystemConfig) {
        return sysSystemConfigService.listSystemConfig(sysSystemConfig);
    }

    /**
     * 新增系统配置
     *
     * @param sysSystemConfig 系统配置
     * @return R
     */
    @Operation(summary = "新增系统配置", description = "新增系统配置")
    @SysLog("新增系统配置")
    @PostMapping
    @HasPermission("sys_message_add")
    public R save(@RequestBody SysSystemConfigEntity sysSystemConfig) {
        return R.ok(sysSystemConfigService.save(sysSystemConfig));
    }

    /**
     * 修改系统配置
     *
     * @param sysSystemConfig 系统配置
     * @return R
     */
    @Operation(summary = "修改系统配置", description = "修改系统配置")
    @SysLog("修改系统配置")
    @PutMapping
    @HasPermission("sys_message_edit")
    public R updateById(@RequestBody SysSystemConfigEntity sysSystemConfig) {
        return sysSystemConfigService.updateSystemConfig(sysSystemConfig);
    }

    /**
     * 通过id删除系统配置
     *
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除系统配置", description = "通过id删除系统配置")
    @SysLog("通过id删除系统配置")
    @DeleteMapping
    @HasPermission("sys_message_del")
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysSystemConfigService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     *
     * @param sysSystemConfig 查询条件
     * @param ids             导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @HasPermission("sys_message_export")
    public List<SysSystemConfigEntity> export(SysSystemConfigEntity sysSystemConfig, Long[] ids) {
        return sysSystemConfigService.list(Wrappers.lambdaQuery(sysSystemConfig).in(ArrayUtil.isNotEmpty(ids), SysSystemConfigEntity::getId, ids));
    }
}
