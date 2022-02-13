
package com.anjiplus.template.gaea.business.modules.dashboardwidget.service;

import com.anjiplus.template.gaea.business.modules.dashboardwidget.controller.param.ReportDashboardWidgetParam;
import com.anjiplus.template.gaea.business.modules.dashboardwidget.dao.entity.ReportDashboardWidget;
import com.anji.plus.gaea.curd.service.GaeaBaseService;

/**
* @desc ReportDashboardWidget 大屏看板数据渲染服务接口
* @author Raod
* @date 2021-04-12 15:12:43.724
**/
public interface ReportDashboardWidgetService extends GaeaBaseService<ReportDashboardWidgetParam, ReportDashboardWidget> {

    /***
     * 查询详情
     *
     * @param id
     */
    ReportDashboardWidget getDetail(Long id);
}
