package com.anjiplus.template.gaea.business.modules.report.service.impl;

import com.anji.plus.gaea.constant.BaseOperationEnum;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.exception.BusinessException;
import com.anjiplus.template.gaea.business.modules.report.controller.dto.ReportDto;
import com.anjiplus.template.gaea.business.modules.report.dao.ReportMapper;
import com.anjiplus.template.gaea.business.modules.report.dao.entity.Report;
import com.anjiplus.template.gaea.business.modules.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author chenkening
 * @date 2021/3/26 10:35
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public GaeaBaseMapper<Report> getMapper() {
        return reportMapper;
    }


    @Override
    public void delReport(ReportDto reportDto) {
        deleteById(reportDto.getId());
        //删除gaea_report_excel、gaea_report_dashboard、gaea_report_dashboard_widget
        //...
    }

    /**
     * 下载次数+1
     *
     * @param reportCode
     */
    @Override
    public void downloadStatistics(String reportCode) {
        Report report = selectOne("report_code", reportCode);
        if (null != report) {
            Long downloadCount = report.getDownloadCount();
            if (null == downloadCount) {
                downloadCount = 0L;
            }else {
                downloadCount++;
            }
            report.setDownloadCount(downloadCount);
            update(report);
        }

    }

    @Override
    public void processBeforeOperation(Report entity, BaseOperationEnum operationEnum) throws BusinessException {

    }
}
