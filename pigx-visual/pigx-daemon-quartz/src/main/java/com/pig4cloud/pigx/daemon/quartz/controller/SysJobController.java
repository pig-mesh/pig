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

package com.pig4cloud.pigx.daemon.quartz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.daemon.quartz.entity.SysJob;
import com.pig4cloud.pigx.daemon.quartz.entity.SysJobLog;
import com.pig4cloud.pigx.daemon.quartz.service.SysJobLogService;
import com.pig4cloud.pigx.daemon.quartz.service.SysJobService;
import com.pig4cloud.pigx.daemon.quartz.util.TaskUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.quartz.Scheduler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.pig4cloud.pigx.daemon.quartz.constants.PigxQuartzEnum.*;

/**
 * @author frwcloud
 * @date 2019-01-27 10:04:42
 * <p>
 * 定时任务管理
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-job")
@Api(value = "sys-job", tags = "定时任务")
public class SysJobController {

	private final SysJobService sysJobService;

	private final SysJobLogService sysJobLogService;

	private final TaskUtil taskUtil;

	private final Scheduler scheduler;

	/**
	 * 定时任务分页查询
	 * @param page 分页对象
	 * @param sysJob 定时任务调度表
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation(value = "分页定时业务查询")
	public R getSysJobPage(Page page, SysJob sysJob) {
		return R.ok(sysJobService.page(page, Wrappers.query(sysJob)));
	}

	/**
	 * 通过id查询定时任务
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "唯一标识查询定时任务")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(sysJobService.getById(id));
	}

	/**
	 * 新增定时任务
	 * @param sysJob 定时任务调度表
	 * @return R
	 */
	@SysLog("新增定时任务")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('job_sys_job_add')")
	@ApiOperation(value = "新增定时任务")
	public R save(@RequestBody SysJob sysJob) {
		sysJob.setJobStatus(JOB_STATUS_RELEASE.getType());
		sysJob.setCreateBy(SecurityUtils.getUser().getUsername());
		return R.ok(sysJobService.save(sysJob));
	}

	/**
	 * 修改定时任务
	 * @param sysJob 定时任务调度表
	 * @return R
	 */
	@SysLog("修改定时任务")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('job_sys_job_edit')")
	@ApiOperation(value = "修改定时任务")
	public R updateById(@RequestBody SysJob sysJob) {
		sysJob.setUpdateBy(SecurityUtils.getUser().getUsername());
		SysJob querySysJob = this.sysJobService.getById(sysJob.getJobId());
		if (JOB_STATUS_NOT_RUNNING.getType().equals(querySysJob.getJobStatus())) {
			this.taskUtil.addOrUpateJob(sysJob, scheduler);
			sysJobService.updateById(sysJob);
		}
		else if (JOB_STATUS_RELEASE.getType().equals(querySysJob.getJobStatus())) {
			sysJobService.updateById(sysJob);
		}
		return R.ok();
	}

	/**
	 * 通过id删除定时任务
	 * @param id id
	 * @return R
	 */
	@SysLog("删除定时任务")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('job_sys_job_del')")
	@ApiOperation(value = "唯一标识查询定时任务，暂停任务才能删除")
	public R removeById(@PathVariable Integer id) {
		SysJob querySysJob = this.sysJobService.getById(id);
		if (JOB_STATUS_NOT_RUNNING.getType().equals(querySysJob.getJobStatus())) {
			this.taskUtil.removeJob(querySysJob, scheduler);
			this.sysJobService.removeById(id);
		}
		else if (JOB_STATUS_RELEASE.getType().equals(querySysJob.getJobStatus())) {
			this.sysJobService.removeById(id);
		}
		return R.ok();
	}

	/**
	 * 暂停全部定时任务
	 * @return
	 */
	@SysLog("暂停全部定时任务")
	@PostMapping("/shutdown-jobs")
	@PreAuthorize("@pms.hasPermission('job_sys_job_shutdown_job')")
	@ApiOperation(value = "暂停全部定时任务")
	public R shutdownJobs() {
		taskUtil.pauseJobs(scheduler);
		long count = this.sysJobService
				.count(new LambdaQueryWrapper<SysJob>().eq(SysJob::getJobStatus, JOB_STATUS_RUNNING.getType()));
		if (count <= 0) {
			return R.ok("无正在运行定时任务");
		}
		else {
			// 更新定时任务状态条件，运行状态2更新为暂停状态2
			this.sysJobService.update(SysJob.builder().jobStatus(JOB_STATUS_NOT_RUNNING.getType()).build(),
					new UpdateWrapper<SysJob>().lambda().eq(SysJob::getJobStatus, JOB_STATUS_RUNNING.getType()));
			return R.ok("暂停成功");
		}
	}

	/**
	 * 启动全部定时任务
	 * @return
	 */
	@SysLog("启动全部定时任务")
	@PostMapping("/start-jobs")
	@PreAuthorize("@pms.hasPermission('job_sys_job_start_job')")
	@ApiOperation(value = "启动全部定时任务")
	public R startJobs() {
		// 更新定时任务状态条件，暂停状态3更新为运行状态2
		this.sysJobService.update(SysJob.builder().jobStatus(JOB_STATUS_RUNNING.getType()).build(),
				new UpdateWrapper<SysJob>().lambda().eq(SysJob::getJobStatus, JOB_STATUS_NOT_RUNNING.getType()));
		taskUtil.startJobs(scheduler);
		return R.ok();
	}

	/**
	 * 刷新全部定时任务
	 * @return
	 */
	@SysLog("刷新全部定时任务")
	@PostMapping("/refresh-jobs")
	@PreAuthorize("@pms.hasPermission('job_sys_job_refresh_job')")
	@ApiOperation(value = "刷新全部定时任务")
	public R refreshJobs() {
		sysJobService.list().forEach((sysjob) -> {
			if (JOB_STATUS_RELEASE.getType().equals(sysjob.getJobStatus())
					|| JOB_STATUS_DEL.getType().equals(sysjob.getJobStatus())) {
				taskUtil.removeJob(sysjob, scheduler);
			}
			else if (JOB_STATUS_RUNNING.getType().equals(sysjob.getJobStatus())
					|| JOB_STATUS_NOT_RUNNING.getType().equals(sysjob.getJobStatus())) {
				taskUtil.addOrUpateJob(sysjob, scheduler);
			}
			else {
				taskUtil.removeJob(sysjob, scheduler);
			}
		});
		return R.ok();
	}

	/**
	 * 启动定时任务
	 * @param jobId
	 * @return
	 */
	@SysLog("启动定时任务")
	@PostMapping("/start-job/{id}")
	@PreAuthorize("@pms.hasPermission('job_sys_job_start_job')")
	@ApiOperation(value = "启动定时任务")
	public R startJob(@PathVariable("id") Long jobId) {
		SysJob querySysJob = this.sysJobService.getById(jobId);
		if (querySysJob != null && JOB_LOG_STATUS_FAIL.getType().equals(querySysJob.getJobStatus())) {
			taskUtil.addOrUpateJob(querySysJob, scheduler);
		}
		else {
			taskUtil.resumeJob(querySysJob, scheduler);
		}
		// 更新定时任务状态条件，暂停状态3更新为运行状态2
		this.sysJobService.updateById(SysJob.builder().jobId(jobId).jobStatus(JOB_STATUS_RUNNING.getType()).build());
		return R.ok();
	}

	/**
	 * 启动定时任务
	 * @param jobId
	 * @return
	 */
	@SysLog("立刻执行定时任务")
	@PostMapping("/run-job/{id}")
	@PreAuthorize("@pms.hasPermission('job_sys_job_run_job')")
	@ApiOperation(value = "立刻执行定时任务")
	public R runJob(@PathVariable("id") Integer jobId) {
		SysJob querySysJob = this.sysJobService.getById(jobId);
		return TaskUtil.runOnce(scheduler, querySysJob) ? R.ok() : R.failed();
	}

	/**
	 * 暂停定时任务
	 * @return
	 */
	@SysLog("暂停定时任务")
	@PostMapping("/shutdown-job/{id}")
	@PreAuthorize("@pms.hasPermission('job_sys_job_shutdown_job')")
	@ApiOperation(value = "暂停定时任务")
	public R shutdownJob(@PathVariable("id") Integer id) {
		SysJob querySysJob = this.sysJobService.getById(id);
		// 更新定时任务状态条件，运行状态2更新为暂停状态3
		this.sysJobService.updateById(
				SysJob.builder().jobId(querySysJob.getJobId()).jobStatus(JOB_STATUS_NOT_RUNNING.getType()).build());
		taskUtil.pauseJob(querySysJob, scheduler);
		return R.ok();
	}

	/**
	 * 唯一标识查询定时执行日志
	 * @return
	 */
	@GetMapping("/job-log")
	@ApiOperation(value = "唯一标识查询定时执行日志")
	public R getJobLog(Page page, SysJobLog sysJobLog) {
		return R.ok(sysJobLogService.page(page, Wrappers.query(sysJobLog)));
	}

	/**
	 * 检验任务名称和任务组联合是否唯一
	 * @return
	 */
	@GetMapping("/is-valid-task-name")
	@ApiOperation(value = "检验任务名称和任务组联合是否唯一")
	public R isValidTaskName(@RequestParam String jobName, @RequestParam String jobGroup) {
		return this.sysJobService
				.count(Wrappers.query(SysJob.builder().jobName(jobName).jobGroup(jobGroup).build())) > 0
						? R.failed("任务重复，请检查此组内是否已包含同名任务") : R.ok();
	}

}
