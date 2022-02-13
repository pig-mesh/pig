
package com.anjiplus.template.gaea.business.modules.dashboardwidget.controller.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;


/**
*
* @description 大屏看板数据渲染 dto
* @author Raod
* @date 2021-04-12 15:12:43.724
**/
@Data
public class ReportDashboardWidgetDto implements Serializable {

    /**
     * 组件类型参考字典DASHBOARD_PANEL_TYPE
     */
    private String type;

    /**
     * value
     */
    private ReportDashboardWidgetValueDto value;

    /**
     * options
     */
    private JSONObject options;

}
