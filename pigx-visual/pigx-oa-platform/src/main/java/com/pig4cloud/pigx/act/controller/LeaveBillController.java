/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.act.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.act.entity.LeaveBill;
import com.pig4cloud.pigx.act.service.LeaveBillService;
import com.pig4cloud.pigx.act.service.ProcessService;
import com.pig4cloud.pigx.common.core.constant.enums.TaskStatusEnum;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.common.xss.core.XssCleanIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 请假流程
 *
 * @author 冷冷
 * @date 2018-09-27 15:20:44
 */
@RestController
@RequestMapping("/leave-bill")
@RequiredArgsConstructor
@Tag(description = "leave-bill", name = "leave-bill表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class LeaveBillController {

	private final LeaveBillService leaveBillService;

	private final ProcessService processService;

	/**
	 * 请假审批单简单分页查询
	 * @param page 分页对象
	 * @param leaveBill 请假审批单
	 * @return
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询", description = "分页查询")
	@PreAuthorize("@pms.hasPermission('oa_leave_bill_view')")
	public R getLeaveBillPage(Page page, LeaveBill leaveBill) {
		LambdaQueryWrapper<LeaveBill> wrapper = Wrappers.<LeaveBill>lambdaQuery()
				.like(StrUtil.isNotBlank(leaveBill.getUsername()), LeaveBill::getUsername, leaveBill.getUsername())
				.eq(StrUtil.isNotBlank(leaveBill.getState()), LeaveBill::getState, leaveBill.getState());
		return R.ok(leaveBillService.page(page, wrapper));
	}

	/**
	 * 信息
	 * @param leaveId
	 * @return R
	 */
	@GetMapping("/{leaveId}")
	@Operation(summary = "分页查询", description = "分页查询")
	@PreAuthorize("@pms.hasPermission('oa_leave_bill_view')")
	public R getById(@PathVariable("leaveId") Long leaveId) {
		return R.ok(leaveBillService.getById(leaveId));
	}

	/**
	 * 保存
	 * @param leaveBill
	 * @return R
	 */
	@PostMapping
	@XssCleanIgnore
	@Operation(summary = "保存", description = "保存")
	@PreAuthorize("@pms.hasPermission('oa_leave_bill_add')")
	public R save(@RequestBody LeaveBill leaveBill) {
		leaveBill.setUsername(SecurityUtils.getUser().getUsername());
		leaveBill.setState(TaskStatusEnum.UNSUBMIT.getStatus());
		return R.ok(leaveBillService.save(leaveBill));
	}

	/**
	 * 修改
	 * @param leaveBill
	 * @return R
	 */
	@PutMapping
	@XssCleanIgnore
	@Operation(summary = "修改", description = "修改")
	@PreAuthorize("@pms.hasPermission('oa_leave_bill_edit')")
	public R updateById(@RequestBody LeaveBill leaveBill) {
		return R.ok(leaveBillService.updateById(leaveBill));
	}

	/**
	 * 删除
	 * @param ids
	 * @return R
	 */
	@DeleteMapping
	@Operation(summary = "删除请假工单", description = "删除请假工单")
	@PreAuthorize("@pms.hasPermission('oa_leave_bill_del')")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(leaveBillService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 提交请假流程
	 * @param leaveId
	 * @return R
	 */
	@GetMapping("/submit/{leaveId}")
	@Operation(summary = "提交请假流程", description = "提交请假流程")
	@PreAuthorize("@pms.hasPermission('oa_leave_bill_view')")
	public R submit(@PathVariable("leaveId") Long leaveId) {
		return R.ok(processService.saveStartProcess(leaveId));
	}

	/**
	 * 导出
	 * @param leaveBill
	 * @return R
	 */
	@ResponseExcel
	@GetMapping("/export")
	@Operation(summary = "导出", description = "导出")
	@PreAuthorize("@pms.hasPermission('oa_leave_bill_export')")
	public List<LeaveBill> export(LeaveBill leaveBill) {
		return leaveBillService.list(Wrappers.query(leaveBill));
	}

}
