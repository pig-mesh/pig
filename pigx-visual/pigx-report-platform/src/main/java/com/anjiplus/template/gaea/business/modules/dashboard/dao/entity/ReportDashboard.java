
package com.anjiplus.template.gaea.business.modules.dashboard.dao.entity;

import com.anji.plus.gaea.annotation.Unique;
import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description 大屏设计 entity
* @author Raod
* @date 2021-04-12 14:52:21.761
**/
@TableName(keepGlobalPrefix=true, value="gaea_report_dashboard")
@Data
public class ReportDashboard extends GaeaBaseEntity {
    @ApiModelProperty(value = "报表编码")
    @Unique(code = ResponseCode.REPORT_CODE_ISEXIST)
    private String reportCode;

    @ApiModelProperty(value = "看板标题")
    private String title;

    @ApiModelProperty(value = "宽度px")
    private Long width;

    @ApiModelProperty(value = "高度px")
    private Long height;

    @ApiModelProperty(value = "背景色")
    private String backgroundColor;

    @ApiModelProperty(value = "背景图片")
    private String backgroundImage;

    @ApiModelProperty(value = "工作台中的辅助线")
    private String presetLine;

    @ApiModelProperty(value = "自动刷新间隔秒，数据字典REFRESH_TYPE")
    private Integer refreshSeconds;

    @ApiModelProperty(value = "0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG")
    private Integer enableFlag;

    @ApiModelProperty(value = " 0--未删除 1--已删除 DIC_NAME=DEL_FLAG")
    private Integer deleteFlag;

    @ApiModelProperty(value = "排序，降序")
    private Integer sort;


}
