
package com.anjiplus.template.gaea.business.modules.dashboardwidget.service.impl;

import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anjiplus.template.gaea.business.modules.dashboardwidget.dao.ReportDashboardWidgetMapper;
import com.anjiplus.template.gaea.business.modules.dashboardwidget.dao.entity.ReportDashboardWidget;
import com.anjiplus.template.gaea.business.modules.dashboardwidget.service.ReportDashboardWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @desc ReportDashboardWidget 大屏看板数据渲染服务实现
 * @author Raod
 * @date 2021-04-12 15:12:43.724
 **/
@Service
// @RequiredArgsConstructor
public class ReportDashboardWidgetServiceImpl implements ReportDashboardWidgetService {

	@Autowired
	private ReportDashboardWidgetMapper reportDashboardWidgetMapper;

	@Override
	public GaeaBaseMapper<ReportDashboardWidget> getMapper() {
		return reportDashboardWidgetMapper;
	}

	@Override
	public ReportDashboardWidget getDetail(Long id) {
		ReportDashboardWidget reportDashboardWidget = this.selectOne(id);
		return reportDashboardWidget;
	}

}
