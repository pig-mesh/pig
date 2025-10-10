package com.pig4cloud.pigx.flow.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.RequestExcel;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.flow.entity.BpmOaLeaveEntity;
import com.pig4cloud.pigx.flow.service.BpmOaLeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 请假表单
 *
 * @author pigx
 * @date 2025-08-08 09:32:41
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bpmOaLeave")
@Tag(description = "bpmOaLeave", name = "请假表单管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class BpmOaLeaveController {

	private final BpmOaLeaveService bpmOaLeaveService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param bpmOaLeave 请假表单
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("flow_bpmOaLeave_view")
	public R getBpmOaLeavePage(@ParameterObject Page page, @ParameterObject BpmOaLeaveEntity bpmOaLeave) {
		return R.ok(bpmOaLeaveService.page(page, Wrappers.<BpmOaLeaveEntity>lambdaQuery()
			.like(StrUtil.isNotBlank(bpmOaLeave.getUsername()), BpmOaLeaveEntity::getUsername, bpmOaLeave.getUsername())
			.eq(Objects.nonNull(bpmOaLeave.getLeaveStatus()), BpmOaLeaveEntity::getLeaveStatus,
					bpmOaLeave.getLeaveStatus())
			.eq(Objects.nonNull(bpmOaLeave.getLeaveType()), BpmOaLeaveEntity::getLeaveType,
					bpmOaLeave.getLeaveType())));
	}

	/**
	 * 通过条件查询请假表单
	 * @param bpmOaLeave 查询条件
	 * @return R 对象列表
	 */
	@Operation(summary = "通过条件查询", description = "通过条件查询对象")
	@GetMapping("/details")
	@HasPermission("flow_bpmOaLeave_view")
	public R getDetails(@ParameterObject BpmOaLeaveEntity bpmOaLeave) {
		return R.ok(bpmOaLeaveService.list(Wrappers.query(bpmOaLeave)));
	}

	/**
	 * 新增请假表单
	 * @param bpmOaLeave 请假表单
	 * @return R
	 */
	@Operation(summary = "新增请假表单", description = "新增请假表单")
	@SysLog("新增请假表单")
	@PostMapping
	@HasPermission("flow_bpmOaLeave_add")
	public R save(@RequestBody BpmOaLeaveEntity bpmOaLeave) {
		return R.ok(bpmOaLeaveService.saveAndStartProcess(bpmOaLeave));
	}

	/**
	 * 修改请假表单
	 * @param bpmOaLeave 请假表单
	 * @return R
	 */
	@Operation(summary = "修改请假表单", description = "修改请假表单")
	@SysLog("修改请假表单")
	@PutMapping
	@HasPermission("flow_bpmOaLeave_edit")
	public R updateById(@RequestBody BpmOaLeaveEntity bpmOaLeave) {
		return R.ok(bpmOaLeaveService.updateById(bpmOaLeave));
	}

	/**
	 * 通过id删除请假表单
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除请假表单", description = "通过id删除请假表单")
	@SysLog("通过id删除请假表单")
	@DeleteMapping
	@HasPermission("flow_bpmOaLeave_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(bpmOaLeaveService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 * @param bpmOaLeave 查询条件
	 * @param ids 导出指定ID
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("flow_bpmOaLeave_export")
	public List<BpmOaLeaveEntity> exportExcel(BpmOaLeaveEntity bpmOaLeave, Long[] ids) {
		return bpmOaLeaveService
			.list(Wrappers.lambdaQuery(bpmOaLeave).in(ArrayUtil.isNotEmpty(ids), BpmOaLeaveEntity::getId, ids));
	}

	/**
	 * 导入excel 表
	 * @param bpmOaLeaveList 对象实体列表
	 * @param bindingResult 错误信息列表
	 * @return ok fail
	 */
	@PostMapping("/import")
	@HasPermission("flow_bpmOaLeave_export")
	public R importExcel(@RequestExcel List<BpmOaLeaveEntity> bpmOaLeaveList, BindingResult bindingResult) {
		return R.ok(bpmOaLeaveService.saveBatch(bpmOaLeaveList));
	}

}
