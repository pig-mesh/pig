
package com.anjiplus.template.gaea.business.modules.reportexcel.controller.dto;

import com.anji.plus.gaea.curd.dto.GaeaBaseDTO;
import lombok.Data;

import java.io.Serializable;


/**
 * @author chenkening
 * @date 2021/4/13 15:12
 */
@Data
public class ReportExcelDto extends GaeaBaseDTO implements Serializable {
    /**
     * 报表名称
     */
    private String reportName;

    /**
     * 报表编码
     */
    private String reportCode;

    /**
     * 数据集编码，以|分割
     */
    private String setCodes;

    /**
     * 分组
     */
    private String reportGroup;

    /**
     * 数据集查询参数
     */
    private String setParam;

    /**
     * 报表json字符串
     */
    private String jsonStr;

    /**
     * 报表类型
     */
    private String reportType;

    /**
     * 数据总计
     */
    private long total;

    /**
     * 导出类型
     */
    private String exportType;

}
