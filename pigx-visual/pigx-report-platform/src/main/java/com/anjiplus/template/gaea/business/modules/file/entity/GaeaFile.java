package com.anjiplus.template.gaea.business.modules.file.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * (GaeaFile)实体类
 *
 * @author peiyanni
 * @since 2021-02-18 14:48:20
 */
@TableName(keepGlobalPrefix=true, value = "gaea_file")
@Data
public class GaeaFile extends GaeaBaseEntity implements Serializable {

    @ApiModelProperty(value = "文件标识")
    private String fileId;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "url路径")
    private String urlPath;

    @ApiModelProperty(value = "内容说明")
    private String fileInstruction;
}
