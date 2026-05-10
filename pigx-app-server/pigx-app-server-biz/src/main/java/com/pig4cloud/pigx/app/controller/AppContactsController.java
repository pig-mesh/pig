package com.pig4cloud.pigx.app.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.app.api.entity.AppContactsEntity;
import com.pig4cloud.pigx.app.service.AppContactsService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通讯录表管理控制器
 *
 * @author lengleng
 * @date 2025/05/29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/appContacts")
@Tag(description = "appContacts", name = "通讯录表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppContactsController {

    private final AppContactsService appContactsService;

    /**
     * 分页查询
     *
     * @param page        分页对象
     * @param appContacts 通讯录表
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page")
    @HasPermission("app_appContacts_view")
    public R getAppContactsPage(@ParameterObject Page page, @ParameterObject AppContactsEntity appContacts) {
        return R.ok(appContactsService.pageScope(page, appContacts));
    }


    /**
     * 通过id查询通讯录表
     *
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/{id}")
    @HasPermission("app_appContacts_view")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(appContactsService.getById(id));
    }

    /**
     * 新增通讯录表
     *
     * @param appContacts 通讯录表
     * @return R
     */
    @Operation(summary = "新增通讯录表", description = "新增通讯录表")
    @SysLog("新增通讯录表")
    @PostMapping
    @HasPermission("app_appContacts_add")
    public R save(@RequestBody AppContactsEntity appContacts) {
        return R.ok(appContactsService.save(appContacts));
    }

    /**
     * 修改通讯录表
     *
     * @param appContacts 通讯录表
     * @return R
     */
    @Operation(summary = "修改通讯录表", description = "修改通讯录表")
    @SysLog("修改通讯录表")
    @PutMapping
    @HasPermission("app_appContacts_edit")
    public R updateById(@RequestBody AppContactsEntity appContacts) {
        return R.ok(appContactsService.updateById(appContacts));
    }

    /**
     * 通过id删除通讯录表
     *
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除通讯录表", description = "通过id删除通讯录表")
    @SysLog("通过id删除通讯录表")
    @DeleteMapping
    @HasPermission("app_appContacts_del")
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(appContactsService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     *
     * @param appContacts 查询条件
     * @param ids         导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @HasPermission("app_appContacts_export")
    public List<AppContactsEntity> export(AppContactsEntity appContacts, Long[] ids) {
        return appContactsService.list(Wrappers.lambdaQuery(appContacts).in(ArrayUtil.isNotEmpty(ids), AppContactsEntity::getId, ids));
    }
}