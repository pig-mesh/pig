
package com.anjiplus.template.gaea.business.modules.datasetparam.service;

import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anjiplus.template.gaea.business.modules.datasetparam.controller.dto.DataSetParamDto;
import com.anjiplus.template.gaea.business.modules.datasetparam.controller.param.DataSetParamParam;
import com.anjiplus.template.gaea.business.modules.datasetparam.dao.entity.DataSetParam;

import java.util.List;
import java.util.Map;

/**
 * @author Raod
 * @desc DataSetParam 数据集动态参数服务接口
 * @date 2021-03-18 12:12:33.108033200
 **/
public interface DataSetParamService extends GaeaBaseService<DataSetParamParam, DataSetParam> {

    /**
     * 参数替换
     *
     * @param contextData
     * @param dynSentence
     * @return
     */
    String transform(Map<String, Object> contextData, String dynSentence);

    /**
     * 参数替换
     *
     * @param dataSetParamDtoList
     * @param dynSentence
     * @return
     */
    String transform(List<DataSetParamDto> dataSetParamDtoList, String dynSentence);

    /**
     * 参数校验  js脚本
     * @param dataSetParamDto
     * @return
     */
    Object verification(DataSetParamDto dataSetParamDto);

    /**
     * 参数校验  js脚本
     *
     * @param dataSetParamDtoList
     * @return
     */
    boolean verification(List<DataSetParamDto> dataSetParamDtoList, Map<String, Object> contextData);
}
