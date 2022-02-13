
package com.anjiplus.template.gaea.business.modules.dataset.controller.dto;

import com.anji.plus.gaea.curd.dto.GaeaBaseDTO;
import com.anjiplus.template.gaea.business.modules.datasetparam.controller.dto.DataSetParamDto;
import com.anjiplus.template.gaea.business.modules.datasettransform.controller.dto.DataSetTransformDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
*
* @description 数据集 dto
* @author Raod
* @date 2021-03-18 12:11:31.150755900
**/
@Data
public class DataSetDto extends GaeaBaseDTO implements Serializable {
    /** 数据集编码 */
     private String setCode;

    /** 数据集名称 */
     private String setName;

    /** 数据集描述 */
     private String setDesc;

    /** 数据集类型 */
    private String setType;


    /** 数据源编码 */
     private String sourceCode;

    /** 动态查询sql或者接口中的请求体 */
     private String dynSentence;

    /** 结果案例 */
     private String caseResult;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
     private Integer enableFlag;

    /** 0--未删除 1--已删除 DIC_NAME=DELETE_FLAG */
     private Integer deleteFlag;

    /** 请求参数集合 */
    private List<DataSetParamDto> dataSetParamDtoList;

    /** 数据转换集合 */
    private List<DataSetTransformDto> dataSetTransformDtoList;

    /** 传入的自定义参数*/
    private Map<String, Object> contextData;

    private Set<String> setParamList;

    /**指定字段*/
    private String fieldLabel;

}
