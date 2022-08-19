
package com.anjiplus.template.gaea.business.modules.datasettransform.controller;

import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anjiplus.template.gaea.business.modules.datasettransform.controller.dto.DataSetTransformDto;
import com.anjiplus.template.gaea.business.modules.datasettransform.controller.param.DataSetTransformParam;
import com.anjiplus.template.gaea.business.modules.datasettransform.dao.entity.DataSetTransform;
import com.anjiplus.template.gaea.business.modules.datasettransform.service.DataSetTransformService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @desc 数据集数据转换 controller
* @website https://gitee.com/anji-plus/gaea
* @author Raod
* @date 2021-03-18 12:13:15.591309400
**/
@RestController
@Api(tags = "数据集数据转换管理")
@RequestMapping("/dataSetTransform")
public class DataSetTransformController extends GaeaBaseController<DataSetTransformParam, DataSetTransform, DataSetTransformDto> {

    @Autowired
    private DataSetTransformService dataSetTransformService;

    @Override
    public GaeaBaseService<DataSetTransformParam, DataSetTransform> getService() {
        return dataSetTransformService;
    }

    @Override
    public DataSetTransform getEntity() {
        return new DataSetTransform();
    }

    @Override
    public DataSetTransformDto getDTO() {
        return new DataSetTransformDto();
    }

}
