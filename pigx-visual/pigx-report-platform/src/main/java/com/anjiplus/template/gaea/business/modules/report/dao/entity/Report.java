package com.anjiplus.template.gaea.business.modules.report.dao.entity;

import com.anji.plus.gaea.annotation.Unique;
import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author chenkening
 * @date 2021/3/26 10:20
 */
@TableName(keepGlobalPrefix=true, value="gaea_report")
@Data
public class Report extends GaeaBaseEntity {

    @ApiModelProperty(value = "名称")
    private String reportName;

    @ApiModelProperty(value = "报表编码")
    @Unique(code = ResponseCode.REPORT_CODE_ISEXIST)
    private String reportCode;

    @ApiModelProperty(value = "分组")
    private String reportGroup;

    @ApiModelProperty(value = "报表描述")
    private String reportDesc;

    @ApiModelProperty(value = "报表类型")
    private String reportType;

    @ApiModelProperty(value = "报表缩略图")
    private String reportImage;

    @ApiModelProperty(value = "报表作者")
    private String reportAuthor;

    @ApiModelProperty(value = "下载次数")
    private Long downloadCount;

    @ApiModelProperty(value = "0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG")
    private Integer enableFlag;

    @ApiModelProperty(value = "0--未删除 1--已删除 DIC_NAME=DELETE_FLAG")
    private Integer deleteFlag;
}
