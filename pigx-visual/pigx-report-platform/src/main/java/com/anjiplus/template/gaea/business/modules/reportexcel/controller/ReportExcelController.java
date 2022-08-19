package com.anjiplus.template.gaea.business.modules.reportexcel.controller;

import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.annotation.log.GaeaAuditLog;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.code.ResponseCode;
import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anjiplus.template.gaea.business.modules.reportexcel.controller.dto.ReportExcelDto;
import com.anjiplus.template.gaea.business.modules.reportexcel.controller.param.ReportExcelParam;
import com.anjiplus.template.gaea.business.modules.reportexcel.dao.entity.ReportExcel;
import com.anjiplus.template.gaea.business.modules.reportexcel.service.ReportExcelService;
import com.anjiplus.template.gaea.business.modules.reportshare.controller.dto.ReportShareDto;
import com.anjiplus.template.gaea.business.modules.reportshare.service.ReportShareService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenkening
 * @date 2021/4/13 15:12
 */
@RestController
@Api(tags = "报表表格管理")
@Permission(code = "excelManage", name = "报表管理")
@RequestMapping("/reportExcel")
public class ReportExcelController extends GaeaBaseController<ReportExcelParam, ReportExcel, ReportExcelDto> {

    @Autowired
    private ReportExcelService reportExcelService;

    @Autowired
    private ReportShareService reportShareService;

    @Override
    public GaeaBaseService<ReportExcelParam, ReportExcel> getService() {
        return reportExcelService;
    }

    @Override
    public ReportExcel getEntity() {
        return new ReportExcel();
    }

    @Override
    public ReportExcelDto getDTO() {
        return new ReportExcelDto();
    }

    @GetMapping("/detailByReportCode/{reportCode}")
    @Permission(code = "query", name = "详情")
    @GaeaAuditLog(pageTitle = "详情")
    public ResponseBean detailByReportCode(@PathVariable String reportCode) {
        ReportExcelDto reportExcelDto = reportExcelService.detailByReportCode(reportCode);
        return ResponseBean.builder().data(reportExcelDto).build();
    }

    @PostMapping("/preview")
    @Permission(code = "view", name = "预览")
    @GaeaAuditLog(pageTitle = "预览")
    public ResponseBean preview(@RequestBody ReportExcelDto reportExcelDto) {
        ReportExcelDto result = reportExcelService.preview(reportExcelDto);
        return ResponseBean.builder().data(result).build();
    }


    @PostMapping("/exportExcel")
    @Permission(code = "export", name = "导出")
    @GaeaAuditLog(pageTitle = "报表导出")
    public ResponseBean exportExcel(@RequestBody ReportExcelDto reportExcelDto) {

        return ResponseBean.builder().code(ResponseCode.SUCCESS_CODE)
                .data(reportExcelService.exportExcel(reportExcelDto))
                .message("导出成功，请稍后在文件管理中查看").build();
    }

//    @PostMapping("/exportPdf")
//    public ResponseBean exportPdf(@RequestBody ReportExcelDto reportExcelDto) {
//        reportExcelService.exportPdf(reportExcelDto);
//        return ResponseBean.builder().code(ResponseCode.SUCCESS_CODE)
//                .build();
//    }

    @PostMapping("/share")
    @GaeaAuditLog(pageTitle = "excel分享")
    @Permission(code = "reportExcel", name = "分享报表")
    public ResponseBean share(@Validated @RequestBody ReportShareDto dto) {
        return ResponseBean.builder().data(reportShareService.insertShare(dto)).build();
    }
}
