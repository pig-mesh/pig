
package com.anjiplus.template.gaea.business.modules.dashboard.controller;

import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.annotation.log.GaeaAuditLog;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anjiplus.template.gaea.business.modules.dashboard.service.ReportDashboardService;
import com.anjiplus.template.gaea.business.modules.dashboard.controller.dto.ChartDto;
import com.anjiplus.template.gaea.business.modules.dashboard.controller.dto.ReportDashboardObjectDto;
import com.anjiplus.template.gaea.business.modules.reportshare.controller.dto.ReportShareDto;
import com.anjiplus.template.gaea.business.modules.reportshare.service.ReportShareService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @desc 大屏设计 controller
* @website https://gitee.com/anji-plus/gaea
* @author Raod
* @date 2021-04-12 14:52:21.761
**/
@RestController
@Api(tags = "大屏设计管理")
@Permission(code = "bigScreenManage", name = "大屏报表")
@RequestMapping("/reportDashboard")
public class ReportDashboardController {

    @Autowired
    private ReportDashboardService reportDashboardService;

    @Autowired
    private ReportShareService reportShareService;

    /**
     * 预览、查询大屏详情
     * @param reportCode
     * @return
     */
    @GetMapping({"/{reportCode}"})
    @Permission(code = "view", name = "查看大屏")
    public ResponseBean detail(@PathVariable("reportCode") String reportCode) {
        return ResponseBean.builder().data(reportDashboardService.getDetail(reportCode)).build();
    }

    /**
     * 保存大屏设计
     * @param dto
     * @return
     */
    @PostMapping
    @Permission(code = "design", name = "设计大屏")
    @GaeaAuditLog(pageTitle = "新增")
    public ResponseBean insert(@RequestBody ReportDashboardObjectDto dto) {
        reportDashboardService.insertDashboard(dto);
        return ResponseBean.builder().build();
    }


    /**
     * 获取去单个图层数据
     * @param dto
     * @return
     */
    @PostMapping("/getData")
    @Permission(code = "view", name = "查看大屏")
    public ResponseBean getData(@RequestBody ChartDto dto) {
        return ResponseBean.builder().data(reportDashboardService.getChartData(dto)).build();
    }


    /**
     * 导出大屏
     * @param reportCode
     * @return
     */
    @GetMapping("/export")
    @Permission(code = "export", name = "导出大屏")
    public ResponseEntity<byte[]> exportDashboard(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestParam("reportCode") String reportCode, @RequestParam(value = "showDataSet",required = false, defaultValue = "1") Integer showDataSet) {
        return reportDashboardService.exportDashboard(request, response, reportCode, showDataSet);
    }

    /**
     * 导入大屏
     * @param file  导入的zip文件
     * @param reportCode
     * @return
     */
    @PostMapping("/import/{reportCode}")
    @Permission(code = "import", name = "导入大屏")
    public ResponseBean importDashboard(@RequestParam("file") MultipartFile file, @PathVariable("reportCode") String reportCode) {
        reportDashboardService.importDashboard(file, reportCode);
        return ResponseBean.builder().build();
    }

    @PostMapping("/share")
    @GaeaAuditLog(pageTitle = "分享")
    public ResponseBean share(@Validated @RequestBody ReportShareDto dto) {
        return ResponseBean.builder().data(reportShareService.insertShare(dto)).build();
    }

}
