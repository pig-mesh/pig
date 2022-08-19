
package com.anjiplus.template.gaea.business.modules.datasettransform.service;

import com.alibaba.fastjson.JSONObject;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anjiplus.template.gaea.business.modules.datasettransform.controller.dto.DataSetTransformDto;
import com.anjiplus.template.gaea.business.modules.datasettransform.controller.param.DataSetTransformParam;
import com.anjiplus.template.gaea.business.modules.datasettransform.dao.entity.DataSetTransform;

import java.util.List;

/**
* @desc DataSetTransform 数据集数据转换服务接口
* @author Raod
* @date 2021-03-18 12:13:15.591309400
**/
public interface DataSetTransformService extends GaeaBaseService<DataSetTransformParam, DataSetTransform> {

    List<JSONObject> transform(List<DataSetTransformDto> dataSetTransformDtoList, List<JSONObject> data);

}
