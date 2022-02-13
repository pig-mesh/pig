
package com.anjiplus.template.gaea.business.modules.dashboard.controller.dto;

import com.anjiplus.template.gaea.business.modules.dashboardwidget.controller.dto.ReportDashboardWidgetDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
*
* @description 大屏设计 dto
* @author Raod
* @date 2021-04-12 14:52:21.761
**/
@Data
public class ReportDashboardObjectDto implements Serializable {

    /** 报表编码 */
    private String reportCode;
    /**
     * 报表编码
     */
    private ReportDashboardDto dashboard;

    /**
     * 大屏画布中的组件
     */
    private List<ReportDashboardWidgetDto> widgets;

}
