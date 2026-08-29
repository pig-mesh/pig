package com.pig4cloud.pig.daemon.quartz.controller;

import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.daemon.quartz.constants.JobTypeQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.service.SysJobLogService;
import com.pig4cloud.pig.daemon.quartz.service.SysJobService;
import com.pig4cloud.pig.daemon.quartz.util.RestTaskUrlValidator;
import com.pig4cloud.pig.daemon.quartz.util.TaskUtil;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SysJobControllerTests {

	private final SysJobService sysJobService = mock(SysJobService.class);

	private final SysJobLogService sysJobLogService = mock(SysJobLogService.class);

	private final TaskUtil taskUtil = mock(TaskUtil.class);

	private final Scheduler scheduler = mock(Scheduler.class);

	private final RestTaskUrlValidator validator = mock(RestTaskUrlValidator.class);

	private final SysJobController controller = new SysJobController(sysJobService, sysJobLogService, taskUtil,
			scheduler, validator);

	@Test
	void rejectsRestTaskBeforeSaving() {
		SysJob sysJob = restJob();
		when(validator.isAllowed(sysJob.getExecutePath())).thenReturn(false);

		R result = controller.save(sysJob);

		assertThat(result.isOk()).isFalse();
		assertThat(result.getMsg()).isEqualTo("REST任务地址未配置白名单或不在白名单中");
		verifyNoInteractions(taskUtil, sysJobService, scheduler);
	}

	@Test
	void rejectsRestTaskBeforeUpdating() {
		SysJob sysJob = restJob();
		when(validator.isAllowed(sysJob.getExecutePath())).thenReturn(false);

		R result = controller.updateById(sysJob);

		assertThat(result.isOk()).isFalse();
		assertThat(result.getMsg()).isEqualTo("REST任务地址未配置白名单或不在白名单中");
		verifyNoInteractions(taskUtil, sysJobService, scheduler);
	}

	private SysJob restJob() {
		return SysJob.builder()
			.jobId(1L)
			.jobType(JobTypeQuartzEnum.REST.getType())
			.executePath("http://127.0.0.1/internal")
			.build();
	}

}
