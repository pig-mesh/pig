package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysAreaEntity;
import com.pig4cloud.pigx.admin.service.SysAreaService;
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
 * 行政区划
 *
 * @author lbw
 * @date 2024-02-16 22:40:06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sysArea")
@Tag(description = "sysArea", name = "行政区划管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysAreaController {

    private final SysAreaService sysAreaService;

    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param sysArea 行政区划
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page")
    @HasPermission("sys_sysArea_view")
    public R getSysAreaPage(@ParameterObject Page page, @ParameterObject SysAreaEntity sysArea) {
        return R.ok(sysAreaService.selectPage(page, sysArea));
    }

    /**
     * 前端联动组件需要数据
     *
     * @param sysArea 查询条件
     * @return tree
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/tree")
    @HasPermission("sys_sysArea_view")
    public R getSysAreaTree(@ParameterObject SysAreaEntity sysArea) {
        return R.ok(sysAreaService.selectTree(sysArea));
    }

    /**
     * 获取详细信息
     *
     * @param sysArea 查询条件
     * @return {@link R }
     */
    @Operation(summary = "获取详细信息", description = "获取详细信息")
    @GetMapping("/details")
    @HasPermission("sys_sysArea_view")
    public R getDetails(@ParameterObject SysAreaEntity sysArea) {
        return R.ok(sysAreaService.getOne(Wrappers.query(sysArea)));
    }

    /**
     * 新增行政区划
     *
     * @param sysArea 行政区划
     * @return R
     */
    @Operation(summary = "新增行政区划", description = "新增行政区划")
    @SysLog("新增行政区划")
    @PostMapping
    @HasPermission("sys_sysArea_add")
    public R save(@RequestBody SysAreaEntity sysArea) {
        return R.ok(sysAreaService.save(sysArea));
    }

    /**
     * 修改行政区划
     *
     * @param sysArea 行政区划
     * @return R
     */
    @Operation(summary = "修改行政区划", description = "修改行政区划")
    @SysLog("修改行政区划")
    @PutMapping
    @HasPermission("sys_sysArea_edit")
    public R updateById(@RequestBody SysAreaEntity sysArea) {
        return R.ok(sysAreaService.updateById(sysArea));
    }

    /**
     * 通过id删除行政区划
     *
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除行政区划", description = "通过id删除行政区划")
    @SysLog("通过id删除行政区划")
    @DeleteMapping
    @HasPermission("sys_sysArea_del")
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysAreaService.removeBatchByIds(CollUtil.toList(ids)));
    }

    /**
     * 导出excel 表格
     *
     * @param sysArea 查询条件
     * @param ids     导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @HasPermission("sys_sysArea_export")
    public List<SysAreaEntity> export(SysAreaEntity sysArea, Long[] ids) {
        return sysAreaService
                .list(Wrappers.lambdaQuery(sysArea).in(ArrayUtil.isNotEmpty(ids), SysAreaEntity::getId, ids));
    }

}
