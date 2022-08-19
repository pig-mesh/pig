package com.anjiplus.template.gaea.business.modules.dict.controller.dto;

import com.anji.plus.gaea.curd.dto.GaeaBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据字典项(GaeaDictItem)实体类
 *
 * @author lirui
 * @since 2021-03-10 13:05:59
 */
@ApiModel(value = "数据字典项")
@Data
public class GaeaDictItemDTO extends GaeaBaseDTO implements Serializable {
    /**
     * 数据字典编码
     */
    @ApiModelProperty(value = "数据字典编码")
    private String dictCode;
    /**
     * 字典项名称
     */
    @ApiModelProperty(value = "字典项名称")
    private String itemName;
    /**
     * 字典项值
     */
    @ApiModelProperty(value = "字典项值")
    private String itemValue;

    /**
     * 字典项扩展
     */
    @ApiModelProperty(value = "字典项扩展")
    private String itemExtend;
    /**
     * 语言标识
     */
    @ApiModelProperty(value = "语言标识")
    private String locale;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 1：启用，0:禁用
     */
    private Integer enabled;
}
