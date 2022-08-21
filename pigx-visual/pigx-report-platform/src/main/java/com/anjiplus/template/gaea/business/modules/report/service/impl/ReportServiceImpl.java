package com.anjiplus.template.gaea.business.modules.report.service.impl;

import com.anji.plus.gaea.constant.BaseOperationEnum;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.exception.BusinessException;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.anjiplus.template.gaea.business.enums.ReportTypeEnum;
import com.anjiplus.template.gaea.business.modules.dashboard.dao.entity.ReportDashboard;
import com.anjiplus.template.gaea.business.modules.dashboard.service.ReportDashboardService;
import com.anjiplus.template.gaea.business.modules.dashboardwidget.dao.entity.ReportDashboardWidget;
import com.anjiplus.template.gaea.business.modules.dashboardwidget.service.ReportDashboardWidgetService;
import com.anjiplus.template.gaea.business.modules.report.controller.dto.ReportDto;
import com.anjiplus.template.gaea.business.modules.report.dao.ReportMapper;
import com.anjiplus.template.gaea.business.modules.report.dao.entity.Report;
import com.anjiplus.template.gaea.business.modules.report.service.ReportService;
import com.anjiplus.template.gaea.business.modules.reportexcel.dao.entity.ReportExcel;
import com.anjiplus.template.gaea.business.modules.reportexcel.service.ReportExcelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author chenkening
 * @date 2021/3/26 10:35
 */
@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportMapper reportMapper;

	@Autowired
	private ReportDashboardService reportDashboardService;

	@Autowired
	private ReportDashboardWidgetService reportDashboardWidgetService;

	@Autowired
	private ReportExcelService reportExcelService;

	@Override
	public GaeaBaseMapper<Report> getMapper() {
		return reportMapper;
	}

	@Override
	public void processBatchBeforeOperation(List<Report> entities, BaseOperationEnum operationEnum)
			throws BusinessException {
		ReportService.super.processBatchAfterOperation(entities, operationEnum);
		switch (operationEnum) {
			case DELETE_BATCH:
				entities.forEach(report -> {
					Long id = report.getId();
					Report delReport = selectOne(id);
					if (null == delReport) {
						return;
					}
					String reportCode = delReport.getReportCode();
					String reportType = delReport.getReportType();
					switch (ReportTypeEnum.valueOf(reportType)) {
						case report_screen:
							LambdaQueryWrapper<ReportDashboard> reportDashboardLambdaQueryWrapper = Wrappers
									.lambdaQuery();
							reportDashboardLambdaQueryWrapper.eq(ReportDashboard::getReportCode, reportCode);
							reportDashboardService.delete(reportDashboardLambdaQueryWrapper);

							LambdaQueryWrapper<ReportDashboardWidget> reportDashboardWidgetLambdaQueryWrapper = Wrappers
									.lambdaQuery();
							reportDashboardWidgetLambdaQueryWrapper.eq(ReportDashboardWidget::getReportCode,
									reportCode);
							reportDashboardWidgetService.delete(reportDashboardWidgetLambdaQueryWrapper);

							break;
						case report_excel:
							LambdaQueryWrapper<ReportExcel> reportExcelLambdaQueryWrapper = Wrappers.lambdaQuery();
							reportExcelLambdaQueryWrapper.eq(ReportExcel::getReportCode, reportCode);
							reportExcelService.delete(reportExcelLambdaQueryWrapper);
							break;
						default:
					}
				});
				break;
			default:

		}
	}

	/**
	 * 下载次数+1
	 * @param reportCode
	 */
	@Override
	public void downloadStatistics(String reportCode) {
		Report report = selectOne("report_code", reportCode);
		if (null != report) {
			Long downloadCount = report.getDownloadCount();
			if (null == downloadCount) {
				downloadCount = 0L;
			}
			else {
				downloadCount++;
			}
			report.setDownloadCount(downloadCount);
			update(report);
		}

	}

	@Override
	public void copy(ReportDto dto) {
		if (null == dto.getId()) {
			throw BusinessExceptionBuilder.build(ResponseCode.NOT_NULL, "id");
		}
		if (StringUtils.isBlank(dto.getReportCode())) {
			throw BusinessExceptionBuilder.build(ResponseCode.NOT_NULL, "报表编码");
		}
		Report report = selectOne(dto.getId());
		String reportCode = report.getReportCode();
		Report copyReport = copyReport(report, dto);
		// 复制主表数据
		insert(copyReport);
		String copyReportCode = copyReport.getReportCode();
		String reportType = report.getReportType();
		switch (ReportTypeEnum.valueOf(reportType)) {
			case report_screen:
				// 查询看板
				ReportDashboard reportDashboard = reportDashboardService.selectOne("report_code", reportCode);
				if (null != reportDashboard) {
					reportDashboard.setId(null);
					reportDashboard.setReportCode(copyReportCode);
					reportDashboardService.insert(reportDashboard);
				}

				// 查询组件
				List<ReportDashboardWidget> reportDashboardWidgetList = reportDashboardWidgetService.list("report_code",
						reportCode);
				if (!CollectionUtils.isEmpty(reportDashboardWidgetList)) {
					String finalCopyReportCode = copyReportCode;
					reportDashboardWidgetList.forEach(reportDashboardWidget -> {
						reportDashboardWidget.setId(null);
						reportDashboardWidget.setReportCode(finalCopyReportCode);
					});
					reportDashboardWidgetService.insertBatch(reportDashboardWidgetList);
				}

				break;
			case report_excel:
				ReportExcel reportExcel = reportExcelService.selectOne("report_code", reportCode);
				if (null != reportExcel) {
					reportExcel.setId(null);
					reportExcel.setReportCode(copyReportCode);
					reportExcelService.insert(reportExcel);
				}

				break;
			default:
		}
	}

	private Report copyReport(Report report, ReportDto dto) {
		// 复制主表数据
		Report copyReport = new Report();
		GaeaBeanUtils.copyAndFormatter(report, copyReport);
		copyReport.setReportCode(dto.getReportCode());
		copyReport.setReportName(dto.getReportName());
		return copyReport;
	}

	@Override
	public void processBeforeOperation(Report entity, BaseOperationEnum operationEnum) throws BusinessException {

	}

}
