package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.dto.SysAreaSortDTO;
import com.pig4cloud.pig.admin.api.dto.SysAreaUpdateDTO;
import com.pig4cloud.pig.admin.api.entity.SysAreaEntity;
import com.pig4cloud.pig.admin.service.SysAreaService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 行政区划
 *
 * @author lbw
 * @date 2024-02-16 22:40:06
 */
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/sysArea")
@Tag(description = "sysArea", name = "行政区划管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysAreaController {

	private final SysAreaService sysAreaService;

	/**
	 * 分页查询
	 * @param page 分页对象
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
	 * 查询管理端直属子级
	 * @param pid 父级行政编码
	 * @return 直属子级
	 */
	@Operation(summary = "查询管理端直属子级", description = "包含启用和停用的直属子级")
	@GetMapping("/manage/children")
	@HasPermission("sys_sysArea_view")
	public R getManageChildren(@RequestParam(required = false, defaultValue = "0") Long pid) {
		return R.ok(sysAreaService.listManageChildren(pid));
	}

	/**
	 * 搜索管理端行政区划
	 * @param keyword 名称或行政编码
	 * @return 搜索结果
	 */
	@Operation(summary = "搜索管理端行政区划", description = "按名称或行政编码搜索")
	@GetMapping("/manage/search")
	@HasPermission("sys_sysArea_view")
	public R searchManage(@RequestParam @Size(max = 64, message = "搜索关键词长度不能超过64个字符") String keyword) {
		return R.ok(sysAreaService.searchManage(keyword));
	}

	/**
	 * 判断行政编码是否存在
	 * @param adcode 行政编码
	 * @return 是否存在
	 */
	@Operation(summary = "判断行政编码是否存在", description = "新增行政区划时校验行政编码")
	@GetMapping("/exists/adcode")
	@HasPermission("sys_sysArea_add")
	public R existsByAdcode(@RequestParam Long adcode) {
		return R.ok(sysAreaService.existsByAdcode(adcode));
	}

	/**
	 * 获取详细信息
	 * @param id 行政区划ID
	 * @return {@link R }
	 */
	@Operation(summary = "获取详细信息", description = "获取详细信息")
	@GetMapping("/details")
	@HasPermission("sys_sysArea_view")
	public R getDetails(@RequestParam Long id) {
		return R.ok(sysAreaService.getById(id));
	}

	/**
	 * 新增行政区划
	 * @param sysArea 行政区划
	 * @return R
	 */
	@Operation(summary = "新增行政区划", description = "新增行政区划")
	@SysLog("新增行政区划")
	@PostMapping
	@HasPermission("sys_sysArea_add")
	public R save(@RequestBody SysAreaEntity sysArea) {
		return sysAreaService.saveArea(sysArea);
	}

	/**
	 * 修改行政区划
	 * @param sysArea 行政区划
	 * @return R
	 */
	@Operation(summary = "修改行政区划", description = "修改行政区划")
	@SysLog("修改行政区划")
	@PutMapping
	@HasPermission("sys_sysArea_edit")
	public R updateById(@Valid @RequestBody SysAreaUpdateDTO updateDTO) {
		return sysAreaService.updateArea(updateDTO);
	}

	/**
	 * 更新同级行政区划排序
	 * @param sortDTO 排序参数
	 * @return R
	 */
	@Operation(summary = "更新行政区划排序", description = "仅支持同一父级的完整直属子级排序")
	@SysLog("更新行政区划排序")
	@PutMapping("/sort")
	@HasPermission("sys_sysArea_edit")
	public R sort(@Valid @RequestBody SysAreaSortDTO sortDTO) {
		return sysAreaService.updateAreaSort(sortDTO);
	}

	/**
	 * 通过id删除行政区划
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除行政区划", description = "通过id删除行政区划")
	@SysLog("通过id删除行政区划")
	@DeleteMapping
	@HasPermission("sys_sysArea_del")
	public R removeById(@RequestBody Long[] ids) {
		return sysAreaService.removeAreas(CollUtil.toList(ids));
	}

	/**
	 * 导出excel 表格
	 * @param sysArea 查询条件
	 * @param ids 导出指定ID
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("sys_sysArea_export")
	public List<SysAreaEntity> export(SysAreaEntity sysArea, Long[] ids) {
		return sysAreaService.list(Wrappers.lambdaQuery(sysArea)
			.in(ArrayUtil.isNotEmpty(ids), SysAreaEntity::getId, CollUtil.toList(ids)));
	}

}
