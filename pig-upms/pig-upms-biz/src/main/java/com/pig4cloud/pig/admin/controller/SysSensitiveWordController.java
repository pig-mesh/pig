package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysSensitiveWordEntity;
import com.pig4cloud.pig.admin.service.SysSensitiveWordService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 敏感词
 *
 * @author pig
 * @date 2024-07-06 15:54:43
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sysSensitiveWord")
@Tag(description = "sysSensitiveWord", name = "敏感词管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysSensitiveWordController {

    private final SysSensitiveWordService sysSensitiveWordService;

    /**
     * 分页查询
     *
     * @param page             分页对象
     * @param sysSensitiveWord 敏感词
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page")
    @HasPermission("admin_sysSensitiveWord_view")
    public R getSysSensitiveWordPage(@ParameterObject Page page,
                                     @ParameterObject SysSensitiveWordEntity sysSensitiveWord) {
        LambdaQueryWrapper<SysSensitiveWordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(sysSensitiveWord.getSensitiveWord()), SysSensitiveWordEntity::getSensitiveWord,
                sysSensitiveWord.getSensitiveWord());
        wrapper.eq(StrUtil.isNotBlank(sysSensitiveWord.getSensitiveType()), SysSensitiveWordEntity::getSensitiveType,
                sysSensitiveWord.getSensitiveType());
        return R.ok(sysSensitiveWordService.page(page, wrapper));
    }

    @Inner
    @Operation(summary = "查询所有敏感词", description = "查询所有敏感词")
    @GetMapping("/remote/list/{type}")
    public R list(@PathVariable String type) {
        return R.ok(sysSensitiveWordService
                .list(Wrappers.<SysSensitiveWordEntity>lambdaQuery().eq(SysSensitiveWordEntity::getSensitiveType, type))
                .stream()
                .map(SysSensitiveWordEntity::getSensitiveWord)
                .toList());
    }

    /**
     * 通过id查询敏感词
     *
     * @return R
     */
    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/details")
    @HasPermission("admin_sysSensitiveWord_view")
    public R getById(@ParameterObject SysSensitiveWordEntity sysSensitiveWord) {
        return R.ok(sysSensitiveWordService.getOne(Wrappers.lambdaQuery(sysSensitiveWord), false));
    }

    /**
     * 新增敏感词
     *
     * @param sysSensitiveWord 敏感词
     * @return R
     */
    @Operation(summary = "新增敏感词", description = "新增敏感词")
    @SysLog("新增敏感词")
    @PostMapping
    @HasPermission("admin_sysSensitiveWord_add")
    public R save(@RequestBody SysSensitiveWordEntity sysSensitiveWord) {
        return R.ok(sysSensitiveWordService.saveSensitive(sysSensitiveWord));
    }

    /**
     * 修改敏感词
     *
     * @param sysSensitiveWord 敏感词
     * @return R
     */
    @Operation(summary = "修改敏感词", description = "修改敏感词")
    @SysLog("修改敏感词")
    @PutMapping
    @HasPermission("admin_sysSensitiveWord_edit")
    public R updateById(@RequestBody SysSensitiveWordEntity sysSensitiveWord) {
        sysSensitiveWordService.updateById(sysSensitiveWord);
        RedisUtils.execute((RedisCallback<Void>) connection -> {
            connection.publish(CacheConstants.SENSITIVE_REDIS_RELOAD_TOPIC.getBytes(), "刷新敏感词缓存".getBytes());
            return null;
        });
        return R.ok();
    }

    /**
     * 查询敏感词
     *
     * @param sysSensitiveWord 敏感词
     * @return R
     */
    @Operation(summary = "查询敏感词", description = "查询敏感词")
    @SysLog("查询敏感词")
    @PostMapping("/match")
    @HasPermission("admin_sysSensitiveWord_del")
    public R match(@RequestBody SysSensitiveWordEntity sysSensitiveWord) {
        return R.ok(sysSensitiveWordService.matchSensitiveWord(sysSensitiveWord));
    }

    /**
     * 通过id删除敏感词
     *
     * @param ids sensitiveId列表
     * @return R
     */
    @Operation(summary = "通过id删除敏感词", description = "通过id删除敏感词")
    @SysLog("通过id删除敏感词")
    @DeleteMapping
    @HasPermission("admin_sysSensitiveWord_del")
    public R removeById(@RequestBody Long[] ids) {
        sysSensitiveWordService.removeBatchByIds(CollUtil.toList(ids));
        RedisUtils.execute((RedisCallback<Void>) connection -> {
            connection.publish(CacheConstants.SENSITIVE_REDIS_RELOAD_TOPIC.getBytes(), "刷新敏感词缓存".getBytes());
            return null;
        });
        return R.ok();
    }

    /**
     * 导出excel 表格
     *
     * @param sysSensitiveWord 查询条件
     * @param ids              导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @HasPermission("admin_sysSensitiveWord_export")
    public List<SysSensitiveWordEntity> export(SysSensitiveWordEntity sysSensitiveWord, Long[] ids) {
        return sysSensitiveWordService.list(Wrappers.lambdaQuery(sysSensitiveWord)
                .in(ArrayUtil.isNotEmpty(ids), SysSensitiveWordEntity::getSensitiveId, CollUtil.toList(ids)));
    }

    /**
     * 刷新敏感词缓存
     *
     * @return R
     */
    @Operation(summary = "刷新敏感词缓存", description = "刷新敏感词缓存")
    @GetMapping("/refresh")
    @HasPermission("admin_sysSensitiveWord_del")
    public R refresh() {
        RedisUtils.execute((RedisCallback<Void>) connection -> {
			connection.publish(CacheConstants.SENSITIVE_REDIS_RELOAD_TOPIC.getBytes(), "刷新敏感词缓存".getBytes());
			return null;
		});
		return R.ok();
	}

}
