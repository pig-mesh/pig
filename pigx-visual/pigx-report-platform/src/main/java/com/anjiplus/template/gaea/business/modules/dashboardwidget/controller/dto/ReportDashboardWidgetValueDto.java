
package com.anjiplus.template.gaea.business.modules.dashboardwidget.controller.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;


/**
*
* @description 大屏看板数据渲染 dto，已弃用
* @author Raod
* @date 2021-04-12 15:12:43.724
**/
@Data
public class ReportDashboardWidgetValueDto implements Serializable {
    /** 报表编码 */
    private String reportCode;

    /** 组件的渲染属性json */
    private JSONObject setup;

    /** 组件的数据属性json */
    private JSONObject data;

    /** 组件的配置属性json */
    private JSONObject collapse;

    /** 组件的大小位置属性json */
    private JSONObject position;

    private String options;

    /** 自动刷新间隔秒 */
    private Integer refreshSeconds;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
    private Integer enableFlag;

    /**  0--未删除 1--已删除 DIC_NAME=DEL_FLAG */
    private Integer deleteFlag;

    /** 排序，图层的概念 */
    private Long sort;

}
