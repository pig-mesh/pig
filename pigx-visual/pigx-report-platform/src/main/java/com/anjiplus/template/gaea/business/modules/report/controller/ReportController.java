package com.anjiplus.template.gaea.business.modules.report.controller;

import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.annotation.log.GaeaAuditLog;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anjiplus.template.gaea.business.modules.report.controller.dto.ReportDto;
import com.anjiplus.template.gaea.business.modules.report.controller.param.ReportParam;
import com.anjiplus.template.gaea.business.modules.report.dao.entity.Report;
import com.anjiplus.template.gaea.business.modules.report.service.ReportService;
import com.anjiplus.template.gaea.business.modules.reportshare.controller.dto.ReportShareDto;
import com.anjiplus.template.gaea.business.modules.reportshare.service.ReportShareService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @author chenkening
 * @date 2021/3/26 10:19
 */
@RestController
@Api(tags = "报表数据管理")
@Permission(code = "reportManage", name = "报表管理")
@RequestMapping("/report")
public class ReportController extends GaeaBaseController<ReportParam, Report, ReportDto> {

	@Autowired
	private ReportShareService reportShareService;

	@Autowired
	private ReportService reportService;

	@Override
	public GaeaBaseService<ReportParam, Report> getService() {
		return reportService;
	}

	@Override
	public Report getEntity() {
		return new Report();
	}

	@Override
	public ReportDto getDTO() {
		return new ReportDto();
	}

	@DeleteMapping("/delReport")
	@Permission(code = "delete", name = "删除")
	@GaeaAuditLog(pageTitle = "删除")
	public ResponseBean delReport(@RequestBody ReportDto reportDto) {
		reportService.delReport(reportDto);
		return ResponseBean.builder().build();
	}

	@PostMapping("/share")
	@Permission(code = "share", name = "分享")
	@GaeaAuditLog(pageTitle = "分享")
	public ResponseBean share(@Validated @RequestBody ReportShareDto dto) {
		return ResponseBean.builder().data(reportShareService.insertShare(dto)).build();
	}

}
