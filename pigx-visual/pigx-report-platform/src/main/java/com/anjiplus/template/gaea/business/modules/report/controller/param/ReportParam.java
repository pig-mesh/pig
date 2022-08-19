package com.anjiplus.template.gaea.business.modules.report.controller.param;

import com.anji.plus.gaea.annotation.Query;
import com.anji.plus.gaea.constant.QueryEnum;
import com.anji.plus.gaea.curd.params.PageParam;
import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author chenkening
 * @date 2021/3/26 10:40
 */
@Data
public class ReportParam extends PageParam implements Serializable{

    /** 报表名称 */
    @Query(QueryEnum.LIKE)
    private String reportName;

    /** 报表作者 */
    @Query(QueryEnum.LIKE)
    private String reportAuthor;

    /** 报表编码 */
    @Query(QueryEnum.LIKE)
    private String reportCode;

    /** 报表类型 */
    @Query(QueryEnum.EQ)
    private String reportType;


}
